/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package v1.validation.fullReturn

import play.api.libs.json.{Json, JsPath, JsValue}
import v1.models.Validation.ValidationResult
import v1.models.fullReturn.{AdjustedGroupInterestModel, FullReturnModel, UkCompanyModel}
import v1.models.{Original, Revised, Validation}
import v1.validation.BaseValidation
import v1.validation.errors._

trait FullReturnValidator extends BaseValidation {

  import cats.implicits._

  val fullReturnModel: FullReturnModel

  private def validateRevisedReturnDetails: ValidationResult[_] = {
    (fullReturnModel.submissionType, fullReturnModel.revisedReturnDetails) match {
      case (Original, Some(details)) => RevisedReturnDetailsSupplied(details).invalidNec
      case (Revised, None) => RevisedReturnDetailsNotSupplied.invalidNec
      case (Revised, Some(details)) => details.validate(JsPath)
      case _ => fullReturnModel.revisedReturnDetails.validNec
    }
  }

  private def validateParentCompany: ValidationResult[Boolean] = {
    (fullReturnModel.reportingCompany.sameAsUltimateParent, fullReturnModel.parentCompany) match {
      case (true, Some(details)) => ParentCompanyDetailsSupplied(details).invalidNec
      case (false, None) => ParentCompanyDetailsNotSupplied.invalidNec
      case _ => fullReturnModel.appointedReportingCompany.validNec
    }
  }

  private def validateAngie: ValidationResult[BigDecimal] = {
    val angie: BigDecimal = fullReturnModel.angie
    angie match {
      case a if a < 0 => NegativeAngieError(angie).invalidNec
      case a if a % 0.01 != 0 => AngieDecimalError(angie).invalidNec
      case _ => angie.validNec
    }
  }

  private def validateNotBothRestrictionsAndReactivations: ValidationResult[Boolean] = {
    (fullReturnModel.groupSubjectToInterestRestrictions, fullReturnModel.groupSubjectToInterestReactivation) match {
      case (true, true) =>
        GroupLevelInterestRestrictionsAndReactivationSupplied(
          fullReturnModel.groupSubjectToInterestRestrictions, fullReturnModel.groupSubjectToInterestReactivation).invalidNec
      case _ => fullReturnModel.groupSubjectToInterestRestrictions.validNec
    }
  }

  private def validateAllocatedRestrictions: ValidationResult[Boolean] = {
    (fullReturnModel.groupSubjectToInterestRestrictions, fullReturnModel.groupSubjectToInterestReactivation, fullReturnModel.ukCompanies.zipWithIndex) match {
      case (false, _, companies) if companies.exists(_._1.allocatedRestrictions.nonEmpty) => combineValidations(companies.collect {
        case (company, i) if company.allocatedRestrictions.nonEmpty => CompaniesContainedAllocatedRestrictions(company, i).invalidNec
      }: _*)
      case _ => fullReturnModel.groupSubjectToInterestRestrictions.validNec
    }
  }

  private def validateAllocatedReactivations: ValidationResult[Boolean] = {
    (fullReturnModel.groupSubjectToInterestReactivation, fullReturnModel.groupSubjectToInterestRestrictions, fullReturnModel.ukCompanies.zipWithIndex) match {
      case (false, _, companies) if companies.exists(_._1.allocatedReactivations.nonEmpty) => combineValidations(companies.collect {
        case (company, i) if company.allocatedReactivations.nonEmpty => CompaniesContainedAllocatedReactivations(company, i).invalidNec
      }: _*)
      case _ => fullReturnModel.groupSubjectToInterestReactivation.validNec
    }
  }

  private def validateTotalReactivationsNotGreaterThanCapacity: ValidationResult[BigDecimal] = {
    val capacity: BigDecimal = fullReturnModel.groupLevelAmount.interestReactivationCap
    val calculatedReactivations: BigDecimal = fullReturnModel.aggregateAllocatedReactivations
    if (calculatedReactivations > capacity) {
      TotalReactivationsNotGreaterThanCapacity(calculatedReactivations, capacity).invalidNec
    } else {
      calculatedReactivations.validNec
    }
  }

  private def validateTotalRestrictions: ValidationResult[BigDecimal] = {
    val restrictions: BigDecimal = fullReturnModel.totalRestrictions
    val calculatedRestrictions: BigDecimal = fullReturnModel.aggregateAllocatedRestrictions
    (restrictions, calculatedRestrictions) match {
      case (r, _) if r % 0.01 != 0 => TotalRestrictionsDecimalError(restrictions).invalidNec
      case (r, cr) if r != cr => TotalRestrictionsDoesNotMatch(restrictions, calculatedRestrictions).invalidNec
      case (_, _) => restrictions.validNec
    }
  }

  private def validateAggInterestAndReallocationOrRestrictionStatus: ValidationResult[BigDecimal] = {
    val aggregateNetTaxInterest: BigDecimal = fullReturnModel.aggregateNetTaxInterest
    val subjectRestrictions: Boolean = fullReturnModel.groupSubjectToInterestRestrictions

    if (aggregateNetTaxInterest > 0 && subjectRestrictions) {
      AggregateNetTaxInterestIncomeSubjectToRestrictions(subjectRestrictions).invalidNec
    } else {
      aggregateNetTaxInterest.validNec
    }
  }

  private def validateAdjustedNetGroupInterest: ValidationResult[Option[AdjustedGroupInterestModel]] = {
    (fullReturnModel.groupLevelElections.groupRatio.isElected, fullReturnModel.adjustedGroupInterest) match {
      case (true, None) => AdjustedNetGroupInterestNotSupplied.invalidNec
      case (false, Some(details)) => AdjustedNetGroupInterestSupplied(details).invalidNec
      case _ => fullReturnModel.adjustedGroupInterest.validNec
    }
  }

  private def validateAppointedReporter: ValidationResult[Boolean] = {
    if (fullReturnModel.appointedReportingCompany) fullReturnModel.appointedReportingCompany.validNec else {
      ReportingCompanyNotAppointed.invalidNec
    }
  }

  private def groupEstimateReasonIsValidLength(reason: String): Boolean = (reason.length >= 1 && reason.length <= 10000)

  private def groupEstimateReasonHasValidCharacters(reason: String): Boolean = {
    val regex = "^[ -~¢-¥©®±×÷‐₠-₿−-∝≈≠≣-≥]*$".r
    reason match {
      case regex(_ *) => true
      case _ => false
    }
  }

  private def validateGroupEstimateReason: ValidationResult[Option[String]] = 
    (fullReturnModel.returnContainsEstimates, fullReturnModel.groupEstimateReason) match {
      case (false, Some(reason)) => EstimateReasonSupplied(reason).invalidNec
      case (true, Some(reason)) if !groupEstimateReasonIsValidLength(reason) => EstimateReasonLengthError(reason).invalidNec
      case (true, Some(reason)) if !groupEstimateReasonHasValidCharacters(reason) => EstimateReasonCharacterError(reason).invalidNec
      case _ => fullReturnModel.groupEstimateReason.validNec
    }

  private def validateCompanyEstimateReasons: ValidationResult[_] = 
    (fullReturnModel.returnContainsEstimates, fullReturnModel.ukCompanies.zipWithIndex) match {
      case (false, companies) if companies.exists(_._1.companyEstimateReason.nonEmpty) => combineValidations(companies.collect {
        case (company, i) => company.companyEstimateReason match {
          case Some(reason) => CompaniesContainedEstimateReason(reason, i).invalidNec
          case None => company.companyEstimateReason.validNec
        }
      }: _*)
      case _ => fullReturnModel.returnContainsEstimates.validNec
    }

  private def validateReturnContainsEstimates: ValidationResult[_] = {
    val companyContainsAnEstimateReason = fullReturnModel.ukCompanies.exists(_.companyEstimateReason.nonEmpty)
    val groupEstimateReasonPopulated = fullReturnModel.groupEstimateReason.isDefined

    (fullReturnModel.returnContainsEstimates, companyContainsAnEstimateReason, groupEstimateReasonPopulated) match {
      case (true, false, false) => NoEstimatesSupplied(fullReturnModel.returnContainsEstimates).invalidNec
      case _ => fullReturnModel.returnContainsEstimates.validNec
    }
  }

  private def validateDeclaration: ValidationResult[Boolean] =
    fullReturnModel.declaration match {
      case true => fullReturnModel.declaration.validNec
      case false => ReturnDeclarationError(fullReturnModel.declaration).invalidNec
    }

  private def validateNetTaxInterest: ValidationResult[_] = {
    if (fullReturnModel.groupSubjectToInterestReactivation &&
      fullReturnModel.aggregateNetTaxInterest > fullReturnModel.groupLevelAmount.interestReactivationCap) {
      AggregateNetTaxInterestIncomeExceedsCap(fullReturnModel.groupLevelAmount.interestReactivationCap).invalidNec
    } else {
      fullReturnModel.aggregateNetTaxInterest.validNec
    }
  }

  private def validateTotalRestrictionsDoesntExceedAggNetTaxInterestExpense: ValidationResult[_] = {
    val aggregateNetTaxInterestExpense: Option[BigDecimal] =
      if (fullReturnModel.aggregateNetTaxInterest < 0) Some(fullReturnModel.aggregateNetTaxInterest * -1) else None

    aggregateNetTaxInterestExpense match {
      case Some(expense) if fullReturnModel.totalRestrictions > expense =>
        TotalRestrictionExceedsAggregateNetTaxInterestExpense(fullReturnModel.totalRestrictions).invalidNec
      case _ => fullReturnModel.totalRestrictions.validNec
    }

  }

  def validateReactivationCapSubjectToReactivations: ValidationResult[_] =
    if (!fullReturnModel.groupSubjectToInterestReactivation && fullReturnModel.groupLevelAmount.interestReactivationCap > 0) {
      ReactivationCapNotSubjectToReactivations(fullReturnModel.groupLevelAmount.interestReactivationCap).invalidNec
    } else {
      fullReturnModel.groupSubjectToInterestReactivation.validNec
    }

  def validate: ValidationResult[FullReturnModel] = {

    val validatedUkCompanies =
      if (fullReturnModel.ukCompanies.isEmpty) UkCompaniesEmpty.invalidNec else {
        combineValidations(fullReturnModel.ukCompanies.zipWithIndex.map {
          case (a, i) => a.validate(fullReturnModel.groupCompanyDetails.accountingPeriod)(JsPath \ s"ukCompanies[$i]")
        }: _*)
      }

    combineValidations(fullReturnModel.agentDetails.validate(JsPath \ "agentDetails"),
      fullReturnModel.reportingCompany.validate(JsPath \ "reportingCompany"),
      optionValidations(fullReturnModel.parentCompany.map(_.validate(JsPath \ "parentCompany"))),
      fullReturnModel.groupCompanyDetails.validate(JsPath \ "groupCompanyDetails"),
      fullReturnModel.groupLevelElections.validate(JsPath \ "groupLevelElections"),
      validatedUkCompanies,
      validateAngie,
      validateNotBothRestrictionsAndReactivations,
      validateAllocatedRestrictions,
      validateAllocatedReactivations,
      validateTotalReactivationsNotGreaterThanCapacity,
      validateTotalRestrictions,
      validateAggInterestAndReallocationOrRestrictionStatus,
      validateParentCompany,
      validateRevisedReturnDetails,
      validateAdjustedNetGroupInterest,
      validateAppointedReporter,
      fullReturnModel.groupLevelAmount.validate(JsPath \ "groupLevelAmount"),
      optionValidations(fullReturnModel.adjustedGroupInterest.map(_.validate(JsPath \ "adjustedGroupInterest"))),
      validateGroupEstimateReason,
      validateCompanyEstimateReasons,
      validateReturnContainsEstimates,
      validateDeclaration,
      validateNetTaxInterest,
      validateTotalRestrictionsDoesntExceedAggNetTaxInterestExpense,
      validateReactivationCapSubjectToReactivations
    ).map(_ => fullReturnModel)
  }
}

case class NegativeAngieError(amt: BigDecimal) extends Validation {
  val code = "NEGATIVE_ANGIE"
  val errorMessage: String = "ANGIE must be a positive number"
  val path: JsPath = JsPath \ "angie"
  val value: Option[JsValue] = Some(Json.toJson(amt))
}

case class AngieDecimalError(amt: BigDecimal) extends Validation {
  val code = "ANGIE_DECIMAL"
  val errorMessage: String = "ANGIE must be to 2 decimal places or less"
  val path: JsPath = JsPath \ "angie"
  val value: Option[JsValue] = Some(Json.toJson(amt))
}

case class GroupLevelInterestRestrictionsAndReactivationSupplied(groupSubjectToInterestRestrictions: Boolean, groupSubjectToInterestReactivation: Boolean)
  extends Validation {
  val code = "RESTRICTION_REACTIVATION_SUPPLIED"
  override val errorMessage: String = "Cannot have group level restriction and reactivation"
  val path: JsPath = JsPath \ ""
  val value: Option[JsValue] = Some(Json.toJson((
    "groupSubjectToInterestRestrictions: " + groupSubjectToInterestRestrictions,
    "groupSubjectToInterestReactivation: " + groupSubjectToInterestReactivation)))
}

case class TotalReactivationsNotGreaterThanCapacity(calculated: BigDecimal, capacity: BigDecimal) extends Validation {
  val code = "REACTIVATIONS_GREATER_THAN_CAP"
  val errorMessage: String = s"Total reactivations calculated cannot be greater than reactivation cap"
  val path: JsPath = JsPath \ "groupLevelAmount" \ "interestReactivationCap"
  val value: Option[JsValue] = None
}

case class TotalRestrictionsDecimalError(amt: BigDecimal) extends Validation {
  val code = "TOTAL_RESTRICTIONS_DECIMAL"
  val errorMessage: String = s"Total restrictions must be to 2 decimal places or less"
  val path: JsPath = JsPath \ "totalRestrictions"
  val value: Option[JsValue] = Some(Json.toJson(amt))
}

case class TotalRestrictionsDoesNotMatch(amt: BigDecimal, calculated: BigDecimal) extends Validation {
  val code = "TOTAL_RESTRICTIONS_MATCH"
  val errorMessage: String = s"Total of restrictions allocated does not match group disallowed amount"
  val path: JsPath = JsPath \ "totalRestrictions"
  val value: Option[JsValue] = None
}

case class CompaniesContainedAllocatedRestrictions(company: UkCompanyModel, i: Int) extends Validation {
  val code = "COMPANIES_RESTRICTIONS"
  val errorMessage: String = s"Group does not have an interest restriction so no allocated restrictions needed for this company"
  val path: JsPath = JsPath \ s"ukCompanies[$i]"
  val value: Option[JsValue] = None
}

case class CompaniesContainedAllocatedReactivations(company: UkCompanyModel, i: Int) extends Validation {
  val code = "COMPANIES_REACTIVATIONS"
  val errorMessage: String = s"No reactivation can be allocated to this company because the group is not subject to reactivations"
  val path: JsPath = JsPath \ s"ukCompanies[$i]"
  val value: Option[JsValue] = None
}

case object AdjustedNetGroupInterestNotSupplied extends Validation {
  val code = "ADJUSTED_GROUP_INTEREST_NOT_SUPPLIED"
  val errorMessage: String = "Group ratio % elected so qngie, group-EBITDA and group ratio should be provided"
  val path: JsPath = JsPath \ "adjustedGroupInterest"
  val value: Option[JsValue] = None
}

case class AdjustedNetGroupInterestSupplied(details: AdjustedGroupInterestModel) extends Validation {
  val code = "ADJUSTED_GROUP_INTEREST_SUPPLIED"
  val errorMessage: String = "Group ratio % not elected so qngie, group-EBITDA and group ratio should not be provided"
  val path: JsPath = JsPath \ "adjustedGroupInterest"
  val value: Option[JsValue] = Some(Json.toJson(details))
}

case class EstimateReasonSupplied(reason: String) extends Validation {
  val code = "ESTIMATE_REASON_SUPPLIED"
  val errorMessage: String = "Return contains estimate field is set to false, so group estimate reason field cannot be sent"
  val path: JsPath = JsPath \ "groupEstimateReason"
  val value: Option[JsValue] = Some(Json.toJson(reason))
}

case class EstimateReasonLengthError(reason: String) extends Validation {
  val code = "ESTIMATE_REASON_LENGTH"
  val errorMessage: String = s"Estimate reason must be between 1 and 10,000 characters"
  val path: JsPath = JsPath \ "groupEstimateReason"
  val value: Option[JsValue] = Some(Json.toJson(reason))
}

case class EstimateReasonCharacterError(reason: String) extends Validation {
  val code = "ESTIMATE_REASON_CHARACTERS"
  val errorMessage: String = "Estimate reason contains invalid characters"
  val path: JsPath = JsPath \ "groupEstimateReason"
  val value: Option[JsValue] = Some(Json.toJson(reason))
}

case class CompaniesContainedEstimateReason(reason: String, i: Int) extends Validation {
  val code = "COMPANY_ESTIMATE_REASON_SUPPLIED"
  val errorMessage: String = s"Return contains estimate field is set to false, so company estimate reason field cannot be sent"
  val path: JsPath = JsPath \ s"ukCompanies[$i]"
  val value: Option[JsValue] = Some(Json.toJson(reason))
}

case class NoEstimatesSupplied(bool: Boolean) extends Validation {
  val code = "ESTIMATE_REASON_NOT_SUPPLIED"
  val errorMessage: String = s"Return contains estimate field is set to true, so group estimate or company estimate reason needs to be provided"
  val path: JsPath = JsPath \ "returnContainsEstimates"
  val value: Option[JsValue] = Some(Json.toJson(bool))
}

case class AggregateNetTaxInterestIncomeExceedsCap(reactivationCap: BigDecimal) extends Validation {
  val code = "AGGREGATE_NET_TAX_CAP"
  val errorMessage: String = "The aggregate net-tax interest income exceeds the reactivation cap"
  val path: JsPath = JsPath \ "groupLevelAmount" \ "interestReactivationCap"
  val value: Option[JsValue] = Some(Json.toJson(reactivationCap))
}

case class TotalRestrictionExceedsAggregateNetTaxInterestExpense(totalRestrictions: BigDecimal) extends Validation {
  val code = "TOTAL_RESTRICTIONS_EXCEEDS_EXPENSE"
  val errorMessage: String = "The total restriction exceeds aggregate net-tax interest expense"
  val path: JsPath = JsPath \ "totalRestrictions"
  val value: Option[JsValue] = Some(Json.toJson(totalRestrictions))
}

case class AggregateNetTaxInterestIncomeSubjectToRestrictions(groupSubjectToInterestRestrictions: Boolean) extends Validation {
  val code = "SUBJECT_RESTRICTION_NET_INCOME"
  val errorMessage: String = "The group cannot be subject to restrictions and have an aggregate net tax-interest income"
  val path: JsPath = JsPath \ "groupSubjectToInterestRestrictions"
  val value: Option[JsValue] = Some(Json.toJson(groupSubjectToInterestRestrictions))
}

case class ReactivationCapNotSubjectToReactivations(reactivationCap: BigDecimal) extends Validation {
  val code = "REACTIVATION_CAP_SUBJECT_REACTIVATIONS"
  val errorMessage: String = "Reactivation cap not needed as group is not subject to reactivations"
  val path: JsPath = JsPath \ "groupLevelAmount" \ "interestReactivationCap"
  val value: Option[JsValue] = Some(Json.toJson(reactivationCap))
}
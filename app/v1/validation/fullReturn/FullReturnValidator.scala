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
import v1.models.{Original, ParentCompanyModel, Revised, RevisedReturnDetailsModel, Validation}
import v1.validation.BaseValidation

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
    val calculatedReactivations: BigDecimal = fullReturnModel.ukCompanies.foldLeft[BigDecimal](0) {
      (total, company) =>
        total + company.allocatedReactivations.fold[BigDecimal](0)(reactivations =>
          reactivations.currentPeriodReactivation)
    }
    if (calculatedReactivations > capacity) {
      TotalReactivationsNotGreaterThanCapacity(calculatedReactivations, capacity).invalidNec
    } else {
      calculatedReactivations.validNec
    }
  }

  private def validateTotalRestrictions: ValidationResult[BigDecimal] = {
    val restrictions: BigDecimal = fullReturnModel.totalRestrictions
    val calculatedRestrictions: BigDecimal = fullReturnModel.ukCompanies.foldLeft[BigDecimal](0) {
      (total, company) =>
        total + company.allocatedRestrictions.fold[BigDecimal](0)(restrictions =>
          restrictions.totalDisallowances)
    }
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
      AggInterestPositiveAndRestriction(aggregateNetTaxInterest, subjectRestrictions).invalidNec
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

  def validate: ValidationResult[FullReturnModel] = {

    val validatedUkCompanies =
      if (fullReturnModel.ukCompanies.isEmpty) UkCompaniesEmpty.invalidNec else {
        combineValidations(fullReturnModel.ukCompanies.zipWithIndex.map {
          case (a, i) => a.validate(fullReturnModel.groupCompanyDetails.accountingPeriod)(JsPath \ s"ukCompanies[$i]")
        }: _*)
      }

    (fullReturnModel.agentDetails.validate(JsPath \ "agentDetails"),
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
      validateReturnContainsEstimates
      ).mapN((_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _) => fullReturnModel)
  }
}

case object ReportingCompanyNotAppointed extends Validation {
  val errorMessage: String = "You need to appoint a reporting company"
  val path: JsPath = JsPath \ "appointedReportingCompany"
  val value: JsValue = Json.obj()
}

case object RevisedReturnDetailsNotSupplied extends Validation {
  val errorMessage: String = "A description of the amendments made to the return must be supplied if this is a revised return"
  val path: JsPath = JsPath \ "revisedReturnDetails"
  val value: JsValue = Json.obj()
}

case class RevisedReturnDetailsSupplied(details: RevisedReturnDetailsModel) extends Validation {
  val errorMessage: String = "A description of the amendments made to the return cannot be supplied if this is an original return"
  val path: JsPath = JsPath \ "revisedReturnDetails"
  val value: JsValue = Json.toJson(details)
}

case object ParentCompanyDetailsNotSupplied extends Validation {
  val errorMessage: String = "Parent Company is required if the Reporting Company is not the same as the Ultimate Parent"
  val path: JsPath = JsPath \ "parentCompany"
  val value: JsValue = Json.obj()
}

case class ParentCompanyDetailsSupplied(parentCompany: ParentCompanyModel) extends Validation {
  val errorMessage: String = "Parent Company should not be supplied as the parent is the same as the Reporting Company"
  val path: JsPath = JsPath \ "parentCompany"
  val value: JsValue = Json.toJson(parentCompany)
}

case class NegativeAngieError(amt: BigDecimal) extends Validation {
  val errorMessage: String = "ANGIE cannot be negative"
  val path: JsPath = JsPath \ "angie"
  val value: JsValue = Json.toJson(amt)
}

case class AngieDecimalError(amt: BigDecimal) extends Validation {
  val errorMessage: String = "ANGIE has greater than the allowed 2 decimal places."
  val path: JsPath = JsPath \ "angie"
  val value: JsValue = Json.toJson(amt)
}

case class GroupLevelInterestRestrictionsAndReactivationSupplied(groupSubjectToInterestRestrictions: Boolean, groupSubjectToInterestReactivation: Boolean)
  extends Validation {
  override val errorMessage: String = "You cannot supply both a group level restriction and reactivation in the same return"
  val path: JsPath = JsPath \ ""
  val value: JsValue = Json.toJson((
    "groupSubjectToInterestRestrictions: " + groupSubjectToInterestRestrictions,
    "groupSubjectToInterestReactivation: " + groupSubjectToInterestReactivation))
}

case class TotalReactivationsNotGreaterThanCapacity(calculated: BigDecimal, capacity: BigDecimal) extends Validation {
  val errorMessage: String = s"Calculated Total Reactivations: $calculated cannot not be greater than Reactivations Capacity: $capacity"
  val path: JsPath = JsPath \ "totalReactivation" \ "interestReactivationCap"
  val value: JsValue = Json.obj()
}

case class TotalRestrictionsDecimalError(amt: BigDecimal) extends Validation {
  val errorMessage: String = s"totalRestrictions has greater than the allowed 2 decimal places."
  val path: JsPath = JsPath \ "totalRestrictions"
  val value: JsValue = Json.toJson(amt)
}

case class TotalRestrictionsDoesNotMatch(amt: BigDecimal, calculated: BigDecimal) extends Validation {
  val errorMessage: String = s"Calculated restrictions is $calculated which does not match the supplied amount of $amt"
  val path: JsPath = JsPath \ "totalRestrictions"
  val value: JsValue = Json.obj()
}

case class AggInterestPositiveAndRestriction(totalTaxInterest: BigDecimal, subjectRestrictions: Boolean) extends Validation {
  val errorMessage: String = s"You cannot have a calculated totalTaxInterest: ${totalTaxInterest.abs} when there the  full return is subjectToRestriction: ${subjectRestrictions.toString}"
  val path: JsPath = JsPath \ "totalRestrictions"
  val value: JsValue = Json.obj()
}

case class CompaniesContainedAllocatedRestrictions(company: UkCompanyModel, i: Int) extends Validation {
  val errorMessage: String = s"Allocated Restrictions cannot be supplied for this UK Company when the group is not subject to Interest Restrictions"
  val path: JsPath = JsPath \ s"ukCompanies[$i]"
  val value: JsValue = Json.obj()
}

case class CompaniesContainedAllocatedReactivations(company: UkCompanyModel, i: Int) extends Validation {
  val errorMessage: String = s"Allocated Reactivations cannot be supplied for this UK Company when the group is not subject to Interest Reactivations"
  val path: JsPath = JsPath \ s"ukCompanies[$i]"
  val value: JsValue = Json.obj()
}

case object UkCompaniesEmpty extends Validation {
  val errorMessage: String = "ukCompanies must have at least 1 UK company"
  val path: JsPath = JsPath \ "ukCompanies"
  val value: JsValue = Json.obj()
}

case object AdjustedNetGroupInterestNotSupplied extends Validation {
  val errorMessage: String = "Adjusted Group Interest is required when Group Ratio is elected"
  val path: JsPath = JsPath \ "adjustedGroupInterest"
  val value: JsValue = Json.obj()
}

case class AdjustedNetGroupInterestSupplied(details: AdjustedGroupInterestModel) extends Validation {
  val errorMessage: String = "Adjusted Group Interest should not be supplied as Group Ratio is not elected"
  val path: JsPath = JsPath \ "adjustedGroupInterest"
  val value: JsValue = Json.toJson(details)
}

case class EstimateReasonSupplied(reason: String) extends Validation {
  val errorMessage: String = "groupEstimateReason cannot be supplied when returnContainsEstimates is set to false"
  val path: JsPath = JsPath \ "groupEstimateReason"
  val value: JsValue = Json.toJson(reason)
}

case class EstimateReasonLengthError(reason: String) extends Validation {
  val errorMessage: String = s"groupEstimateReason is ${reason.length} characters long and should be between 1 and 10,000 characters"
  val path: JsPath = JsPath \ "groupEstimateReason"
  val value: JsValue = Json.toJson(reason)
}

case class EstimateReasonCharacterError(reason: String) extends Validation {
  val errorMessage: String = "groupEstimateReason contains invalid characters"
  val path: JsPath = JsPath \ "groupEstimateReason"
  val value: JsValue = Json.toJson(reason)
}

case class CompaniesContainedEstimateReason(reason: String, i: Int) extends Validation {
  val errorMessage: String = s"companyEstimateReason cannot be supplied for this UK Company when returnContainsEstimates is set to false"
  val path: JsPath = JsPath \ s"ukCompanies[$i]"
  val value: JsValue = Json.toJson(reason)
}

case class NoEstimatesSupplied(bool: Boolean) extends Validation {
  val errorMessage: String = s"An estimate needs to be supplied in groupEstimateReason or companyEstimateReason where returnContainsEstimates is set to true"
  val path: JsPath = JsPath \ "returnContainsEstimates"
  val value: JsValue = Json.toJson(bool)
}

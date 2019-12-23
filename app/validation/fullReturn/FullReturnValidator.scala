/*
 * Copyright 2019 HM Revenue & Customs
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

package validation.fullReturn

import models.Validation.ValidationResult
import models.fullReturn.{FullReturnModel, UkCompanyModel}
import models.{Original, ParentCompanyModel, Revised, Validation}
import play.api.libs.json.{JsPath, Json}
import validation.BaseValidation

trait FullReturnValidator extends BaseValidation {

  import cats.implicits._

  val fullReturnModel: FullReturnModel

  private def validateRevisedReturnDetails: ValidationResult[Option[String]] = {
    (fullReturnModel.submissionType, fullReturnModel.revisedReturnDetails) match {
      case (Original, Some(details)) => RevisedReturnDetailsSupplied(details).invalidNec
      case (Revised, None) => RevisedReturnDetailsNotSupplied.invalidNec
      case _ => fullReturnModel.revisedReturnDetails.validNec
    }
  }

  private def validateParentCompany: ValidationResult[Option[String]] = {
    (fullReturnModel.reportingCompany.sameAsUltimateParent, fullReturnModel.parentCompany) match {
      case (true, Some(details)) => ParentCompanyDetailsSupplied(details).invalidNec
      case (false, None) => ParentCompanyDetailsNotSupplied.invalidNec
      case _ => fullReturnModel.revisedReturnDetails.validNec
    }
  }

  private def validateAngie: ValidationResult[BigDecimal] = {
    val angie: BigDecimal = fullReturnModel.angie.getOrElse(0)
    if (angie >= 0) angie.validNec else NegativeAngieError(angie).invalidNec
  }

  private def validateInterestReactivationCap: ValidationResult[Option[BigDecimal]] = {
    (fullReturnModel.groupSubjectToInterestReactivation, fullReturnModel.groupLevelAmount.interestReactivationCap) match {
      case (true, None) => InterestReactivationCapNotSupplied.invalidNec
      case _ => fullReturnModel.groupLevelAmount.interestReactivationCap.validNec
    }
  }

  private def validateAllocatedRestrictions: ValidationResult[Boolean] = {
    (fullReturnModel.groupSubjectToInterestRestrictions, fullReturnModel.ukCompanies.zipWithIndex) match {
      case (true, x) if x.exists(_._1.allocatedRestrictions.isEmpty) => combineValidations(x.map {
        case (company, i) => MissingAllocatedRestrictionsForCompanies(company, i).invalidNec
      }:_*)
      case (false, x) if x.exists(_._1.allocatedRestrictions.nonEmpty) => combineValidations(x.map {
        case (company, i) => CompaniesContainedAllocatedRestrictions(company, i).invalidNec
      }:_*)
      case _ => fullReturnModel.groupSubjectToInterestRestrictions.validNec
    }
  }

  private def validateAllocatedReactivations: ValidationResult[Boolean] = {
    (fullReturnModel.groupSubjectToInterestReactivation, fullReturnModel.ukCompanies.zipWithIndex) match {
      case (true, x) if x.exists(_._1.allocatedReactivations.isEmpty) => combineValidations(x.map {
        case (company, i) => MissingAllocatedReactivationsForCompanies(company, i).invalidNec
      }:_*)
      case (false, x) if x.exists(_._1.allocatedReactivations.nonEmpty) => combineValidations(x.map {
        case (company, i) => CompaniesContainedAllocatedReactivations(company, i).invalidNec
      }:_*)
      case _ => fullReturnModel.groupSubjectToInterestReactivation.validNec
    }
  }

  private def validateTotalReactivations: ValidationResult[BigDecimal] = {
    val reactivations: BigDecimal = fullReturnModel.totalReactivation
    val calculatedReactivations: BigDecimal = fullReturnModel.ukCompanies.foldLeft[BigDecimal](0) {
      (total, company) =>
        total + company.allocatedRestrictions.fold[BigDecimal](0)(restrictions =>
          restrictions.totalDisallowances.getOrElse[BigDecimal](0))
    }
    if(reactivations == calculatedReactivations) reactivations.validNec else {
      TotalReactivationsDoesNotMatch(reactivations, calculatedReactivations).invalidNec
    }
  }

  def validate: ValidationResult[FullReturnModel] = {

    val validatedUkCompanies = fullReturnModel.ukCompanies.zipWithIndex.map {
      case (a, i) => a.validate(JsPath \ s"ukCompanies[$i]")
    }

    (
      fullReturnModel.agentDetails.validate(JsPath \ "agentDetails"),
      fullReturnModel.reportingCompany.validate(JsPath \ "reportingCompany"),
      optionValidations(fullReturnModel.parentCompany.map(_.validate(JsPath \ "parentCompany"))),
      fullReturnModel.groupCompanyDetails.validate(JsPath \ "groupCompanyDetails"),
      optionValidations(fullReturnModel.groupLevelElections.map(_.validate(JsPath \ "groupLevelElections"))),
      combineValidations(validatedUkCompanies:_*),
      validateAngie,
      validateInterestReactivationCap,
      validateAllocatedRestrictions,
      validateAllocatedReactivations,
      validateTotalReactivations,
      validateParentCompany,
      validateRevisedReturnDetails,
      fullReturnModel.groupLevelAmount.validate(JsPath \ "groupLevelAmount"),
      fullReturnModel.adjustedGroupInterest.validate(JsPath \ "adjustedGroupInterest")
      ).mapN((_,_,_,_,_,_,_,_,_,_,_,_,_,_,_) => fullReturnModel)
  }
}

case object RevisedReturnDetailsNotSupplied extends Validation {
  val errorMessage: String = "A description of the amendments made to the return must be supplied if this is a revised return"
  val path = JsPath \ "revisedReturnDetails"
  val value = Json.obj()
}

case class RevisedReturnDetailsSupplied(details: String) extends Validation {
  val errorMessage: String = "A description of the amendments made to the return cannot be supplied if this is an original return"
  val path = JsPath \ "revisedReturnDetails"
  val value = Json.toJson(details)
}

case object ParentCompanyDetailsNotSupplied extends Validation {
  val errorMessage: String = "Parent Company is required if the Reporting Company is not the same as the Ultimate Parent"
  val path = JsPath \ "parentCompany"
  val value = Json.obj()
}

case class ParentCompanyDetailsSupplied(parentCompany: ParentCompanyModel) extends Validation {
  val errorMessage: String = "Parent Company should not be supplied as the parent is the same as the Reporting Company"
  val path = JsPath \ "parentCompany"
  val value = Json.toJson(parentCompany)
}

case class NegativeAngieError(amt: BigDecimal) extends Validation {
  val errorMessage: String = "ANGIE cannot be negative"
  val path = JsPath \ "angie"
  val value = Json.toJson(amt)
}

case object InterestReactivationCapNotSupplied extends Validation {
  val errorMessage: String = "Interest Reactivation Cap is required if the group is subject to interest reactivation"
  val path = JsPath \ "groupLevelAmount" \ "interestReactivationCap"
  val value = Json.obj()
}

case class TotalReactivationsDoesNotMatch(amt: BigDecimal, calculated: BigDecimal) extends Validation {
  val errorMessage: String = s"Calculated reactivations is $calculated which does not match the supplied amount of $amt"
  val path = JsPath \ "totalReactivation"
  val value = Json.obj()
}

case class MissingAllocatedRestrictionsForCompanies(company: UkCompanyModel, i: Int) extends Validation {
  val errorMessage: String = s"Allocated Restrictions must be supplied for this UK Company when the group is subject to Interest Restrictions"
  val path = JsPath \ s"ukCompanies[$i]"
  val value = Json.obj()
}

case class CompaniesContainedAllocatedRestrictions(company: UkCompanyModel, i: Int) extends Validation {
  val errorMessage: String = s"Allocated Restrictions cannot be supplied for this UK Company when the group is not subject to Interest Restrictions"
  val path = JsPath \ s"ukCompanies[$i]"
  val value = Json.obj()
}

case class MissingAllocatedReactivationsForCompanies(company: UkCompanyModel, i: Int) extends Validation {
  val errorMessage: String = s"Allocated Reactivations must be supplied for this UK Company when the group is subject to Interest Reactivations"
  val path = JsPath \ s"ukCompanies[$i]"
  val value = Json.obj()
}

case class CompaniesContainedAllocatedReactivations(company: UkCompanyModel, i: Int) extends Validation {
  val errorMessage: String = s"Allocated Reactivations cannot be supplied for this UK Company when the group is not subject to Interest Reactivations"
  val path = JsPath \ s"ukCompanies[$i]"
  val value = Json.obj()
}





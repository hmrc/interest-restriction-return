/*
 * Copyright 2020 HM Revenue & Customs
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

package v1.validation.abbreviatedReturn

import play.api.libs.json.{JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.abbreviatedReturn.AbbreviatedReturnModel
import v1.models.{Original, ParentCompanyModel, Revised, Validation}
import v1.validation._

trait AbbreviatedReturnValidator extends BaseValidation {

  import cats.implicits._

  val abbreviatedReturnModel: AbbreviatedReturnModel

  private def validateRevisedReturnDetails: ValidationResult[Option[String]] = {
    (abbreviatedReturnModel.submissionType, abbreviatedReturnModel.revisedReturnDetails) match {
      case (Original, Some(details)) => RevisedReturnDetailsSupplied(details).invalidNec
      case (Revised, None) => RevisedReturnDetailsNotSupplied.invalidNec
      case _ => abbreviatedReturnModel.revisedReturnDetails.validNec
    }
  }

  private def validateParentCompany: ValidationResult[Boolean] = {
    (abbreviatedReturnModel.reportingCompany.sameAsUltimateParent, abbreviatedReturnModel.parentCompany) match {
      case (true, Some(details)) => ParentCompanyDetailsSupplied(details).invalidNec
      case (false, None) => ParentCompanyDetailsNotSupplied.invalidNec
      case _ => abbreviatedReturnModel.appointedReportingCompany.validNec
    }
  }

  private def validateAngie: ValidationResult[BigDecimal] = {
    val angie: BigDecimal = abbreviatedReturnModel.angie.getOrElse(0)
    if (angie >= 0) angie.validNec else NegativeAngieError(angie).invalidNec
  }

  private def validateAppointedReporter: ValidationResult[Boolean] = {
    if(abbreviatedReturnModel.appointedReportingCompany) abbreviatedReturnModel.appointedReportingCompany.validNec else {
      ReportingCompanyNotAppointed.invalidNec
    }
  }

  def validate: ValidationResult[AbbreviatedReturnModel] = {

    val validatedUkCompanies =
      if(abbreviatedReturnModel.ukCompanies.isEmpty) UkCompaniesEmpty.invalidNec else {
        combineValidations(abbreviatedReturnModel.ukCompanies.zipWithIndex.map {
          case (a, i) => a.validate(JsPath \ s"ukCompanies[$i]")
        }:_*)
      }

    (abbreviatedReturnModel.agentDetails.validate(JsPath \ "agentDetails"),
      abbreviatedReturnModel.reportingCompany.validate(JsPath \ "reportingCompany"),
      optionValidations(abbreviatedReturnModel.parentCompany.map(_.validate(JsPath \ "parentCompany"))),
      abbreviatedReturnModel.groupCompanyDetails.validate(JsPath \ "groupCompanyDetails"),
      optionValidations(abbreviatedReturnModel.groupLevelElections.map(_.validate(JsPath \ "groupLevelElections"))),
      validatedUkCompanies,
      validateParentCompany,
      validateRevisedReturnDetails,
      validateAngie,
      validateAppointedReporter
      ).mapN((_,_,_,_,_,_,_,_,_,_) => abbreviatedReturnModel)
  }
}

case object ReportingCompanyNotAppointed extends Validation {
  val code = APPOINT_REPORTING_COMPANY
  val message: String = "You need to appoint a reporting company"
  val path = JsPath \ "appointedReportingCompany"
}

case object RevisedReturnDetailsNotSupplied extends Validation {
  val code = MISSING_FIELD
  val message: String = "A description of the amendments made to the return must be supplied if this is a revised return"
  val path = JsPath \ "revisedReturnDetails"
}

case class RevisedReturnDetailsSupplied(details: String) extends Validation {
  val code = UNEXPECTED_FIELD
  val message: String = "A description of the amendments made to the return cannot be supplied if this is an original return"
  val path = JsPath \ "revisedReturnDetails"
}

case object ParentCompanyDetailsNotSupplied extends Validation {
  val code = MISSING_FIELD
  val message: String = "Parent Company is required if the Reporting Company is not the same as the Ultimate Parent"
  val path = JsPath \ "parentCompany"
}

case class ParentCompanyDetailsSupplied(parentCompany: ParentCompanyModel) extends Validation {
  val code = UNEXPECTED_FIELD
  val message: String = "Parent Company should not be supplied as the parent is the same as the Reporting Company"
  val path = JsPath \ "parentCompany"
}

case object UkCompaniesEmpty extends Validation {
  val code = MISSING_FIELD
  val message: String = "ukCompanies must have at least 1 UK company"
  val path = JsPath \ "ukCompanies"
}

case class NegativeAngieError(amt: BigDecimal) extends Validation {
  val code = NEGATIVE_AMOUNT
  val message: String = "ANGIE cannot be negative"
  val path = JsPath \ "angie"
}

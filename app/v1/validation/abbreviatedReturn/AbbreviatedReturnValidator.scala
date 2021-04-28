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

package v1.validation.abbreviatedReturn

import play.api.libs.json.{Json, JsPath, JsValue}
import v1.models.Validation.ValidationResult
import v1.models.abbreviatedReturn.AbbreviatedReturnModel
import v1.models.{Original, ParentCompanyModel, Revised, Validation}
import v1.validation.BaseValidation

trait AbbreviatedReturnValidator extends BaseValidation {

  import cats.implicits._

  val abbreviatedReturnModel: AbbreviatedReturnModel

  private def validateRevisedReturnDetails: ValidationResult[_] = {
    (abbreviatedReturnModel.submissionType, abbreviatedReturnModel.revisedReturnDetails) match {
      case (Original, Some(details)) => RevisedReturnDetailsSupplied(details.details).invalidNec
      case (Revised, None) => RevisedReturnDetailsNotSupplied.invalidNec
      case (Revised, Some(details)) => details.validate(JsPath)
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

  private def validateAppointedReporter: ValidationResult[Boolean] = {
    if(abbreviatedReturnModel.appointedReportingCompany) abbreviatedReturnModel.appointedReportingCompany.validNec else {
      ReportingCompanyNotAppointed.invalidNec
    }
  }

  private def validateDeclaration: ValidationResult[Boolean] =
    abbreviatedReturnModel.declaration match {
      case true => abbreviatedReturnModel.declaration.validNec
      case false => AbbreviatedReturnDeclarationError(abbreviatedReturnModel.declaration).invalidNec
    }

  def validate: ValidationResult[AbbreviatedReturnModel] = {

    val validatedUkCompanies =
      if(abbreviatedReturnModel.ukCompanies.isEmpty) UkCompaniesEmpty.invalidNec else {
        combineValidations(abbreviatedReturnModel.ukCompanies.zipWithIndex.map {
          case (a, i) => a.validate(JsPath \ s"ukCompanies[$i]")
        }:_*)
      }

    combineValidations(abbreviatedReturnModel.agentDetails.validate(JsPath \ "agentDetails"),
      abbreviatedReturnModel.reportingCompany.validate(JsPath \ "reportingCompany"),
      optionValidations(abbreviatedReturnModel.parentCompany.map(_.validate(JsPath \ "parentCompany"))),
      abbreviatedReturnModel.groupCompanyDetails.validate(JsPath \ "groupCompanyDetails"),
      optionValidations(abbreviatedReturnModel.groupLevelElections.map(_.validate(JsPath \ "groupLevelElections"))),
      validatedUkCompanies,
      validateParentCompany,
      validateRevisedReturnDetails,
      validateAppointedReporter,
      validateDeclaration
      ).map(_ => abbreviatedReturnModel)
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

case class RevisedReturnDetailsSupplied(details: String) extends Validation {
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

case object UkCompaniesEmpty extends Validation {
  val errorMessage: String = "ukCompanies must have at least 1 UK company"
  val path: JsPath = JsPath \ "ukCompanies"
  val value: JsValue = Json.obj()
}

case class AbbreviatedReturnDeclarationError(declaration: Boolean) extends Validation {
  val errorMessage: String = "The declaration must be true"
  val path: JsPath = JsPath \ "declaration"
  val value: JsValue = Json.toJson(declaration)
}

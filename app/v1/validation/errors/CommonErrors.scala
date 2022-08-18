/*
 * Copyright 2022 HM Revenue & Customs
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

package v1.validation.errors

import play.api.libs.json.{JsPath, JsValue, Json}
import v1.models.{ParentCompanyModel, RevisedReturnDetailsModel, UltimateParentModel}
import v1.models.Validation

case object ReportingCompanyNotAppointed extends Validation {
  val code                   = "REPORTING_COMPANY_NOT_APPOINTED"
  val errorMessage: String   = "Reporting company required"
  val path: JsPath           = JsPath \ "appointedReportingCompany"
  val value: Option[JsValue] = None
}

case object RevisedReturnDetailsNotSupplied extends Validation {
  val code                   = "REVISION_DETAILS_NOT_SUPPLIED"
  val errorMessage: String   = "Return is a revised return, describe the changes made to the original return"
  val path: JsPath           = JsPath \ "revisedReturnDetails"
  val value: Option[JsValue] = None
}

case class RevisedReturnDetailsSupplied(details: RevisedReturnDetailsModel) extends Validation {
  val code                   = "REVISION_DETAILS_SUPPLIED"
  val errorMessage: String   = "Return is not a revised return so changes to original return not needed"
  val path: JsPath           = JsPath \ "revisedReturnDetails"
  val value: Option[JsValue] = Some(Json.toJson(details))
}

case object ParentCompanyDetailsNotSupplied extends Validation {
  val code                   = "PARENT_COMPANY_NOT_SUPPLIED"
  val errorMessage: String   =
    "Reporting company is not the ultimate parent, so details of ultimate parent or deemed parent needed"
  val path: JsPath           = JsPath \ "parentCompany"
  val value: Option[JsValue] = None
}

case class ParentCompanyDetailsSupplied(parentCompany: ParentCompanyModel) extends Validation {
  val code                   = "PARENT_COMPANY_SUPPLIED"
  val errorMessage: String   = "Reporting company is the ultimate parent so details of parent company not needed"
  val path: JsPath           = JsPath \ "parentCompany"
  val value: Option[JsValue] = Some(Json.toJson(parentCompany))
}

case object UkCompaniesEmpty extends Validation {
  val code                   = "COMPANIES_EMPTY"
  val errorMessage: String   = "You must provide details of all eligible UK companies"
  val path: JsPath           = JsPath \ "ukCompanies"
  val value: Option[JsValue] = None
}

case class UltimateParentCompanyIsSupplied(ultimateParentModel: UltimateParentModel) extends Validation {
  val code                   = "ULTIMATE_PARENT_SUPPLIED"
  val errorMessage: String   = "Ultimate parent company not needed as it is the same as the reporting company"
  val path: JsPath           = JsPath \ "ultimateParentCompany"
  val value: Option[JsValue] = Some(Json.toJson(ultimateParentModel))
}

case object UltimateParentCompanyIsNotSupplied extends Validation {
  val code                   = "ULTIMATE_PARENT_NOT_SUPPLIED"
  val errorMessage: String   = "Ultimate parent company must be entered if it is not the same as the reporting company"
  val path: JsPath           = JsPath \ "ultimateParentCompany"
  val value: Option[JsValue] = None
}

case object AuthorisingCompaniesEmpty extends Validation {
  val code                   = "AUTHORISING_COMPANIES_EMPTY"
  val errorMessage: String   = "Enter at least 1 authorising company"
  val path: JsPath           = JsPath \ "authorisingCompanies"
  val value: Option[JsValue] = None
}

case object AuthorisingCompaniesContainsDuplicates extends Validation {
  val code                   = "AUTHORISING_COMPANIES_DUPLICATES"
  val errorMessage: String   = "Authorising companies contain duplicate information"
  val path: JsPath           = JsPath \ "authorisingCompanies"
  val value: Option[JsValue] = None
}

case class ReturnDeclarationError(declaration: Boolean) extends Validation {
  val code                   = "RETURN_DECLARATION_FALSE"
  val errorMessage: String   = "Declaration is not valid so will not be submitted. " +
    "You need to confirm that the return is correct and complete to the best of your knowledge"
  val path: JsPath           = JsPath \ "declaration"
  val value: Option[JsValue] = Some(Json.toJson(declaration))
}

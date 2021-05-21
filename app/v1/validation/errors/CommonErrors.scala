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

package v1.validation.errors

import play.api.libs.json.{Json, JsPath, JsValue}
import v1.models.{ParentCompanyModel, RevisedReturnDetailsModel, UltimateParentModel}
import v1.models.Validation

case object ReportingCompanyNotAppointed extends Validation {
  val code = "REPORTING_COMPANY_NOT_APPOINTED"
  val errorMessage: String = "You need to appoint a reporting company"
  val path: JsPath = JsPath \ "appointedReportingCompany"
  val value: Option[JsValue] = None
}

case object RevisedReturnDetailsNotSupplied extends Validation {
  val code = "REVISION_DETAILS_NOT_SUPPLIED"
  val errorMessage: String = "A description of the amendments made to the return must be supplied if this is a revised return"
  val path: JsPath = JsPath \ "revisedReturnDetails"
  val value: Option[JsValue] = None
}

case class RevisedReturnDetailsSupplied(details: RevisedReturnDetailsModel) extends Validation {
  val code = "REVISION_DETAILS_SUPPLIED"
  val errorMessage: String = "A description of the amendments made to the return cannot be supplied if this is an original return"
  val path: JsPath = JsPath \ "revisedReturnDetails"
  val value: Option[JsValue] = Some(Json.toJson(details))
}

case object ParentCompanyDetailsNotSupplied extends Validation {
  val code = "PARENT_COMPANY_NOT_SUPPLIED"
  val errorMessage: String = "Parent Company is required if the Reporting Company is not the same as the Ultimate Parent"
  val path: JsPath = JsPath \ "parentCompany"
  val value: Option[JsValue] = None
}

case class ParentCompanyDetailsSupplied(parentCompany: ParentCompanyModel) extends Validation {
  val code = "PARENT_COMPANY_SUPPLIED"
  val errorMessage: String = "Parent Company should not be supplied as the parent is the same as the Reporting Company"
  val path: JsPath = JsPath \ "parentCompany"
  val value: Option[JsValue] = Some(Json.toJson(parentCompany))
}

case object UkCompaniesEmpty extends Validation {
  val code = "COMPANIES_EMPTY"
  val errorMessage: String = "ukCompanies must have at least 1 UK company"
  val path: JsPath = JsPath \ "ukCompanies"
  val value: Option[JsValue] = None
}


case class UltimateParentCompanyIsSupplied(ultimateParentModel: UltimateParentModel) extends Validation {
  val code = "ULTIMATE_PARENT_SUPPLIED"
  val errorMessage: String = "Ultimate Parent Company must not be supplied if it is the same as the reporting company"
  val path: JsPath = JsPath \ "ultimateParentCompany"
  val value = Some(Json.toJson(ultimateParentModel))
}

case object UltimateParentCompanyIsNotSupplied extends Validation {
  val code = "ULTIMATE_PARENT_NOT_SUPPLIED"
  val errorMessage: String = "Ultimate Parent Company must be supplied if it is not the same as the reporting company"
  val path: JsPath = JsPath \ "ultimateParentCompany"
  val value = None
}

case object AuthorisingCompaniesEmpty extends Validation {
  val code = "AUTHORISING_COMPANIES_EMPTY"
  val errorMessage: String = "authorisingCompanies must have at least 1 authorising company"
  val path: JsPath = JsPath \ "authorisingCompanies"
  val value = None
}

case object AuthorisingCompaniesContainsDuplicates extends Validation {
  val code = "AUTHORISING_COMPANIES_DUPLICATES"
  val errorMessage: String = "Authorising companies contain duplicate information"
  val path = JsPath \ "authorisingCompanies"
  val value = None
}
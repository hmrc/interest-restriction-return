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

package v1.validation.appointReportingCompany

import play.api.libs.json.{JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.appointReportingCompany.AppointReportingCompanyModel
import v1.models.{IdentityOfCompanySubmittingModel, UltimateParentModel, Validation}
import v1.validation.BaseValidation

trait AppointReportingCompanyValidator extends BaseValidation {

  import cats.implicits._

  val appointReportingCompanyModel: AppointReportingCompanyModel

  private def validateIdentityOfAppointingCompany: ValidationResult[Option[IdentityOfCompanySubmittingModel]] = {
    (appointReportingCompanyModel.isReportingCompanyAppointingItself, appointReportingCompanyModel.identityOfAppointingCompany) match {
      case (true, Some(appointingCompany)) => IdentityOfAppointingCompanyIsSupplied(appointingCompany).invalidNec
      case (false, None) => IdentityOfAppointingCompanyIsNotSupplied.invalidNec
      case _ => appointReportingCompanyModel.identityOfAppointingCompany.validNec
    }
  }

  private def validateUltimateParentCompany: ValidationResult[Option[UltimateParentModel]] = {
    (appointReportingCompanyModel.reportingCompany.sameAsUltimateParent, appointReportingCompanyModel.ultimateParentCompany) match {
      case (true, Some(parent)) => UltimateParentCompanyIsSupplied(parent).invalidNec
      case (false, None) => UltimateParentCompanyIsNotSupplied.invalidNec
      case _ => appointReportingCompanyModel.ultimateParentCompany.validNec
    }
  }

  def validate: ValidationResult[AppointReportingCompanyModel] = {

    val validatedAuthorisingCompanies =
      if(appointReportingCompanyModel.authorisingCompanies.isEmpty) AuthorisingCompaniesEmpty.invalidNec else {
        combineValidations(appointReportingCompanyModel.authorisingCompanies.zipWithIndex.map {
          case (a, i) => a.validate(JsPath \ s"authorisingCompanies[$i]")
        }:_*)
      }

    (appointReportingCompanyModel.agentDetails.validate(JsPath \ "agentDetails"),
      appointReportingCompanyModel.reportingCompany.validate(JsPath \ "reportingCompany"),
      validatedAuthorisingCompanies,
      optionValidations(appointReportingCompanyModel.ultimateParentCompany.map(_.validate(JsPath \ "ultimateParentCompany"))),
      optionValidations(appointReportingCompanyModel.identityOfAppointingCompany.map(_.validate(JsPath \ "identityOfAppointingCompany"))),
      appointReportingCompanyModel.accountingPeriod.validate(JsPath \ "accountingPeriod"),
      validateIdentityOfAppointingCompany,
      validateUltimateParentCompany
      ).mapN((_,_,_,_,_,_,_,_) => appointReportingCompanyModel)
  }
}

case object IdentityOfAppointingCompanyIsNotSupplied extends Validation {
  val errorMessage: String = "Identity of Appointing Company must be supplied if it is not the same as the reporting company or agent"
  val path = JsPath \ "identifyOfAppointingCompany"
  val value = Json.obj()
}

case class IdentityOfAppointingCompanyIsSupplied(identityOfCompanySubmittingModel: IdentityOfCompanySubmittingModel) extends Validation {
  val errorMessage: String = "Identity of Appointing Company must not be supplied if it is the same as the reporting company or agent"
  val path = JsPath \ "identifyOfAppointingCompany"
  val value = Json.toJson(identityOfCompanySubmittingModel)
}

case class UltimateParentCompanyIsSupplied(ultimateParentModel: UltimateParentModel) extends Validation {
  val errorMessage: String = "Ultimate Parent Company must not be supplied if it is the same as the reporting company"
  val path = JsPath \ "ultimateParentCompany"
  val value = Json.toJson(ultimateParentModel)
}

case object UltimateParentCompanyIsNotSupplied extends Validation {
  val errorMessage: String = "Ultimate Parent Company must be supplied if it is not the same as the reporting company"
  val path = JsPath \ "ultimateParentCompany"
  val value = Json.obj()
}

case object AuthorisingCompaniesEmpty extends Validation {
  val errorMessage: String = "authorisingCompanies must have at least 1 authorising company"
  val path = JsPath \ "authorisingCompanies"
  val value = Json.obj()
}









/*
 * Copyright 2023 HM Revenue & Customs
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

import play.api.libs.json.{JsPath, JsValue, Json}
import v1.models.Validation.ValidationResult
import v1.models.appointReportingCompany.AppointReportingCompanyModel
import v1.models.{IdentityOfCompanySubmittingModel, UltimateParentModel, Validation}
import v1.models.AuthorisingCompanyModel
import v1.validation.BaseValidation
import v1.validation.errors._

trait AppointReportingCompanyValidator extends BaseValidation {

  import cats.implicits._

  val appointReportingCompanyModel: AppointReportingCompanyModel

  private def validateIdentityOfAppointingCompany: ValidationResult[Option[IdentityOfCompanySubmittingModel]] =
    (
      appointReportingCompanyModel.isReportingCompanyAppointingItself,
      appointReportingCompanyModel.identityOfAppointingCompany
    ) match {
      case (true, Some(appointingCompany)) => IdentityOfAppointingCompanyIsSupplied(appointingCompany).invalidNec
      case (false, None)                   => IdentityOfAppointingCompanyIsNotSupplied.invalidNec
      case _                               => appointReportingCompanyModel.identityOfAppointingCompany.validNec
    }

  private def validateUltimateParentCompany: ValidationResult[Option[UltimateParentModel]] =
    (
      appointReportingCompanyModel.reportingCompany.sameAsUltimateParent,
      appointReportingCompanyModel.ultimateParentCompany
    ) match {
      case (true, Some(parent)) => UltimateParentCompanyIsSupplied(parent).invalidNec
      case (false, None)        => UltimateParentCompanyIsNotSupplied.invalidNec
      case _                    => appointReportingCompanyModel.ultimateParentCompany.validNec
    }

  private def validateDuplicateAuthorisingCompanies: ValidationResult[Seq[AuthorisingCompanyModel]] = {
    val duplicatesExist =
      appointReportingCompanyModel.authorisingCompanies.distinct.size != appointReportingCompanyModel.authorisingCompanies.size
    if (duplicatesExist) {
      AuthorisingCompaniesContainsDuplicates.invalidNec
    } else {
      appointReportingCompanyModel.authorisingCompanies.validNec
    }
  }

  private def validateDeclaration: ValidationResult[Boolean] = {
    val declaration = appointReportingCompanyModel.declaration
    if (declaration) {
      declaration.validNec
    } else {
      DeclaredFiftyPercentOfEligibleCompanies(declaration).invalidNec
    }
  }

  def validate: ValidationResult[AppointReportingCompanyModel] = {

    val validatedAuthorisingCompanies =
      if (appointReportingCompanyModel.authorisingCompanies.isEmpty) {
        AuthorisingCompaniesEmpty.invalidNec
      } else {
        combineValidations(appointReportingCompanyModel.authorisingCompanies.zipWithIndex.map { case (a, i) =>
          a.validate(JsPath \ s"authorisingCompanies[$i]")
        }: _*)
      }

    combineValidations(
      appointReportingCompanyModel.agentDetails.validate(JsPath \ "agentDetails"),
      appointReportingCompanyModel.reportingCompany.validate(JsPath \ "reportingCompany"),
      validatedAuthorisingCompanies,
      optionValidations(
        appointReportingCompanyModel.ultimateParentCompany.map(_.validate(JsPath \ "ultimateParentCompany"))
      ),
      optionValidations(
        appointReportingCompanyModel.identityOfAppointingCompany.map(_.validate(JsPath \ "identityOfAppointingCompany"))
      ),
      appointReportingCompanyModel.accountingPeriod.validate(JsPath \ "accountingPeriod"),
      validateIdentityOfAppointingCompany,
      validateUltimateParentCompany,
      validateDuplicateAuthorisingCompanies,
      validateDeclaration
    ).map(_ => appointReportingCompanyModel)
  }
}

case object IdentityOfAppointingCompanyIsNotSupplied extends Validation {
  val code                   = "IDENTITY_APPOINTING_COMPANY_NOT_SUPPLIED"
  val errorMessage: String   = "Appointing company must be supplied if it's not the same as the reporting company"
  val path: JsPath           = JsPath \ "identifyOfAppointingCompany"
  val value: Option[JsValue] = None
}

case class IdentityOfAppointingCompanyIsSupplied(identityOfCompanySubmittingModel: IdentityOfCompanySubmittingModel)
    extends Validation {
  val code                   = "IDENTITY_APPOINTING_COMPANY_SUPPLIED"
  val errorMessage: String   = "Appointing company not needed as it is the same as the reporting company"
  val path: JsPath           = JsPath \ "identifyOfAppointingCompany"
  val value: Option[JsValue] = Some(Json.toJson(identityOfCompanySubmittingModel))
}

case class DeclaredFiftyPercentOfEligibleCompanies(declaration: Boolean) extends Validation {
  val code                   = "DECLARATION_FALSE"
  val errorMessage: String   = "Declaration is not valid so will not be submitted. " +
    "You need to confirm the listed companies constitute at least 50% of the eligible companies."
  val path: JsPath           = JsPath \ "declaration"
  val value: Option[JsValue] = Some(Json.toJson(declaration))
}

/*
 * Copyright 2024 HM Revenue & Customs
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

package v1.validation.revokeReportingCompany

import play.api.libs.json.{JsBoolean, JsPath, JsValue, Json}
import v1.models.Validation.ValidationResult
import v1.models.revokeReportingCompany.RevokeReportingCompanyModel
import v1.models.{IdentityOfCompanySubmittingModel, UltimateParentModel, Validation}
import v1.validation.BaseValidation
import v1.models.AuthorisingCompanyModel
import v1.validation.errors._

trait RevokeReportingCompanyValidator extends BaseValidation {

  import cats.implicits._

  val revokeReportingCompanyModel: RevokeReportingCompanyModel

  private def validateReportingCompanyRevokeItself(implicit path: JsPath): ValidationResult[Boolean] = {
    val revokeItself = revokeReportingCompanyModel.isReportingCompanyRevokingItself
    val company      = revokeReportingCompanyModel.companyMakingRevocation
    (revokeItself, company) match {
      case (true, Some(details)) => DetailsNotNeededIfCompanyRevokingItself(details).invalidNec
      case (false, None)         => CompanyMakingAppointmentMustSupplyDetails.invalidNec
      case _                     => revokeItself.validNec
    }
  }

  private def validateDeclaration: ValidationResult[Boolean] = {
    val declaration = revokeReportingCompanyModel.declaration
    if (declaration) {
      declaration.validNec
    } else {
      DeclaredFiftyPercentOfEligibleCompanies(declaration).invalidNec
    }
  }

  private def validateUltimateParentCompany: ValidationResult[Option[UltimateParentModel]] =
    (
      revokeReportingCompanyModel.reportingCompany.sameAsUltimateParent,
      revokeReportingCompanyModel.ultimateParentCompany
    ) match {
      case (true, Some(parent)) => UltimateParentCompanyIsSupplied(parent).invalidNec
      case (false, None)        => UltimateParentCompanyIsNotSupplied.invalidNec
      case _                    => revokeReportingCompanyModel.ultimateParentCompany.validNec
    }

  private def validateDuplicateAuthorisingCompanies: ValidationResult[Seq[AuthorisingCompanyModel]] = {
    val duplicatesExist =
      revokeReportingCompanyModel.authorisingCompanies.distinct.size != revokeReportingCompanyModel.authorisingCompanies.size
    if (duplicatesExist) {
      AuthorisingCompaniesContainsDuplicates.invalidNec
    } else {
      revokeReportingCompanyModel.authorisingCompanies.validNec
    }
  }

  def validate: ValidationResult[RevokeReportingCompanyModel] = {

    val validatedAuthorisingCompanies =
      if (revokeReportingCompanyModel.authorisingCompanies.isEmpty) {
        AuthorisingCompaniesEmpty.invalidNec
      } else {
        combineValidations(revokeReportingCompanyModel.authorisingCompanies.zipWithIndex.map { case (a, i) =>
          a.validate(JsPath \ s"authorisingCompanies[$i]")
        }: _*)
      }

    combineValidations[Any](
      revokeReportingCompanyModel.agentDetails.validate(JsPath \ "agentDetails"),
      revokeReportingCompanyModel.reportingCompany.validate(JsPath \ "reportingCompany"),
      validateReportingCompanyRevokeItself(JsPath \ "isReportingCompanyRevokingItself"),
      optionValidations(
        revokeReportingCompanyModel.companyMakingRevocation.map(_.validate(JsPath \ "companyMakingRevocation"))
      ),
      validateUltimateParentCompany,
      optionValidations(
        revokeReportingCompanyModel.ultimateParentCompany.map(_.validate(JsPath \ "ultimateParentCompany"))
      ),
      revokeReportingCompanyModel.accountingPeriod.validate(JsPath \ "accountingPeriod"),
      validatedAuthorisingCompanies,
      validateDeclaration,
      validateDuplicateAuthorisingCompanies
    ).map(_ => revokeReportingCompanyModel)
  }
}

case object CompanyMakingAppointmentMustSupplyDetails extends Validation {
  val code: String           = "COMPANY_MAKING_REVOCATION_NOT_SUPPLIED"
  val errorMessage: String   =
    "If the reporting company is not revoking itself, details of the company making revocation must be provided"
  val path: JsPath           = JsPath \ "companyMakingRevocation"
  val value: Option[JsValue] = None
}

case class DeclaredFiftyPercentOfEligibleCompanies(declaration: Boolean) extends Validation {
  val code: String           = "DECLARATION_FALSE"
  val errorMessage: String   = "Declaration is not valid so will not be submitted. " +
    "You need to confirm the listed companies constitute at least 50% of the eligible companies."
  val path: JsPath           = JsPath \ "declaration"
  val value: Option[JsValue] = Some(JsBoolean(declaration))
}

case class DetailsNotNeededIfCompanyRevokingItself(companyMakingRevocation: IdentityOfCompanySubmittingModel)(implicit
  val path: JsPath
) extends Validation {
  val code: String         = "COMPANY_NOT_NEEDED"
  val errorMessage: String = "Reporting company not needed as it is the same the company making the revocation"
  val value: Some[JsValue] = Some(Json.toJson(companyMakingRevocation))
}

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

package v1.validation.revokeReportingCompany

import play.api.libs.json.{JsBoolean, JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.revokeReportingCompany.RevokeReportingCompanyModel
import v1.models.{IdentityOfCompanySubmittingModel, UltimateParentModel, Validation}
import v1.validation._

trait RevokeReportingCompanyValidator extends BaseValidation {

  import cats.implicits._

  val revokeReportingCompanyModel: RevokeReportingCompanyModel

  private def validateReportingCompanyRevokeItself(implicit path: JsPath): ValidationResult[Boolean] = {
    val revokeItself = revokeReportingCompanyModel.isReportingCompanyRevokingItself
    val company = revokeReportingCompanyModel.companyMakingRevocation
    (revokeItself, company) match {
      case (true,Some(details)) => DetailsNotNeededIfCompanyRevokingItself(details).invalidNec
      case (false,None) => CompanyMakingAppointmentMustSupplyDetails.invalidNec
      case _ => revokeItself.validNec
    }
  }

  private def validateDeclaration(implicit path: JsPath): ValidationResult[Boolean] = {
    val declaration = revokeReportingCompanyModel.declaration
    if(declaration) declaration.validNec else {
      DeclaredFiftyPercentOfEligibleCompanies(declaration).invalidNec
    }
  }

  private def validateUltimateParentCompany(implicit path: JsPath): ValidationResult[Option[UltimateParentModel]] = {
    (revokeReportingCompanyModel.reportingCompany.sameAsUltimateParent, revokeReportingCompanyModel.ultimateParent) match {
      case (true, Some(parent)) => UltimateParentCompanyIsSuppliedRevoke(parent).invalidNec
      case _ => revokeReportingCompanyModel.ultimateParent.validNec
    }
  }

  def validate: ValidationResult[RevokeReportingCompanyModel] = {

    val validatedAuthorisingCompanies =
      if(revokeReportingCompanyModel.authorisingCompanies.isEmpty) AuthorisingCompaniesEmpty.invalidNec else {
        combineValidations(revokeReportingCompanyModel.authorisingCompanies.zipWithIndex.map {
          case (a, i) => a.validate(JsPath \ s"authorisingCompanies[$i]")
        }:_*)
      }

    (revokeReportingCompanyModel.agentDetails.validate(JsPath \ "agentDetails"),
      revokeReportingCompanyModel.reportingCompany.validate(JsPath \ "reportingCompany"),
      validateReportingCompanyRevokeItself(JsPath \ "isReportingCompanyRevokingItself"),
      optionValidations(revokeReportingCompanyModel.companyMakingRevocation.map(_.validate(JsPath \ "companyMakingRevocation"))),
      validateUltimateParentCompany(JsPath \ "ultimateParent"),
      optionValidations(revokeReportingCompanyModel.ultimateParent.map(_.validate(JsPath \ "ultimateParent"))),
      revokeReportingCompanyModel.accountingPeriod.validate(JsPath \ "accountingPeriod"),
      validatedAuthorisingCompanies,
      validateDeclaration(JsPath \ "declaration")
      ).mapN((_,_,_,_,_,_,_,_,_) => revokeReportingCompanyModel)
  }
}

case object CompanyMakingAppointmentMustSupplyDetails extends Validation {
  val code = MISSING_FIELD
  val message: String = "companyMakingRevocation must be supplied when isReportingCompanyRevokingItself is false"
  val path = JsPath \ "companyMakingRevocation"
}

case class DeclaredFiftyPercentOfEligibleCompanies(declaration: Boolean)(implicit val path: JsPath) extends Validation {
  val code = COMPANIES_DECLARATION
  val message: String = "The declaration that the listed companies constitute at least 50% of the eligible companies must be true"
}

case class DetailsNotNeededIfCompanyRevokingItself(companyMakingRevocation: IdentityOfCompanySubmittingModel)(implicit val path: JsPath) extends Validation {
  val code = UNEXPECTED_FIELD
  val message: String = "If the reporting company is submitting this revocation, the identity of company making revocation is not needed."
}

//TODO identify common messages and move
case class UltimateParentCompanyIsSuppliedRevoke(ultimateParentModel: UltimateParentModel) extends Validation {
  val code = UNEXPECTED_FIELD
  val message: String = "Ultimate Parent Company must not be supplied if it is the same as the reporting company"
  val path = JsPath \ "ultimateParentCompany"
}

case object AuthorisingCompaniesEmpty extends Validation {
  val code = MISSING_FIELD
  val message: String = "authorisingCompanies must have at least 1 authorising company"
  val path = JsPath \ "authorisingCompanies"
}
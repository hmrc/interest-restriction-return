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

package v1.validation.revokeReportingCompany

import play.api.libs.json.{JsBoolean, JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.revokeReportingCompany.RevokeReportingCompanyModel
import v1.models.{IdentityOfCompanySubmittingModel, UltimateParentModel, Validation}
import v1.validation.BaseValidation
import v1.models.AuthorisingCompanyModel

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

  private def validateUltimateParentCompany: ValidationResult[Option[UltimateParentModel]] = {
    (revokeReportingCompanyModel.reportingCompany.sameAsUltimateParent, revokeReportingCompanyModel.ultimateParentCompany) match {
      case (true, Some(parent)) => UltimateParentCompanyIsSuppliedRevoke(parent).invalidNec
      case (false, None) => UltimateParentCompanyIsNotSuppliedRevoke.invalidNec
      case _ => revokeReportingCompanyModel.ultimateParentCompany.validNec
    }
  }

  private def validateDuplicateAuthorisingCompanies: ValidationResult[Seq[AuthorisingCompanyModel]] = {
    val duplicatesExist = revokeReportingCompanyModel.authorisingCompanies.distinct.size != revokeReportingCompanyModel.authorisingCompanies.size
    duplicatesExist match {
      case true => AuthorisingCompaniesContainsDuplicates.invalidNec
      case false => revokeReportingCompanyModel.authorisingCompanies.validNec
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
      validateUltimateParentCompany,
      optionValidations(revokeReportingCompanyModel.ultimateParentCompany.map(_.validate(JsPath \ "ultimateParentCompany"))),
      revokeReportingCompanyModel.accountingPeriod.validate(JsPath \ "accountingPeriod"),
      validatedAuthorisingCompanies,
      validateDeclaration(JsPath \ "declaration"),
      validateDuplicateAuthorisingCompanies
      ).mapN((_,_,_,_,_,_,_,_,_, _) => revokeReportingCompanyModel)
  }
}

case object CompanyMakingAppointmentMustSupplyDetails extends Validation {
  val errorMessage: String = "companyMakingRevocation must be supplied when isReportingCompanyRevokingItself is false"
  val path = JsPath \ "companyMakingRevocation"
  val value = Json.obj()
}

case class DeclaredFiftyPercentOfEligibleCompanies(declaration: Boolean)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "The declaration that the listed companies constitute at least 50% of the eligible companies must be true"
  val value = JsBoolean(declaration)
}

case class DetailsNotNeededIfCompanyRevokingItself(companyMakingRevocation: IdentityOfCompanySubmittingModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "If the reporting company is submitting this revocation, the identity of company making revocation is not needed."
  val value = Json.toJson(companyMakingRevocation)
}

//TODO identify common messages and move
case class UltimateParentCompanyIsSuppliedRevoke(ultimateParentModel: UltimateParentModel) extends Validation {
  val errorMessage: String = "Ultimate Parent Company must not be supplied if it is the same as the reporting company"
  val path = JsPath \ "ultimateParentCompany"
  val value = Json.toJson(ultimateParentModel)
}

case object UltimateParentCompanyIsNotSuppliedRevoke extends Validation {
  val errorMessage: String = "Ultimate Parent Company must be supplied if it is not the same as the reporting company"
  val path = JsPath \ "ultimateParentCompany"
  val value = Json.obj()
}

case object AuthorisingCompaniesEmpty extends Validation {
  val errorMessage: String = "authorisingCompanies must have at least 1 authorising company"
  val path = JsPath \ "authorisingCompanies"
  val value = Json.obj()
}

case object AuthorisingCompaniesContainsDuplicates extends Validation {
  val errorMessage: String = "Authorising companies contain duplicate information"
  val path = JsPath \ "authorisingCompanies"
  val value = Json.obj()
}
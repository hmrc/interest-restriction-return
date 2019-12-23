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

package validation.revokeReportingCompany

import models.Validation.ValidationResult
import models.revokeReportingCompany.RevokeReportingCompanyModel
import models.{IdentityOfCompanySubmittingModel, UltimateParentModel, Validation}
import play.api.libs.json.{JsBoolean, JsPath, Json}
import validation.BaseValidation

trait RevokeReportingCompanyValidator extends BaseValidation {

  import cats.implicits._

  val revokeReportingCompanyModel: RevokeReportingCompanyModel

  private def validateReportingCompanyRevokeItself(implicit path: JsPath): ValidationResult[Boolean] = {
    val revokeItself = revokeReportingCompanyModel.isReportingCompanyRevokingItself
    val company = revokeReportingCompanyModel.companyMakingRevocation
    (revokeItself, company) match {
      case (true,Some(details)) => DetailsNotNeededIfCompanyRevokingItself(details).invalidNec
      case (false,None) => CompanyMakingAppointmentMustSupplyDetails().invalidNec
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

    val validatedAuthorisingCompanies = revokeReportingCompanyModel.authorisingCompanies.zipWithIndex.map {
      case (a, i) => a.validate(JsPath \ s"authorisingCompanies[$i]")
    }

    (revokeReportingCompanyModel.agentDetails.validate(JsPath \ "agentDetails"),
      revokeReportingCompanyModel.reportingCompany.validate(JsPath \ "reportingCompany"),
      validateReportingCompanyRevokeItself(JsPath \ "isReportingCompanyRevokingItself"),
      optionValidations(revokeReportingCompanyModel.companyMakingRevocation.map(_.validate(JsPath \ "companyMakingRevocation"))),
      validateUltimateParentCompany(JsPath \ "ultimateParent"),
      revokeReportingCompanyModel.accountingPeriod.validate(JsPath \ "accountingPeriod"),
      combineValidationsForField(validatedAuthorisingCompanies: _*),
      validateDeclaration(JsPath \ "declaration")
    ).mapN((_, _, _, _, _, _, _, _) => revokeReportingCompanyModel)
  }
}

case class CompanyMakingAppointmentMustSupplyDetails(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "If the reporting company (or their authorised agent) is not submitting this revocation, " +
    "the company making this appointment must supply their company name, CT UTR, CRN and Country of incorporation (if non-UK)."
  val value = Json.obj()
}

case class DeclaredFiftyPercentOfEligibleCompanies(declaration: Boolean)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "The declaration that the listed companies constitute at least 50% of the eligible companies is missing."
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

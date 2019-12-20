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

package validation

import models.{IdentityOfCompanySubmittingModel, Validation}
import models.Validation.ValidationResult
import models.revokeReportingCompany.RevokeReportingCompanyModel
import play.api.libs.json.{JsBoolean, JsPath, Json}

trait RevokeReportingCompanyValidator extends BaseValidation {

  import cats.implicits._

  val revokeReportingCompanyModel: RevokeReportingCompanyModel

  private def validateReportingCompanyRevokeItself(implicit path: JsPath): ValidationResult[Boolean] = {
    val revokeItself = revokeReportingCompanyModel.isReportingCompanyRevokingItself
    val company = revokeReportingCompanyModel.companyMakingRevocation
    (revokeItself, company) match {
      case (true,Some(details)) => DetailsNotNeededIfCompanyRevokingItself(details).invalidNec
      case (true,None) => revokeItself.validNec
      case (false,Some(companyMakingRevocation)) if companyMakingRevocation.validate.isValid => revokeItself.validNec
      case _ => CompanyMakingAppointmentMustSupplyDetails().invalidNec
    }
  }

  private def validateDeclaration(implicit path: JsPath): ValidationResult[Boolean] = {
    val declaration = revokeReportingCompanyModel.declaration
    if(declaration) declaration.validNec else {
      DeclaredFiftyPercentOfEligibleCompanies(declaration).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[RevokeReportingCompanyModel] =
    (revokeReportingCompanyModel.agentDetails.validate(path \ "agentDetails"),
      revokeReportingCompanyModel.reportingCompany.validate(path \ "reportingCompany"),
      validateReportingCompanyRevokeItself(path \ "isReportingCompanyRevokingItself"),
      optionValidations(revokeReportingCompanyModel.companyMakingRevocation.map(_.validate(path \ "companyMakingRevocation"))),
      optionValidations(revokeReportingCompanyModel.ultimateParent.map(_.validate(path \ "ultimateParent"))),
      revokeReportingCompanyModel.accountingPeriod.validate(path \ "accountingPeriod"),
      combineValidationsForField(revokeReportingCompanyModel.authorisingCompanies.map(_.validate(path \ "authorisingCompanies")):_*),
      validateDeclaration(path \ "declaration")
    ).mapN((_,_,_,_,_,_,_,_) => revokeReportingCompanyModel)
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

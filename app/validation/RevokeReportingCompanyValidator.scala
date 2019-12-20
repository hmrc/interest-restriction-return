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

import models.Validation
import models.Validation.ValidationResult
import models.revokeReportingCompany.RevokeReportingCompanyModel
import play.api.libs.json.{JsBoolean, JsPath, Json}

trait RevokeReportingCompanyValidator extends BaseValidation {

  import cats.implicits._

  val revokeReportingCompanyModel: RevokeReportingCompanyModel

  private def validateReportingCompanyRevokeItself(implicit path: JsPath): ValidationResult[Boolean] = {
    val revokeItself = revokeReportingCompanyModel.isReportingCompanyRevokingItself
    val company = revokeReportingCompanyModel.companyMakingRevocation
    val declaration = revokeReportingCompanyModel.declaration
    (revokeItself, company, declaration) match {
      case (true,_,true) => revokeItself.validNec
      case (false,Some(companyMakingRevocation),true) if companyMakingRevocation.validate.isValid => revokeItself.validNec
      case (_,_,false) => DeclaredFiftyPercentOfEligibleCompanies(declaration).invalidNec
      case _ => CompanyMakingAppointmentMustSupplyDetails().invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[RevokeReportingCompanyModel] =
    (revokeReportingCompanyModel.agentDetails.validate(path \ "agentDetails"),
      revokeReportingCompanyModel.reportingCompany.validate(path \ "reportingCompany"),
      validateReportingCompanyRevokeItself,
      optionValidations(revokeReportingCompanyModel.companyMakingRevocation.map(_.validate(path \ "companyMakingRevocation"))),
      optionValidations(revokeReportingCompanyModel.ultimateParent.map(_.validate(path \ "ultimateParent"))),
      revokeReportingCompanyModel.accountingPeriod.validate(path \ "accountingPeriod"),
      combineValidationsForField(revokeReportingCompanyModel.authorisingCompanies.map(_.validate(path \ "authorisingCompanies")):_*)
    ).mapN((_,_,_,_,_,_,_) => revokeReportingCompanyModel)
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

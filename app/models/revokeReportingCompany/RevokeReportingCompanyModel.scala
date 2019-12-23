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

package models.revokeReportingCompany

import models.{AccountingPeriodModel, AgentDetailsModel, AuthorisingCompanyModel, IdentityOfCompanySubmittingModel, ReportingCompanyModel, UltimateParentModel}
import play.api.libs.json.Json
import validation.revokeReportingCompany.RevokeReportingCompanyValidator

case class RevokeReportingCompanyModel(agentDetails: AgentDetailsModel,
                                       reportingCompany: ReportingCompanyModel,
                                       isReportingCompanyRevokingItself: Boolean,
                                       companyMakingRevocation: Option[IdentityOfCompanySubmittingModel],
                                       ultimateParent: Option[UltimateParentModel],
                                       accountingPeriod: AccountingPeriodModel,
                                       authorisingCompanies: Seq[AuthorisingCompanyModel],
                                       declaration: Boolean) extends RevokeReportingCompanyValidator {
  override val revokeReportingCompanyModel: RevokeReportingCompanyModel = this
}

object RevokeReportingCompanyModel {
  implicit val format = Json.format[RevokeReportingCompanyModel]
}

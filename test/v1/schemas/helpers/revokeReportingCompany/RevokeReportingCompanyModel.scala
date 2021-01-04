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

package v1.schemas.helpers.revokeReportingCompany

import play.api.libs.json.Json
import v1.schemas.helpers._

case class RevokeReportingCompanyModel(agentDetails: Option[AgentDetails] = Some(AgentDetails()),
                                       reportingCompany: Option[ReportingCompany] = Some(ReportingCompany()),
                                       isReportingCompanyRevokingItself: Option[Boolean] = Some(true),
                                       companyMakingRevocation: Option[IdentityOfCompanySubmitting] = Some(IdentityOfCompanySubmitting()),
                                       ultimateParentCompany: Option[UltimateParent] = Some(UltimateParent(sautr = None, countryOfIncorporation = None)),
                                       accountingPeriod: Option[AccountingPeriod] = Some(AccountingPeriod()),
                                       authorisingCompanies: Option[Seq[AuthorisingCompanies]] = Some(Seq(AuthorisingCompanies())),
                                       declaration: Option[Boolean] = Some(true))

object RevokeReportingCompanyModel {
  implicit val writes = Json.writes[RevokeReportingCompanyModel]
}


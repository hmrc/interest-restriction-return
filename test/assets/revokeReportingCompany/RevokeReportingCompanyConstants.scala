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

package assets.revokeReportingCompany

import models.revokeReportingCompany.RevokeReportingCompanyModel
import assets.AgentDetailsConstants._
import assets.ReportingCompanyConstants._
import assets.UltimateParentConstants._
import assets.AccountingPeriodConstants._
import assets.AuthorisingCompanyConstants._
import assets.revokeReportingCompany.CompanyMakingRevocationConstants._
import play.api.libs.json.Json

object RevokeReportingCompanyConstants {

  val revokeReportingCompanyModelMax = RevokeReportingCompanyModel(
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModelMax,
    isReportingCompanyRevokingItself = true,
    companyMakingRevocation = Some(companyMakingRevocationModelMax),
    ultimateParent = Some(ukParentModelMax),
    accountingPeriod = accountingPeriodModel,
    authorisingCompanies = Seq(authorisingCompanyModel),
    declaration = true
  )

  val revokeReportingCompanyJsonMax = Json.obj(
    "agentDetails" -> agentDetailsModelMax,
    "reportingCompany" -> reportingCompanyModelMax,
    "isReportingCompanyRevokingItself" -> true,
    "companyMakingRevocation" -> companyMakingRevocationJsonMax,
    "ultimateParent" -> ukParentModelMax,
    "accountingPeriod" -> accountingPeriodModel,
    "authorisingCompanies" -> Seq(authorisingCompanyModel),
    "declaration" -> true
  )

  val revokeReportingCompanyModelMin = RevokeReportingCompanyModel(
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModelMax,
    isReportingCompanyRevokingItself = true,
    companyMakingRevocation = None,
    ultimateParent = None,
    accountingPeriod = accountingPeriodModel,
    authorisingCompanies = Seq(authorisingCompanyModel),
    declaration = true
  )

  val revokeReportingCompanyJsonMin = Json.obj(
    "agentDetails" -> agentDetailsModelMax,
    "reportingCompany" -> reportingCompanyModelMax,
    "isReportingCompanyRevokingItself" -> true,
    "accountingPeriod" -> accountingPeriodModel,
    "authorisingCompanies" -> Seq(authorisingCompanyModel),
    "declaration" -> true
  )
}

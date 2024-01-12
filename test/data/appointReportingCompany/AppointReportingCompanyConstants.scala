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

package data.appointReportingCompany

import data.AccountingPeriodConstants._
import data.AgentDetailsConstants._
import data.AuthorisingCompanyConstants._
import data.IdentityOfCompanySubmittingConstants._
import data.ReportingCompanyConstants._
import data.UltimateParentConstants._
import play.api.libs.json.{JsObject, Json}
import v1.models.appointReportingCompany.AppointReportingCompanyModel

object AppointReportingCompanyConstants {

  val ackRef: String = "ackRef"

  val appointReportingCompanyJsonMax: JsObject = Json.obj(
    "agentDetails"                       -> agentDetailsJsonMax,
    "reportingCompany"                   -> reportingCompanyJson,
    "authorisingCompanies"               -> Json.arr(authorisingCompanyJson),
    "isReportingCompanyAppointingItself" -> false,
    "identityOfAppointingCompany"        -> identityOfCompanySubmittingJsonMax,
    "ultimateParentCompany"              -> ultimateParentJsonUkCompany,
    "accountingPeriod"                   -> accountingPeriodJson,
    "declaration"                        -> true
  )

  val appointReportingCompanyJsonMin: JsObject = Json.obj(
    "agentDetails"                       -> agentDetailsJsonMin,
    "reportingCompany"                   -> reportingCompanyJson,
    "authorisingCompanies"               -> Json.arr(authorisingCompanyJson),
    "isReportingCompanyAppointingItself" -> true,
    "accountingPeriod"                   -> accountingPeriodJson,
    "declaration"                        -> true
  )

  val invalidAppointReportingCompanyJson: JsObject = Json.obj(
    "agentDetails"                       -> agentDetailsJsonMin,
    "reportingCompany"                   -> reportingCompanyJson,
    "isReportingCompanyAppointingItself" -> true,
    "accountingPeriod"                   -> accountingPeriodJson,
    "declaration"                        -> true
  )

  val appointReportingCompanyModelMax: AppointReportingCompanyModel = AppointReportingCompanyModel(
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    authorisingCompanies = Seq(authorisingCompanyModel),
    isReportingCompanyAppointingItself = false,
    identityOfAppointingCompany = Some(identityOfCompanySubmittingModelMax),
    ultimateParentCompany = Some(ultimateParentModelUkCompany),
    accountingPeriod = accountingPeriodModel,
    declaration = true
  )

  val appointReportingCompanyModelMin: AppointReportingCompanyModel = AppointReportingCompanyModel(
    agentDetails = agentDetailsModelMin,
    reportingCompany = reportingCompanyModel,
    authorisingCompanies = Seq(authorisingCompanyModel),
    isReportingCompanyAppointingItself = true,
    identityOfAppointingCompany = None,
    ultimateParentCompany = None,
    accountingPeriod = accountingPeriodModel,
    declaration = true
  )
}

/*
 * Copyright 2025 HM Revenue & Customs
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

package data

import data.AccountingPeriodITConstants.*
import data.AgentDetailsITConstants.*
import data.AuthorisingCompanyITConstants.*
import data.ReportingCompanyITConstants.*
import play.api.libs.json.{JsObject, Json}

object RevokeReportingCompanyITConstants {

  val ackRef: String = "ackRef"

  val revokeReportingCompanyJson: JsObject = Json.obj(
    "agentDetails"                     -> agentDetailsJson,
    "reportingCompany"                 -> reportingCompanyJson,
    "isReportingCompanyRevokingItself" -> true,
    "accountingPeriod"                 -> accountingPeriodJson,
    "authorisingCompanies"             -> Seq(authorisingCompanyJson),
    "declaration"                      -> true
  )

  val revokeReportingCompanySuccessJson: JsObject = Json.obj(
    "acknowledgementReference" -> ackRef
  )
}

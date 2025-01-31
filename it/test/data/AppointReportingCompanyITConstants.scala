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

package data

import data.AccountingPeriodITConstants.*
import data.AgentDetailsITConstants.*
import data.AuthorisingCompanyITConstants.*
import data.IdentityOfCompanySubmittingITConstants.*
import data.ReportingCompanyITConstants.*
import play.api.libs.json.{JsObject, Json}

object AppointReportingCompanyITConstants {

  val ackRef: String = "ackRef"

  val appointReportingCompanyJson: JsObject = Json.obj(
    "agentDetails"                       -> agentDetailsJson,
    "reportingCompany"                   -> reportingCompanyJson,
    "authorisingCompanies"               -> Json.arr(authorisingCompanyJson),
    "isReportingCompanyAppointingItself" -> false,
    "identityOfAppointingCompany"        -> identityOfCompanySubmittingJson,
    "accountingPeriod"                   -> accountingPeriodJson,
    "declaration"                        -> true
  )

  val appointReportingCompanyDesSuccessJson: JsObject = Json.obj(
    "acknowledgementReference" -> ackRef
  )
}

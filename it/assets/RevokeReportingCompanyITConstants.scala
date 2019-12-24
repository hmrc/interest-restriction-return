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

package assets

import assets.AgentDetailsITConstants._
import assets.ReportingCompanyITConstants._
import assets.IdentityOfCompanySubmittingITConstants._
import assets.UltimateParentITConstants._
import assets.AccountingPeriodITConstants._
import assets.AuthorisingCompanyITConstants._
import play.api.libs.json.Json

object RevokeReportingCompanyITConstants {

  val ackRef = "ackRef"

  val revokeReportingCompanyJson = Json.obj(
    "agentDetails" -> agentDetailsJson,
    "reportingCompany" -> reportingCompanyJson,
    "isReportingCompanyRevokingItself" -> true,
    "accountingPeriod" -> accountingPeriodJson,
    "authorisingCompanies" -> Seq(authorisingCompanyJson),
    "declaration" -> true
  )

  println("#########\n\n\n\n\n")
  println("Json: " + revokeReportingCompanyJson)
  println("\n\n\n\n\n#########")

  val revokeReportingCompanyDesSuccessJson = Json.obj(
    "acknowledgementReference" -> ackRef
  )
}

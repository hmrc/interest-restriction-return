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
import assets.AuthorisingCompanyITConstants._
import play.api.libs.json.Json

object AppointReportingCompanyITConstants {

  val ackRef = "ackRef"

  val appointReportingCompanyJson = Json.obj(
    "agentDetails" -> agentDetailsJsonMax,
    "reportingCompany" -> reportingCompanyJsonMax,
    "authorisingCompanies" -> Json.arr(
      authorisingCompanyJson
    ),
    "declaration" -> true
  )

  val appointReportingCompanyDesSuccessJson = Json.obj(
    "acknowledgementReference" -> ackRef
  )
}
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

import data.AdjustedGroupInterestITConstants.adjustedGroupInterestJson
import data.AgentDetailsITConstants.*
import data.GroupCompanyDetailsITConstants.groupCompanyDetailsJson
import data.GroupLevelAmountITConstants.groupLevelAmountJson
import data.GroupLevelElectionsITConstants.groupLevelElectionsJson
import data.ReportingCompanyITConstants.*
import data.UkCompanyITConstants.ukCompanyFullJson
import play.api.libs.json.{JsObject, Json}

object FullReturnITConstants {

  val ackRef: String = "ackRef"

  val fullReturnSuccessJson: JsObject = Json.obj(
    "acknowledgementReference" -> ackRef
  )

  val fullReturnJson: JsObject = Json.obj(
    "declaration"                        -> true,
    "appointedReportingCompany"          -> true,
    "agentDetails"                       -> agentDetailsJson,
    "reportingCompany"                   -> reportingCompanyJson,
    "publicInfrastructure"               -> true,
    "groupCompanyDetails"                -> groupCompanyDetailsJson,
    "submissionType"                     -> "revised",
    "revisedReturnDetails"               -> "some details",
    "groupLevelElections"                -> groupLevelElectionsJson,
    "returnContainsEstimates"            -> true,
    "groupEstimateReason"                -> "Some reason",
    "ukCompanies"                        -> Seq(ukCompanyFullJson),
    "angie"                              -> 1.1,
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation"                  -> 2.22,
    "totalRestrictions"                  -> 0,
    "groupLevelAmount"                   -> groupLevelAmountJson,
    "adjustedGroupInterest"              -> adjustedGroupInterestJson
  )
}

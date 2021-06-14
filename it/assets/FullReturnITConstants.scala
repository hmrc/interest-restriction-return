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

package assets

import assets.AdjustedGroupInterestITConstants.adjustedGroupInterestJson
import assets.AgentDetailsITConstants._
import assets.GroupCompanyDetailsITConstants.groupCompanyDetailsJson
import assets.GroupLevelAmountITConstants.groupLevelAmountJson
import assets.GroupLevelElectionsITConstants.groupLevelElectionsJson
import assets.ReportingCompanyITConstants._
import assets.UkCompanyITConstants.ukCompanyFullJson
import play.api.libs.json.Json
import v1.models.{SubmissionType, Revised}

object FullReturnITConstants {


  val ackRef = "ackRef"

  val fullReturnDesSuccessJson = Json.obj(
    "acknowledgementReference" -> ackRef
  )

  val fullReturnJson = Json.obj(
    "declaration" -> true,
    "appointedReportingCompany" -> true,
    "agentDetails" -> agentDetailsJson,
    "reportingCompany" -> reportingCompanyJson,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Json.toJson[SubmissionType](Revised),
    "revisedReturnDetails" -> "some details",
    "groupLevelElections" -> groupLevelElectionsJson,
    "returnContainsEstimates" -> true,
    "groupEstimateReason" -> "Some reason",
    "ukCompanies" -> Seq(ukCompanyFullJson),
    "angie" -> 1.1,
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> 2.22,
    "totalRestrictions" -> 0,
    "groupLevelAmount" -> groupLevelAmountJson,
    "adjustedGroupInterest" -> adjustedGroupInterestJson
  )
}

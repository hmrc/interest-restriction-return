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

import assets.AdjustedGroupInterestITConstants.adjustedGroupInterestJson
import assets.AgentDetailsITConstants._
import assets.GroupCompanyDetailsITConstants.groupCompanyDetailsJson
import assets.GroupLevelAmountITConstants.groupLevelAmountJson
import assets.GroupLevelElectionsITConstants.groupLevelElectionsJson
import assets.ParentCompanyITConstants.parentCompanyJson
import assets.ReportingCompanyITConstants._
import assets.UkCompanyITConstants.ukCompanyFullJson
import models.Original
import play.api.libs.json.Json

object FullReturnITConstants {


  val ackRef = "ackRef"

  val fullReturnDesSuccessJson = Json.obj(
    "acknowledgementReference" -> ackRef
  )

  val fullReturnJson = Json.obj(
    "agentDetails" -> agentDetailsJson,
    "reportingCompany" -> reportingCompanyJson,
    "parentCompany" -> parentCompanyJson,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Original,
    "revisedReturnDetails" -> "some details",
    "groupLevelElections" -> groupLevelElectionsJson,
    "ukCompanies" -> Seq(ukCompanyFullJson),
    "angie" -> 1.1,
    "groupSubjectToInterestRestrictions" -> true,
    "groupSubjectToInterestReactivation" -> true,
    "groupLevelAmount" -> groupLevelAmountJson,
    "adjustedGroupInterest" -> adjustedGroupInterestJson
  )
}
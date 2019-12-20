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

package assets.abbreviatedReturn

import assets.AgentDetailsConstants._
import assets.GroupCompanyDetailsConstants._
import assets.GroupLevelElectionsConstants._
import assets.ParentCompanyConstants._
import assets.ReportingCompanyConstants._
import assets.abbreviatedReturn.UkCompanyConstants._
import models.Original
import models.abbreviatedReturn.AbbreviatedReturnModel
import play.api.libs.json.Json

object AbbreviatedReturnConstants {

  val ackRef = "ackRef"

  val abbreviatedReturnJsonMax = Json.obj(
    "agentDetails" -> agentDetailsJsonMax,
    "reportingCompany" -> reportingCompanyJsonMax,
    "publicInfrastructure" -> true,
    "parentCompany" -> parentCompanyJsonMax,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Original,
    "revisedReturnDetails" -> "revised details",
    "groupLevelElections" -> groupLevelElectionsJsonMax,
    "ukCompanies" -> Seq(ukCompanyJson)
  )

  val abbreviatedReturnModelMax = AbbreviatedReturnModel(
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModelMax,
    publicInfrastructure = true,
    parentCompany = parentCompanyModelMax,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Original,
    revisedReturnDetails = Some("revised details"),
    groupLevelElections = Some(groupLevelElectionsModel),
    ukCompanies = Seq(ukCompanyModel)
  )

  val abbreviatedReturnJsonMin = Json.obj(
    "agentDetails" -> agentDetailsJsonMin,
    "reportingCompany" -> reportingCompanyJsonMin,
    "publicInfrastructure" -> true,
    "parentCompany" -> parentCompanyJsonMin,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Original,
    "ukCompanies" -> Seq(ukCompanyJson)
  )

  val abbreviatedReturnModelMin = AbbreviatedReturnModel(
    agentDetails = agentDetailsModelMin,
    reportingCompany = reportingCompanyModelMin,
    publicInfrastructure = true,
    parentCompany = parentCompanyModelMin,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Original,
    revisedReturnDetails = None,
    groupLevelElections = None,
    ukCompanies = Seq(ukCompanyModel)
  )
}

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

import assets.AgentDetailsConstants.{agentDetailsModelMax, agentDetailsModelMin}
import assets.GroupCompanyDetailsConstants.groupCompanyDetailsModel
import assets.GroupLevelElectionsConstants.groupLevelElectionsModelMax
import assets.ParentCompanyConstants.{parentCompanyModelMax, parentCompanyModelMin}
import assets.ReportingCompanyConstants.{reportingCompanyModelMax, reportingCompanyModelMin}
import assets.fullReturn.AdjustedGroupInterestConstants.adjustedGroupInterestModel
import assets.fullReturn.GroupLevelAmountConstants.groupLevelAmountModel
import assets.fullReturn.UkCompanyConstants.{ukCompanyModelMax, ukCompanyModelMin}
import models.Original
import models.fullReturn.FullReturnModel
import play.api.libs.json.Json

object FullReturnITConstants {

  val revisedReturnDetails = "some details"
  val angie: BigDecimal = 1.11

  val fullReturnModelMax = FullReturnModel(
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModelMax,
    parentCompany = parentCompanyModelMax,
    publicInfrastructure = true,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Original,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = Some(groupLevelElectionsModelMax),
    ukCompanies = Seq(ukCompanyModelMax),
    angie = Some(angie),
    groupSubjectToInterestRestrictions = true,
    groupSubjectToInterestReactivation = true,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = adjustedGroupInterestModel
  )

  val fullReturnJsonMax = Json.obj(
    "agentDetails" -> agentDetailsModelMax,
    "reportingCompany" -> reportingCompanyModelMax,
    "parentCompany" -> parentCompanyModelMax,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsModel,
    "submissionType" -> Original,
    "revisedReturnDetails" -> revisedReturnDetails,
    "groupLevelElections" -> groupLevelElectionsModelMax,
    "ukCompanies" -> Seq(ukCompanyModelMax),
    "angie" -> angie,
    "groupSubjectToInterestRestrictions" -> true,
    "groupSubjectToInterestReactivation" -> true,
    "groupLevelAmount" -> groupLevelAmountModel,
    "adjustedGroupInterest" -> adjustedGroupInterestModel
  )

  val fullReturnModelMin = FullReturnModel(
    agentDetails = agentDetailsModelMin,
    reportingCompany = reportingCompanyModelMin,
    parentCompany = parentCompanyModelMin,
    publicInfrastructure = true,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Original,
    revisedReturnDetails = None,
    groupLevelElections = None,
    ukCompanies = Seq(ukCompanyModelMin),
    angie = None,
    groupSubjectToInterestRestrictions = true,
    groupSubjectToInterestReactivation = true,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = adjustedGroupInterestModel
  )

  val fullReturnJsonMin = Json.obj(
    "agentDetails" -> agentDetailsModelMin,
    "reportingCompany" -> reportingCompanyModelMin,
    "parentCompany" -> parentCompanyModelMin,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsModel,
    "submissionType" -> Original,
    "ukCompanies" -> Seq(ukCompanyModelMin),
    "groupSubjectToInterestRestrictions" -> true,
    "groupSubjectToInterestReactivation" -> true,
    "groupLevelAmount" -> groupLevelAmountModel,
    "adjustedGroupInterest" -> adjustedGroupInterestModel
  )
}

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

package assets.fullReturn

import assets.AgentDetailsConstants._
import assets.GroupCompanyDetailsConstants._
import assets.GroupLevelElectionsConstants._
import assets.ParentCompanyConstants._
import assets.ReportingCompanyConstants._
import assets.fullReturn.GroupLevelAmountConstants._
import assets.fullReturn.UkCompanyConstants._
import assets.fullReturn.AdjustedGroupInterestConstants._
import models.{Original, Revised}
import models.fullReturn.FullReturnModel
import play.api.libs.json.Json

object FullReturnConstants {

  val ackRef = "ackRef"

  val revisedReturnDetails = "some details"
  val angie: BigDecimal = 1.11
  val totalReactivations = ukCompanyModelMax.allocatedRestrictions.foldLeft[BigDecimal](0){
    (total, company) => total + company.totalDisallowances.getOrElse[BigDecimal](0)
  }

  val fullReturnModelMax = FullReturnModel(
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModelMax,
    parentCompany = Some(parentCompanyModelUlt),
    publicInfrastructure = true,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = Some(groupLevelElectionsModel),
    ukCompanies = Seq(ukCompanyModelMax),
    angie = Some(angie),
    groupSubjectToInterestRestrictions = true,
    groupSubjectToInterestReactivation = true,
    totalReactivation = totalReactivations,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = adjustedGroupInterestModel
  )

  val fullReturnJsonMax = Json.obj(
    "agentDetails" -> agentDetailsJsonMax,
    "reportingCompany" -> reportingCompanyJsonMax,
    "parentCompany" -> parentCompanyJsonUlt,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Revised,
    "revisedReturnDetails" -> revisedReturnDetails,
    "groupLevelElections" -> groupLevelElectionsJsonMax,
    "ukCompanies" -> Seq(ukCompanyJsonMax),
    "angie" -> angie,
    "groupSubjectToInterestRestrictions" -> true,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> totalReactivations,
    "groupLevelAmount" -> groupLevelAmountJson,
    "adjustedGroupInterest" -> adjustedGroupInterestJson
  )

  val fullReturnModelMin = FullReturnModel(
    agentDetails = agentDetailsModelMin,
    reportingCompany = reportingCompanyModelMin,
    parentCompany = None,
    publicInfrastructure = true,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Original,
    revisedReturnDetails = None,
    groupLevelElections = None,
    ukCompanies = Seq(ukCompanyModelMin),
    angie = None,
    groupSubjectToInterestRestrictions = true,
    groupSubjectToInterestReactivation = true,
    totalReactivation = totalReactivations,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = adjustedGroupInterestModel
  )

  val fullReturnJsonMin = Json.obj(
    "agentDetails" -> agentDetailsJsonMin,
    "reportingCompany" -> reportingCompanyJsonMin,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Original,
    "ukCompanies" -> Seq(ukCompanyJsonMin),
    "groupSubjectToInterestRestrictions" -> true,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> totalReactivations,
    "groupLevelAmount" -> groupLevelAmountJson,
    "adjustedGroupInterest" -> adjustedGroupInterestJson
  )
}

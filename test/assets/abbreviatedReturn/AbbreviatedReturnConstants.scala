/*
 * Copyright 2022 HM Revenue & Customs
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
import play.api.libs.json.Json
import v1.models.abbreviatedReturn.AbbreviatedReturnModel
import v1.models.{Original, Revised, RevisedReturnDetailsModel, SubmissionType}
import v1.models.SubmissionType._

object AbbreviatedReturnConstants {

  val ackRef = "ackRef"

  val abbreviatedReturnUltimateParentJson = Json.obj(
    "declaration" -> true,
    "appointedReportingCompany" -> true,
    "agentDetails" -> agentDetailsJsonMax,
    "reportingCompany" -> reportingCompanyJson,
    "publicInfrastructure" -> true,
    "parentCompany" -> parentCompanyJsonUltUkCompany,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Json.toJson[SubmissionType](Revised),
    "revisedReturnDetails" -> "revised details",
    "groupLevelElections" -> groupLevelElectionsJsonMax,
    "ukCompanies" -> Seq(ukCompanyJson),
    "numberOfUkCompanies" -> 1
  )

  val abbreviatedReturnUltimateParentModel = AbbreviatedReturnModel(
    declaration = true,
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelUltUkCompany),
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(RevisedReturnDetailsModel("revised details")),
    groupLevelElections = Some(groupLevelElectionsModelMax),
    ukCompanies = Seq(ukCompanyModel)
  )

  val abbreviatedReturnDeemedParentJson = Json.obj(
    "declaration" -> true,
    "appointedReportingCompany" -> true,
    "agentDetails" -> agentDetailsJsonMax,
    "reportingCompany" -> reportingCompanyJson,
    "publicInfrastructure" -> true,
    "parentCompany" -> parentCompanyJsonDeemedMax,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Json.toJson[SubmissionType](Revised),
    "revisedReturnDetails" -> "revised details",
    "groupLevelElections" -> groupLevelElectionsJsonMax,
    "ukCompanies" -> Seq(ukCompanyJson),
    "numberOfUkCompanies" -> 1
  )

  val abbreviatedReturnDeemedParentModel = AbbreviatedReturnModel(
    declaration = true,
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelDeemedMax),
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(RevisedReturnDetailsModel("revised details")),
    groupLevelElections = Some(groupLevelElectionsModelMax),
    ukCompanies = Seq(ukCompanyModel)
  )

  val abbreviatedReturnJsonMin = Json.obj(
    "declaration" -> true,
    "appointedReportingCompany" -> true,
    "agentDetails" -> agentDetailsJsonMin,
    "reportingCompany" -> reportingCompanyJson,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Json.toJson[SubmissionType](Original),
    "ukCompanies" -> Seq(ukCompanyJson),
    "numberOfUkCompanies" -> 1
  )

  val abbreviatedReturnModelMin = AbbreviatedReturnModel(
    declaration = true,
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMin,
    reportingCompany = reportingCompanyModel,
    parentCompany = None,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Original,
    revisedReturnDetails = None,
    groupLevelElections = None,
    ukCompanies = Seq(ukCompanyModel)
  )
}

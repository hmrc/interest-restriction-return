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

package data.fullReturn

import data.AgentDetailsConstants._
import data.GroupCompanyDetailsConstants._
import data.GroupLevelElectionsConstants._
import data.ParentCompanyConstants._
import data.ReportingCompanyConstants._
import data.fullReturn.AdjustedGroupInterestConstants._
import data.fullReturn.GroupLevelAmountConstants._
import data.fullReturn.UkCompanyConstants._
import play.api.libs.json.{JsObject, Json}
import v1.models.fullReturn.FullReturnModel
import v1.models.{Original, Revised, RevisedReturnDetailsModel, SubmissionType}

object FullReturnConstants {

  val ackRef: String                                  = "ackRef"
  val revisedReturnDetails: RevisedReturnDetailsModel = RevisedReturnDetailsModel("some details")
  val angie: BigDecimal                               = 1.11
  val totalReactivations: BigDecimal                  = ukCompanyModelReactivationMax.allocatedReactivations.foldLeft[BigDecimal](0) {
    (total, company) => total + company.currentPeriodReactivation
  }

  val totalRestrictions: BigDecimal = ukCompanyModelRestrictionMax.allocatedRestrictions.foldLeft[BigDecimal](0) {
    (total, company) => total + company.totalDisallowances
  }

  val fullReturnModelMax: FullReturnModel = FullReturnModel(
    declaration = true,
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelUltUkCompany),
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = groupLevelElectionsModelMax,
    ukCompanies = Seq(ukCompanyModelMax, ukCompanyModelMin),
    angie = angie,
    returnContainsEstimates = true,
    groupEstimateReason = Some("Some reason"),
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalRestrictions = 0,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )

  val fullReturnJsonMax: JsObject = Json.obj(
    "declaration"                        -> true,
    "appointedReportingCompany"          -> true,
    "agentDetails"                       -> agentDetailsJsonMax,
    "reportingCompany"                   -> reportingCompanyJson,
    "parentCompany"                      -> parentCompanyJsonUltUkCompany,
    "publicInfrastructure"               -> true,
    "groupCompanyDetails"                -> groupCompanyDetailsJson,
    "submissionType"                     -> "revised",
    "revisedReturnDetails"               -> revisedReturnDetails,
    "groupLevelElections"                -> groupLevelElectionsJsonMax,
    "ukCompanies"                        -> Seq(ukCompanyJsonMax, ukCompanyJsonMin),
    "angie"                              -> angie,
    "returnContainsEstimates"            -> true,
    "groupEstimateReason"                -> "Some reason",
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation"                  -> fullReturnModelMax.totalReactivation,
    "totalRestrictions"                  -> 0,
    "groupLevelAmount"                   -> groupLevelAmountJson,
    "adjustedGroupInterest"              -> adjustedGroupInterestJson
  )

  val fullReturnNetTaxExpenseModelMax: FullReturnModel = FullReturnModel(
    declaration = true,
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelUltUkCompany),
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = groupLevelElectionsModelMax,
    ukCompanies = Seq(ukCompanyModelMax, ukCompanyModelMax, ukCompanyModelMax, ukCompanyModelMin),
    angie = angie,
    returnContainsEstimates = true,
    groupEstimateReason = Some("Some reason"),
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalRestrictions = 0,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )

  val fullReturnNetTaxExpenseJsonMax: JsObject = Json.obj(
    "declaration"                        -> true,
    "appointedReportingCompany"          -> true,
    "agentDetails"                       -> agentDetailsJsonMax,
    "reportingCompany"                   -> reportingCompanyJson,
    "parentCompany"                      -> parentCompanyJsonUltUkCompany,
    "publicInfrastructure"               -> true,
    "groupCompanyDetails"                -> groupCompanyDetailsJson,
    "submissionType"                     -> "revised",
    "revisedReturnDetails"               -> revisedReturnDetails,
    "groupLevelElections"                -> groupLevelElectionsJsonMax,
    "ukCompanies"                        -> Seq(ukCompanyJsonMax, ukCompanyJsonMax, ukCompanyJsonMax, ukCompanyJsonMin),
    "numberOfUkCompanies"                -> 4,
    "angie"                              -> angie,
    "returnContainsEstimates"            -> true,
    "groupEstimateReason"                -> "Some reason",
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation"                  -> totalReactivations * 3,
    "totalRestrictions"                  -> 0,
    "groupLevelAmount"                   -> groupLevelAmountJson,
    "adjustedGroupInterest"              -> adjustedGroupInterestJson,
    "aggregateNetTaxInterest"            -> -10.0,
    "aggregateAllocatedRestrictions"     -> 18,
    "aggregateTaxEBITDA"                 -> 20,
    "aggregateAllocatedReactivations"    -> 6
  )

  val fullReturnNetTaxIncomeModelMax: FullReturnModel = FullReturnModel(
    declaration = true,
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelUltUkCompany),
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = groupLevelElectionsModelMax,
    ukCompanies = Seq(ukCompanyModelMin, ukCompanyModelMin, ukCompanyModelMin, ukCompanyModelMax),
    angie = angie,
    returnContainsEstimates = true,
    groupEstimateReason = Some("Some reason"),
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalRestrictions = 0,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )

  val fullReturnNetTaxIncomeJsonMax: JsObject = Json.obj(
    "declaration"                        -> true,
    "appointedReportingCompany"          -> true,
    "agentDetails"                       -> agentDetailsJsonMax,
    "reportingCompany"                   -> reportingCompanyJson,
    "parentCompany"                      -> parentCompanyJsonUltUkCompany,
    "publicInfrastructure"               -> true,
    "groupCompanyDetails"                -> groupCompanyDetailsJson,
    "submissionType"                     -> "revised",
    "revisedReturnDetails"               -> revisedReturnDetails,
    "groupLevelElections"                -> groupLevelElectionsJsonMax,
    "ukCompanies"                        -> Seq(ukCompanyJsonMin, ukCompanyJsonMin, ukCompanyJsonMin, ukCompanyJsonMax),
    "numberOfUkCompanies"                -> 4,
    "angie"                              -> angie,
    "returnContainsEstimates"            -> true,
    "groupEstimateReason"                -> "Some reason",
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation"                  -> totalReactivations,
    "totalRestrictions"                  -> 0,
    "groupLevelAmount"                   -> groupLevelAmountJson,
    "adjustedGroupInterest"              -> adjustedGroupInterestJson,
    "aggregateNetTaxInterest"            -> 130,
    "aggregateAllocatedRestrictions"     -> 6,
    "aggregateTaxEBITDA"                 -> 20,
    "aggregateAllocatedReactivations"    -> 2
  )

  val fullReturnUltimateParentModel: FullReturnModel = FullReturnModel(
    declaration = true,
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelUltUkCompany),
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = groupLevelElectionsModelMax,
    ukCompanies = Seq(ukCompanyModelReactivationMax, ukCompanyModelReactivationMax),
    angie = angie,
    returnContainsEstimates = true,
    groupEstimateReason = Some("Some reason"),
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalRestrictions = 0,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )

  val fullReturnUltimateParentJson: JsObject = Json.obj(
    "declaration"                        -> true,
    "appointedReportingCompany"          -> true,
    "agentDetails"                       -> agentDetailsJsonMax,
    "reportingCompany"                   -> reportingCompanyJson,
    "parentCompany"                      -> parentCompanyJsonUltUkCompany,
    "publicInfrastructure"               -> true,
    "groupCompanyDetails"                -> groupCompanyDetailsJson,
    "submissionType"                     -> "revised",
    "revisedReturnDetails"               -> revisedReturnDetails,
    "groupLevelElections"                -> groupLevelElectionsJsonMax,
    "ukCompanies"                        -> Seq(ukCompanyReactivationJsonMax, ukCompanyReactivationJsonMax),
    "numberOfUkCompanies"                -> 2,
    "angie"                              -> angie,
    "returnContainsEstimates"            -> true,
    "groupEstimateReason"                -> "Some reason",
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation"                  -> totalReactivations * 2,
    "totalRestrictions"                  -> 0,
    "groupLevelAmount"                   -> groupLevelAmountJson,
    "adjustedGroupInterest"              -> adjustedGroupInterestJson,
    "aggregateNetTaxInterest"            -> 100,
    "aggregateAllocatedRestrictions"     -> 0,
    "aggregateTaxEBITDA"                 -> 10,
    "aggregateAllocatedReactivations"    -> 4
  )

  val fullReturnDeemedParentModel: FullReturnModel = FullReturnModel(
    declaration = true,
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelDeemedMax),
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = groupLevelElectionsModelMax,
    ukCompanies = Seq(ukCompanyModelReactivationMax),
    angie = angie,
    returnContainsEstimates = true,
    groupEstimateReason = Some("Some reason"),
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalRestrictions = 0,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )

  val fullReturnModelMin: FullReturnModel = FullReturnModel(
    declaration = true,
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMin,
    reportingCompany = reportingCompanySameModel,
    parentCompany = None,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Original,
    revisedReturnDetails = None,
    groupLevelElections = groupLevelElectionsModelMin,
    ukCompanies = Seq(ukCompanyModelMin),
    angie = 0,
    returnContainsEstimates = true,
    groupEstimateReason = Some("Some reason"),
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalRestrictions = 0,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = None
  )

  val fullReturnJsonMin: JsObject = Json.obj(
    "declaration"                        -> true,
    "appointedReportingCompany"          -> true,
    "agentDetails"                       -> agentDetailsJsonMin,
    "reportingCompany"                   -> reportingCompanySameJson,
    "publicInfrastructure"               -> true,
    "groupCompanyDetails"                -> groupCompanyDetailsJson,
    "submissionType"                     -> Json.toJson[SubmissionType](Original),
    "ukCompanies"                        -> Seq(ukCompanyJsonMin),
    "numberOfUkCompanies"                -> 1,
    "angie"                              -> 0,
    "groupLevelElections"                -> groupLevelElectionsJsonMin,
    "returnContainsEstimates"            -> true,
    "groupEstimateReason"                -> "Some reason",
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation"                  -> 0,
    "totalRestrictions"                  -> 0,
    "groupLevelAmount"                   -> groupLevelAmountJson,
    "aggregateNetTaxInterest"            -> 50,
    "aggregateAllocatedRestrictions"     -> 0,
    "aggregateTaxEBITDA"                 -> 5,
    "aggregateAllocatedReactivations"    -> 0
  )
}

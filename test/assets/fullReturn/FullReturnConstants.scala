/*
 * Copyright 2020 HM Revenue & Customs
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
import assets.fullReturn.AdjustedGroupInterestConstants._
import assets.fullReturn.GroupLevelAmountConstants._
import assets.fullReturn.UkCompanyConstants._
import play.api.libs.json.{JsObject, Json}
import v1.models.fullReturn.FullReturnModel
import v1.models.{Original, Revised}
import assets.fullReturn.AllocatedRestrictionsConstants._
import assets.fullReturn.AllocatedReactivationsConstants._

object FullReturnConstants {

  private val oSum: Seq[BigDecimal] => Option[BigDecimal] = {
    case x if x.isEmpty => None
    case x => Some(x.sum)
  }

  val ackRef = "ackRef"
  val revisedReturnDetails = "some details"
  val angie: BigDecimal = 1.11
  val totalReactivations: BigDecimal = ukCompanyModelReactivationMax.allocatedReactivations.foldLeft[BigDecimal](0) {
    (total, company) => total + company.currentPeriodReactivation
  }

  val totalRestrictions: BigDecimal = ukCompanyModelRestrictionMax.allocatedRestrictions.foldLeft[BigDecimal](0) {
    (total, company) => total + company.totalDisallowances.getOrElse(0)
  }

  //These models do not pass validation. The ones that do start at Ultimate Parent

  val fullReturnModelMax: FullReturnModel = FullReturnModel(
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelUltUkCompany),
    publicInfrastructure = true,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = groupLevelElectionsModelMax,
    ukCompanies = Seq(ukCompanyModelMax, ukCompanyModelMin),
    angie = Some(angie),
    returnContainsEstimates = true,
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalReactivation = totalReactivations,
    totalRestrictions = 0,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )

  val fullReturnJsonMax: JsObject = Json.obj(
    "appointedReportingCompany" -> true,
    "agentDetails" -> agentDetailsJsonMax,
    "reportingCompany" -> reportingCompanyJson,
    "parentCompany" -> parentCompanyJsonUltUkCompany,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Revised,
    "revisedReturnDetails" -> revisedReturnDetails,
    "groupLevelElections" -> groupLevelElectionsJsonMax,
    "ukCompanies" -> Seq(ukCompanyJsonMax, ukCompanyJsonMin),
    "numberOfUkCompanies" -> 2,
    "aggregateNetTaxInterestIncome" -> 0,
    "aggregateTaxEBITDA" -> (2 * taxEBITDA),
    "aggregateAllocatedRestrictions" -> totalDisallowances,
    "aggregateAllocatedReactivations" -> currentPeriodReactivation,
    "angie" -> angie,
    "returnContainsEstimates" -> true,
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> fullReturnModelMax.aggregateAllocatedReactivations.getOrElse[BigDecimal](0),
    "totalRestrictions" -> 0,
    "groupLevelAmount" -> groupLevelAmountJson,
    "adjustedGroupInterest" -> adjustedGroupInterestJson
  )

  val fullReturnNetTaxExpenseModelMax: FullReturnModel = FullReturnModel(
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelUltUkCompany),
    publicInfrastructure = true,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = groupLevelElectionsModelMax,
    ukCompanies = Seq(ukCompanyModelMax, ukCompanyModelMax, ukCompanyModelMax, ukCompanyModelMin),
    angie = Some(angie),
    returnContainsEstimates = true,
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalReactivation = totalReactivations,
    totalRestrictions = 0,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )

  val fullReturnNetTaxExpenseJsonMax: JsObject = Json.obj(
    "appointedReportingCompany" -> true,
    "agentDetails" -> agentDetailsJsonMax,
    "reportingCompany" -> reportingCompanyJson,
    "parentCompany" -> parentCompanyJsonUltUkCompany,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Revised,
    "revisedReturnDetails" -> revisedReturnDetails,
    "groupLevelElections" -> groupLevelElectionsJsonMax,
    "ukCompanies" -> Seq(ukCompanyJsonMax, ukCompanyJsonMax, ukCompanyJsonMax, ukCompanyJsonMin),
    "numberOfUkCompanies" -> 4,
    "aggregateNetTaxInterestExpense" -> ((3 * netTaxInterestExpense) - netTaxInterestIncome),
    "aggregateTaxEBITDA" -> (4 * taxEBITDA),
    "aggregateAllocatedRestrictions" -> (3 * totalDisallowances),
    "aggregateAllocatedReactivations" -> (3 * currentPeriodReactivation),
    "angie" -> angie,
    "returnContainsEstimates" -> true,
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> totalReactivations,
    "totalRestrictions" -> 0,
    "groupLevelAmount" -> groupLevelAmountJson,
    "adjustedGroupInterest" -> adjustedGroupInterestJson
  )

  val fullReturnNetTaxIncomeModelMax: FullReturnModel = FullReturnModel(
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelUltUkCompany),
    publicInfrastructure = true,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = groupLevelElectionsModelMax,
    ukCompanies = Seq(ukCompanyModelMin, ukCompanyModelMin, ukCompanyModelMin, ukCompanyModelMax),
    angie = Some(angie),
    returnContainsEstimates = true,
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalReactivation = totalReactivations,
    totalRestrictions = 0,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )

  val fullReturnNetTaxIncomeJsonMax: JsObject = Json.obj(
    "appointedReportingCompany" -> true,
    "agentDetails" -> agentDetailsJsonMax,
    "reportingCompany" -> reportingCompanyJson,
    "parentCompany" -> parentCompanyJsonUltUkCompany,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Revised,
    "revisedReturnDetails" -> revisedReturnDetails,
    "groupLevelElections" -> groupLevelElectionsJsonMax,
    "ukCompanies" -> Seq(ukCompanyJsonMin, ukCompanyJsonMin, ukCompanyJsonMin, ukCompanyJsonMax),
    "numberOfUkCompanies" -> 4,
    "aggregateNetTaxInterestIncome" -> ((3 * netTaxInterestIncome) - netTaxInterestExpense),
    "aggregateTaxEBITDA" -> (4 * taxEBITDA),
    "aggregateAllocatedRestrictions" -> totalDisallowances,
    "aggregateAllocatedReactivations" -> currentPeriodReactivation,
    "angie" -> angie,
    "returnContainsEstimates" -> true,
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> totalReactivations,
    "totalRestrictions" -> 0,
    "groupLevelAmount" -> groupLevelAmountJson,
    "adjustedGroupInterest" -> adjustedGroupInterestJson
  )

  // Ultimate Parent Model and Json.

  val fullReturnUltimateParentModel: FullReturnModel = FullReturnModel(
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelUltUkCompany),
    publicInfrastructure = true,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = groupLevelElectionsModelMax,
    ukCompanies = Seq(ukCompanyModelReactivationMax, ukCompanyModelReactivationMax), // Net Tax Income =  2 * £30 = £60.00
    angie = Some(angie),
    returnContainsEstimates = true,
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalReactivation = totalReactivations * 2,
    totalRestrictions = 0,
    groupLevelAmount = groupLevelAmountModel,  //Reactivation Capactiy = £300
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )

  val fullReturnUltimateParentJson: JsObject = Json.obj(
    "appointedReportingCompany" -> true,
    "agentDetails" -> agentDetailsJsonMax,
    "reportingCompany" -> reportingCompanyJson,
    "parentCompany" -> parentCompanyJsonUltUkCompany,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Revised,
    "revisedReturnDetails" -> revisedReturnDetails,
    "groupLevelElections" -> groupLevelElectionsJsonMax,
    "ukCompanies" -> Seq(ukCompanyReactivationJsonMax, ukCompanyReactivationJsonMax),
    "numberOfUkCompanies" -> 2,
    "aggregateNetTaxInterestExpense" -> netTaxInterestExpense,
    "aggregateTaxEBITDA" -> taxEBITDA,
    "aggregateAllocatedRestrictions" -> totalDisallowances,
    "aggregateAllocatedReactivations" -> currentPeriodReactivation,
    "angie" -> angie,
    "returnContainsEstimates" -> true,
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> totalReactivations * 2,
    "totalRestrictions" -> 0,
    "groupLevelAmount" -> groupLevelAmountJson,
    "adjustedGroupInterest" -> adjustedGroupInterestJson
  )

  // Deemed Model and Json
  val fullReturnDeemedParentModel: FullReturnModel = FullReturnModel(
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelDeemedMax),
    publicInfrastructure = true,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = groupLevelElectionsModelMax,
    ukCompanies = Seq(ukCompanyModelReactivationMax),
    angie = Some(angie),
    returnContainsEstimates = true,
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalReactivation = totalReactivations,
    totalRestrictions = 0,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )

  // Minimum Model and Json

  val fullReturnModelMin: FullReturnModel = FullReturnModel(
    appointedReportingCompany = true,
    agentDetails = agentDetailsModelMin,
    reportingCompany = reportingCompanyModel,
    parentCompany = None,
    publicInfrastructure = true,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Original,
    revisedReturnDetails = None,
    groupLevelElections = groupLevelElectionsModelMin,
    ukCompanies = Seq(ukCompanyModelMin),
    angie = None,
    returnContainsEstimates = true,
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalReactivation = totalReactivations,
    totalRestrictions = 0,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = None
  )

  val fullReturnJsonMin: JsObject = Json.obj(
    "appointedReportingCompany" -> true,
    "agentDetails" -> agentDetailsJsonMin,
    "reportingCompany" -> reportingCompanyJson,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Original,
    "ukCompanies" -> Seq(ukCompanyJsonMin),
    "numberOfUkCompanies" -> 1,
    "aggregateNetTaxInterestIncome" -> netTaxInterestIncome,
    "aggregateTaxEBITDA" -> taxEBITDA,
    "groupLevelElections" -> groupLevelElectionsJsonMin,
    "returnContainsEstimates" -> true,
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> totalReactivations,
    "totalRestrictions" -> 0,
    "groupLevelAmount" -> groupLevelAmountJson
  )
}

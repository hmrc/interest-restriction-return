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

object FullReturnConstants {

  val ackRef = "ackRef"
  val revisedReturnDetails = "some details"
  val angie: BigDecimal = 1.11
  val totalReactivations: BigDecimal = ukCompanyModelReactivationMax.allocatedReactivations.foldLeft[BigDecimal](0) {
    (total, company) => total + company.currentPeriodReactivation
  }

  val numberOfUkCompaniesMax: Int = 3
  val numberOfUkCompaniesMin: Int = 1
  val aggregateNetTaxInterestIncome: BigDecimal = 0
  val aggregateNetTaxInterestExpense: BigDecimal = 3.33
  val aggregateNetTaxInterestExpenseMin: BigDecimal = 1.11
  val aggregateTaxEBITDAMax: BigDecimal = 9.99
  val aggregateTaxEBITDAMin: BigDecimal = 3.33
  val aggregateAllocatedRestrictions: Option[BigDecimal] = Some(19.98)
  val aggregateAllocatedReactivations: Option[BigDecimal] = Some(6.66)

  val fullReturnModelMax: FullReturnModel = FullReturnModel(
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelUltUkCompany),
    publicInfrastructure = true,
    groupCompanyDetails = groupCompanyDetailsModel,
    submissionType = Revised,
    revisedReturnDetails = Some(revisedReturnDetails),
    groupLevelElections = groupLevelElectionsModelMax,
    ukCompanies = Seq(ukCompanyModelMax, ukCompanyModelMax, ukCompanyModelMax),
    angie = Some(angie),
    returnContainsEstimates = true,
    groupSubjectToInterestRestrictions = false,
    groupSubjectToInterestReactivation = true,
    totalReactivation = totalReactivations,
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )

  val fullReturnJsonMax: JsObject = Json.obj(
    "agentDetails" -> agentDetailsJsonMax,
    "reportingCompany" -> reportingCompanyJson,
    "parentCompany" -> parentCompanyJsonUltUkCompany,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Revised,
    "revisedReturnDetails" -> revisedReturnDetails,
    "groupLevelElections" -> groupLevelElectionsJsonMax,
    "ukCompanies" -> Seq(ukCompanyJsonMax, ukCompanyJsonMax, ukCompanyJsonMax),
    "numberOfUkCompanies" -> numberOfUkCompaniesMax,
    "aggregateNetTaxInterestIncome" -> aggregateNetTaxInterestIncome,
    "aggregateNetTaxInterestExpense" -> aggregateNetTaxInterestExpense,
    "aggregateTaxEBITDA" -> aggregateTaxEBITDAMax,
    "aggregateAllocatedRestrictions" -> aggregateAllocatedRestrictions,
    "aggregateAllocatedReactivations" -> aggregateAllocatedReactivations,
    "angie" -> angie,
    "returnContainsEstimates" -> true,
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> totalReactivations,
    "groupLevelAmount" -> groupLevelAmountJson,
    "adjustedGroupInterest" -> adjustedGroupInterestJson
  )

  // Ultimate Model and Json

  val fullReturnUltimateParentModel: FullReturnModel = FullReturnModel(
    agentDetails = agentDetailsModelMax,
    reportingCompany = reportingCompanyModel,
    parentCompany = Some(parentCompanyModelUltUkCompany),
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
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )

  val fullReturnUltimateParentJson: JsObject = Json.obj(
    "agentDetails" -> agentDetailsJsonMax,
    "reportingCompany" -> reportingCompanyJson,
    "parentCompany" -> parentCompanyJsonUltUkCompany,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Revised,
    "revisedReturnDetails" -> revisedReturnDetails,
    "groupLevelElections" -> groupLevelElectionsJsonMax,
    "ukCompanies" -> Seq(ukCompanyReactivationJsonMax),
    "numberOfUkCompanies" -> numberOfUkCompaniesMax,
    "aggregateNetTaxInterestIncome" -> aggregateNetTaxInterestIncome,
    "aggregateNetTaxInterestExpense" -> aggregateNetTaxInterestExpense,
    "aggregateTaxEBITDA" -> aggregateTaxEBITDAMax,
    "aggregateAllocatedRestrictions" -> aggregateAllocatedRestrictions,
    "aggregateAllocatedReactivations" -> aggregateAllocatedReactivations,
    "angie" -> angie,
    "returnContainsEstimates" -> true,
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> totalReactivations,
    "groupLevelAmount" -> groupLevelAmountJson,
    "adjustedGroupInterest" -> adjustedGroupInterestJson
  )

  // Deemed Model and Json
  val fullReturnDeemedParentModel: FullReturnModel = FullReturnModel(
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
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = Some(adjustedGroupInterestModel)
  )


  // Minimum Model and Json

  val fullReturnModelMin: FullReturnModel = FullReturnModel(
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
    groupLevelAmount = groupLevelAmountModel,
    adjustedGroupInterest = None
  )

  val fullReturnJsonMin: JsObject = Json.obj(
    "agentDetails" -> agentDetailsJsonMin,
    "reportingCompany" -> reportingCompanyJson,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Original,
    "ukCompanies" -> Seq(ukCompanyJsonMin),
    "numberOfUkCompanies" -> 1,
    "aggregateNetTaxInterestExpense" -> aggregateNetTaxInterestExpenseMin,
    "aggregateNetTaxInterestIncome" -> aggregateNetTaxInterestIncome,
    "aggregateTaxEBITDA" -> aggregateTaxEBITDAMin,
    "groupLevelElections" -> groupLevelElectionsJsonMin,
    "returnContainsEstimates" -> true,
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> totalReactivations,
    "groupLevelAmount" -> groupLevelAmountJson
  )
}

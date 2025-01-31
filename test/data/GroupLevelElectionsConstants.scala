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

package data

import data.ConsolidatedPartnershipConstants.*
import data.GroupRatioConstants.*
import data.NonConsolidatedInvestmentElectionConstants.*
import play.api.libs.json.{JsObject, Json}
import v1.models.GroupLevelElectionsModel

object GroupLevelElectionsConstants {

  val groupLevelElectionsJsonMax: JsObject = Json.obj(
    "groupRatio"                                    -> groupRatioJsonMax,
    "interestAllowanceAlternativeCalculation"       -> true,
    "interestAllowanceNonConsolidatedInvestment"    -> nonConsolidatedInvestmentJsonMax,
    "interestAllowanceConsolidatedPartnership"      -> consolidatedPartnershipsJsonMax,
    "activeInterestAllowanceAlternativeCalculation" -> true
  )

  val groupLevelElectionsModelMax: GroupLevelElectionsModel = GroupLevelElectionsModel(
    groupRatio = groupRatioModelMax,
    interestAllowanceAlternativeCalculation = true,
    interestAllowanceNonConsolidatedInvestment = nonConsolidatedInvestmentModelMax,
    interestAllowanceConsolidatedPartnership = consolidatedPartnershipsModelMax,
    activeInterestAllowanceAlternativeCalculation = true
  )

  val groupLevelElectionsJsonMin: JsObject = Json.obj(
    "groupRatio"                                    -> groupRatioJsonMin,
    "interestAllowanceAlternativeCalculation"       -> true,
    "interestAllowanceNonConsolidatedInvestment"    -> nonConsolidatedInvestmentJsonMin,
    "interestAllowanceConsolidatedPartnership"      -> consolidatedPartnershipsJsonMin,
    "activeInterestAllowanceAlternativeCalculation" -> true
  )

  val groupLevelElectionsModelMin: GroupLevelElectionsModel = GroupLevelElectionsModel(
    groupRatio = groupRatioModelMin,
    interestAllowanceAlternativeCalculation = true,
    interestAllowanceNonConsolidatedInvestment = nonConsolidatedInvestmentModelMin,
    interestAllowanceConsolidatedPartnership = consolidatedPartnershipsModelMin,
    activeInterestAllowanceAlternativeCalculation = true
  )
}

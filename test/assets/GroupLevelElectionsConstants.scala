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

import assets.ConsolidatedPartnershipConstants._
import assets.GroupRatioBlendedConstants._
import assets.NonConsolidatedInvestmentConstants._
import models.GroupLevelElectionsModel
import play.api.libs.json.Json

object GroupLevelElectionsConstants {

  val groupLevelElectionsJsonMax = Json.obj(
    "isElected" -> true,
    "groupRatioBlended" -> groupRatioBlendedJsonMax,
    "groupEBITDAChargeableGains" -> true,
    "interestAllowanceAlternativeCalculation" -> true,
    "interestAllowanceNonConsolidatedInvestment" -> nonConsolidatedInvestmentJsonMax,
    "interestAllowanceConsolidatedPartnership" -> consolidatedPartnershipsJsonMax
  )

  val groupLevelElectionsModelMax = GroupLevelElectionsModel(
    isElected = true,
    groupRatioBlended = Some(groupRatioBlendedModelMax),
    groupEBITDAChargeableGains = Some(true),
    interestAllowanceAlternativeCalculation = Some(true),
    interestAllowanceNonConsolidatedInvestment = Some(nonConsolidatedInvestmentModelMax),
    interestAllowanceConsolidatedPartnership = Some(consolidatedPartnershipsModelMax)
  )

  val groupLevelElectionsJsonMin = Json.obj(
    "isElected" -> true,
    "groupRatioBlended" -> groupRatioBlendedJsonMax
  )

  val groupLevelElectionsModelMin = GroupLevelElectionsModel(
    isElected = true,
    groupRatioBlended = Some(groupRatioBlendedModelMax),
    groupEBITDAChargeableGains = None,
    interestAllowanceAlternativeCalculation = None,
    interestAllowanceNonConsolidatedInvestment = None,
    interestAllowanceConsolidatedPartnership = None
  )
}

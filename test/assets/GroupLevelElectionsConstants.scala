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

import assets.GroupRatioBlendedConstants._
import assets.NonConsolidatedInvestmentConstants._
import assets.ConsolidatedPartnershipConstants._
import models.{Elect, GroupLevelElectionsModel}
import play.api.libs.json.Json

object GroupLevelElectionsConstants {

  val groupLevelElectionsJsonMax = Json.obj(
    "groupRatioElection" -> Elect,
    "groupRatioBlended" -> groupRatioBlendedModelMax,
    "groupEBITDAChargeableGains" -> true,
    "interestAllowanceAlternativeCalculation" -> true,
    "interestAllowanceNonConsolidatedInvestment" -> nonConsolidatedInvestmentModelMax,
    "interestAllowanceConsolidatedPartnership" -> consolidatedPartnershipsModelMax
  )

  val groupLevelElectionsModelMax = GroupLevelElectionsModel(
    groupRatioElection = Elect,
    groupRatioBlended = Some(groupRatioBlendedModelMax),
    groupEBITDAChargeableGains = Some(true),
    interestAllowanceAlternativeCalculation = Some(true),
    interestAllowanceNonConsolidatedInvestment = Some(nonConsolidatedInvestmentModelMax),
    interestAllowanceConsolidatedPartnership = Some(consolidatedPartnershipsModelMax)
  )

  val groupLevelElectionsJsonMin = Json.obj(
    "groupRatioElection" -> Elect,
    "groupRatioBlended" -> groupRatioBlendedModelMax
  )

  val groupLevelElectionsModelMin = GroupLevelElectionsModel(
    groupRatioElection = Elect,
    groupRatioBlended = Some(groupRatioBlendedModelMax),
    groupEBITDAChargeableGains = None,
    interestAllowanceAlternativeCalculation = None,
    interestAllowanceNonConsolidatedInvestment = None,
    interestAllowanceConsolidatedPartnership = None
  )
}

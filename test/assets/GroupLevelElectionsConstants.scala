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

import models.abbreviatedReturn.GroupLevelElectionsModel
import assets.GroupRatioBlendedConstants._
import assets.NonConsolidatedInvestmentConstants._
import assets.ConsolidatedPartnershipConstants._
import models.Elect
import play.api.libs.json.Json

object GroupLevelElectionsConstants {

  val groupLevelElectionsJson = Json.obj(
    "groupRatioElection" -> Elect,
    "groupRatioBlended" -> groupRatioBlendedModel,
    "groupEBITDAChargeableGains" -> true,
    "interestAllowanceAlternativeCalculation" -> true,
    "interestAllowanceNonConsolidatedInvestment" -> nonConsolidatedInvestmentModel,
    "interestAllowanceConsolidatedPartnership" -> consolidatedPartnershipsModel
  )

  val groupLevelElectionsModel = GroupLevelElectionsModel(
    groupRatioElection = Elect,
    groupRatioBlended = Some(groupRatioBlendedModel),
    groupEBITDAChargeableGains = Some(true),
    interestAllowanceAlternativeCalculation = Some(true),
    interestAllowanceNonConsolidatedInvestment = Some(nonConsolidatedInvestmentModel),
    interestAllowanceConsolidatedPartnership = Some(consolidatedPartnershipsModel)
  )
}

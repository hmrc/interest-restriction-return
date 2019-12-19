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

package validation

import assets.ConsolidatedPartnershipConstants.consolidatedPartnershipsModelMax
import assets.GroupLevelElectionsConstants._
import assets.GroupRatioBlendedConstants.groupRatioBlendedModelMax
import assets.NonConsolidatedInvestmentElectionConstants.nonConsolidatedInvestmentModelMax
import play.api.libs.json.JsPath

class GroupLevelElectionValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Group Level Election Validation" when {
    "Return Valid" when {

      "Group Level Election isElected true other elections are optional" in {
        val model = groupLevelElectionsModelMax.copy(isElected = true,
          groupRatioBlended = Some(groupRatioBlendedModelMax),
          groupEBITDAChargeableGains = Some(true),
          interestAllowanceAlternativeCalculation = Some(true),
          interestAllowanceNonConsolidatedInvestment = Some(nonConsolidatedInvestmentModelMax),
          interestAllowanceConsolidatedPartnership = Some(consolidatedPartnershipsModelMax))

        rightSide(model.validateGroupLevelElectionModel) shouldBe model
      }

      "Group Level Election isElected true some elections are optional" in {
        val model = groupLevelElectionsModelMax.copy(isElected = true,
          groupRatioBlended = None,
          groupEBITDAChargeableGains = Some(true),
          interestAllowanceAlternativeCalculation = Some(true),
          interestAllowanceNonConsolidatedInvestment = Some(nonConsolidatedInvestmentModelMax),
          interestAllowanceConsolidatedPartnership = Some(consolidatedPartnershipsModelMax))

        rightSide(model.validateGroupLevelElectionModel) shouldBe model
      }

      "Group Level Election isElected true all elections are not made" in {
        val model = groupLevelElectionsModelMax.copy(isElected = true,
          groupRatioBlended = None,
          groupEBITDAChargeableGains = Some(true),
          interestAllowanceAlternativeCalculation = Some(true),
          interestAllowanceNonConsolidatedInvestment = None,
          interestAllowanceConsolidatedPartnership = None)

        rightSide(model.validateGroupLevelElectionModel) shouldBe model
      }
    }
  }
}

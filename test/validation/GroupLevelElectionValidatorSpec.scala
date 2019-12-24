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

import assets.GroupLevelElectionsConstants._
import assets.GroupRatioConstants._
import assets.NonConsolidatedInvestmentElectionConstants._
import assets.ConsolidatedPartnershipConstants._
import models.{NonConsolidatedInvestmentModel, PartnershipModel}
import play.api.libs.json.JsPath

class GroupLevelElectionValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Group Level Election Validation" when {

    "Return Valid" when {

      "Valid Elections model is supplied" in {
        rightSide(groupLevelElectionsModel.validate) shouldBe groupLevelElectionsModel
      }
    }


    "Return Invalid" when {

      "groupRatio has errors" in {
        val model = groupLevelElectionsModel.copy(
          groupRatio = groupRatioModelMax.copy(
            isElected = false,
            groupRatioBlended = None,
            groupEBITDAChargeableGains = Some(true)
          )
        )

        leftSideError(model.validate).errorMessage shouldBe GroupEBITDASupplied(model.groupRatio.groupEBITDAChargeableGains).errorMessage
      }

      "interestAllowanceNonConsolidatedInvestment has errors" in {
        val model = groupLevelElectionsModel.copy(
          interestAllowanceNonConsolidatedInvestment = nonConsolidatedInvestmentModelMax.copy(
            isElected = true,
            nonConsolidatedInvestments = Some(Seq(NonConsolidatedInvestmentModel("")))
          )
        )

        leftSideError(model.validate).errorMessage shouldBe NonConsolidatedInvestmentNameError("").errorMessage
      }

      "interestAllowanceConsolidatedPartnership has errors" in {
        val model = groupLevelElectionsModel.copy(
          interestAllowanceConsolidatedPartnership = consolidatedPartnershipsModelMax.copy(
            isElected = true,
            consolidatedPartnerships = Some(Seq(PartnershipModel("")))
          )
        )

        leftSideError(model.validate).errorMessage shouldBe PartnershipNameError("").errorMessage
      }
    }
  }
}

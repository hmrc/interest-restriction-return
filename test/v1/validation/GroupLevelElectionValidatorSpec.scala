/*
 * Copyright 2025 HM Revenue & Customs
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

package v1.validation

import data.ConsolidatedPartnershipConstants.*
import data.GroupLevelElectionsConstants.*
import data.GroupRatioConstants.*
import data.NonConsolidatedInvestmentElectionConstants.*
import play.api.libs.json.JsPath
import v1.models.{CompanyNameModel, NonConsolidatedInvestmentModel, PartnershipModel, UTRModel}

class GroupLevelElectionValidatorSpec extends BaseValidationSpec {

  private val sautrFake: UTRModel = UTRModel("1234567890")

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "GroupLevelElectionValidator" should {

    "Return Valid" when {

      "Valid Elections model is supplied" in {
        rightSide(groupLevelElectionsModelMax.validate) shouldBe groupLevelElectionsModelMax
      }

      "groupRatio validation doesn't flag errors" in {
        val model = groupLevelElectionsModelMax.copy(
          groupRatio = groupRatioModelMax.copy(
            isElected = false,
            groupRatioBlended = None,
            groupEBITDAChargeableGains = true
          )
        )

        rightSide(model.validate) shouldBe groupLevelElectionsModelMax.copy(
          groupRatio =
            groupRatioModelMax.copy(isElected = false, groupRatioBlended = None, groupEBITDAChargeableGains = true)
        )
      }
    }

    "Return Invalid" when {

      "interestAllowanceNonConsolidatedInvestment has errors" in {
        val model = groupLevelElectionsModelMax.copy(
          interestAllowanceNonConsolidatedInvestment = nonConsolidatedInvestmentModelMax.copy(
            isElected = true,
            nonConsolidatedInvestments = Some(Seq(NonConsolidatedInvestmentModel("")))
          )
        )

        leftSideError(model.validate).errorMessage shouldBe NonConsolidatedInvestmentNameLengthError("").errorMessage
      }

      "interestAllowanceConsolidatedPartnership has errors" in {
        val model = groupLevelElectionsModelMax.copy(
          interestAllowanceConsolidatedPartnership = consolidatedPartnershipsModelMax.copy(
            isElected = true,
            consolidatedPartnerships =
              Some(Seq(PartnershipModel(partnershipName = CompanyNameModel(""), sautr = Some(sautrFake))))
          )
        )

        leftSideError(model.validate).errorMessage shouldBe CompanyNameLengthError("").errorMessage
      }
    }
  }
}

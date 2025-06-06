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

package v1.models

import data.GroupLevelElectionsConstants.*
import play.api.libs.json.{JsError, Json}
import data.NonConsolidatedInvestmentElectionConstants.*
import utils.BaseSpec

class GroupLevelElectionsModelSpec extends BaseSpec {

  "GroupLevelElectionsModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = groupLevelElectionsJsonMax
        val actualValue   = Json.toJson(groupLevelElectionsModelMax)

        actualValue shouldBe expectedValue
      }

      "All data is included in the Json.toJson" in {

        val expectedValue = Json.obj(
          "groupRatio"                                    -> Json.obj(
            "isElected"                        -> false,
            "groupEBITDAChargeableGains"       -> false,
            "activeGroupEBITDAChargeableGains" -> false
          ),
          "interestAllowanceAlternativeCalculation"       -> true,
          "activeInterestAllowanceAlternativeCalculation" -> true,
          "interestAllowanceNonConsolidatedInvestment"    -> nonConsolidatedInvestmentJsonMin,
          "interestAllowanceConsolidatedPartnership"      -> Json.obj(
            "isElected" -> false,
            "isActive"  -> false
          )
        )

        val actualValue = Json.toJson(groupLevelElectionsModelMin)
        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = groupLevelElectionsModelMax
        val actualValue   = groupLevelElectionsJsonMax.as[GroupLevelElectionsModel]

        actualValue shouldBe expectedValue
      }
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[GroupLevelElectionsModel] shouldBe a[JsError]
      }
      "empty json" in {
        Json.obj().validate[GroupLevelElectionsModel] shouldBe a[JsError]
      }
    }
  }
}

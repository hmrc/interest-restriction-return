/*
 * Copyright 2021 HM Revenue & Customs
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

package v1.schemas.subSchemas

import play.api.libs.json.{JsValue, Json}
import v1.schemas.BaseSchemaSpec
import v1.schemas.helpers.fullReturn.GroupLevelAmount

class GroupLevelAmountSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/groupLevelAmount.json", "1.0", json)

  "groupLevelAmount" should {

    "Return valid" when {

      "valid JSON is received" in {
        validate(Json.toJson(GroupLevelAmount())) shouldBe true
      }

      "Return invalid" when {

        "interestReactivationCap" when {

          "is blank" in {
            val json = Json.toJson(GroupLevelAmount(
              interestReactivationCap = None
            ))
            validate(json) shouldBe false
          }

          "is a negative number" in {
            val json = Json.toJson(GroupLevelAmount(
              interestReactivationCap = Some(-1)
            ))
            validate(json) shouldBe false
          }
        }

        "interestAllowanceBroughtForward" when {

          "is blank" in {
            val json = Json.toJson(GroupLevelAmount(
              interestAllowanceBroughtForward = None
            ))
            validate(json) shouldBe false
          }

          "is a negative number" in {
            val json = Json.toJson(GroupLevelAmount(
              interestAllowanceBroughtForward = Some(-1)
            ))
            validate(json) shouldBe false
          }
        }

        "interestAllowanceForPeriod" when {

          "is blank" in {
            val json = Json.toJson(GroupLevelAmount(
              interestAllowanceForPeriod = None
            ))
            validate(json) shouldBe false
          }

          "is a negative number" in {
            val json = Json.toJson(GroupLevelAmount(
              interestAllowanceForPeriod = Some(-1)
            ))
            validate(json) shouldBe false
          }
        }

        "interestCapacityForPeriod" when {

          "is blank" in {
            val json = Json.toJson(GroupLevelAmount(
              interestCapacityForPeriod = None
            ))
            validate(json) shouldBe false
          }

          "is a negative number" in {
            val json = Json.toJson(GroupLevelAmount(
              interestCapacityForPeriod = Some(-1)
            ))
            validate(json) shouldBe false
          }
        }
      }
    }
  }
}
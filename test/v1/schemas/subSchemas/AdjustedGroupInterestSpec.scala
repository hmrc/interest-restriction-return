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

package v1.schemas.subSchemas

import play.api.libs.json.{JsValue, Json}
import v1.schemas.BaseSchemaSpec
import v1.schemas.helpers.fullReturn.AdjustedGroupInterest

class AdjustedGroupInterestSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/adjustedGroupInterest.json", "1.0", json)

  "adjustedGroupInterest" should {

    "Return valid" when {

      "valid JSON is received" in {
        validate(Json.toJson(AdjustedGroupInterest())) shouldBe true
      }

      "Return invalid" when {

        "QNGIE" when {

          "is blank" in {
            val json = Json.toJson(AdjustedGroupInterest(
              qngie = None
            ))
            validate(json) shouldBe false
          }

          "is a negative number" in {
            val json = Json.toJson(AdjustedGroupInterest(
              qngie = Some(-1)
            ))
            validate(json) shouldBe false
          }
        }

        "groupEBITDA" when {

          "is blank" in {
            val json = Json.toJson(AdjustedGroupInterest(
              groupEBITDA = None
            ))
            validate(json) shouldBe false
          }
        }

        "groupRatio" when {

          "is blank" in {
            val json = Json.toJson(AdjustedGroupInterest(
              groupEBITDA = None
            ))
            validate(json) shouldBe false
          }

          "is a negative number" in {
            val json = Json.toJson(AdjustedGroupInterest(
              groupRatio = Some(-1)
            ))
            validate(json) shouldBe false
          }

          "is above 100" in {
            val json = Json.toJson(AdjustedGroupInterest(
              groupRatio = Some(100.1)
            ))
            validate(json) shouldBe false
          }
        }
      }
    }
  }
}
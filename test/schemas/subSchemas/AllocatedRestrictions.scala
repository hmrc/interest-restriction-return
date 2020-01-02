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

package schemas.subSchemas

import play.api.libs.json.{JsValue, Json}
import schemas.BaseSchemaSpec
import schemas.helpers.fullReturn.AllocatedRestrictions

class AllocatedRestrictions extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/allocatedRestrictions.json", json)

  "allocatedRestrictions" should {

    "Return valid" when {

      "valid JSON is received" in {

        validate(Json.toJson(AllocatedRestrictions())) shouldBe true

      }
    }

    "Return invalid" when {

      "disallowanceAp1" when {

        "is a negative number" in {

          val json = Json.toJson(AllocatedRestrictions(
            disallowanceAp1 = Some(-1)
          ))

          validate(json) shouldBe false
        }
      }

      "disallowanceAp2" when {

        "is a negative number" in {

          val json = Json.toJson(AllocatedRestrictions(
            disallowanceAp2 = Some(-1)
          ))

          validate(json) shouldBe false
        }
      }

      "disallowanceAp3" when {

        "is a negative number" in {

          val json = Json.toJson(AllocatedRestrictions(
            disallowanceAp3 = Some(-1)
          ))

          validate(json) shouldBe false
        }
      }

      "Total Disallowances" when {

        "is a negative number" in {

          val json = Json.toJson(AllocatedRestrictions(
            totalDisallowances = Some(-1)
          ))

          validate(json) shouldBe false
        }

        "is a None" in {

          val json = Json.toJson(AllocatedRestrictions(
            totalDisallowances = None
          ))

          validate(json) shouldBe false
        }
      }

      "AP1 Date is None" in {

        val json = Json.toJson(AllocatedRestrictions(
          ap1EndDate = None
        ))

        validate(json) shouldBe false
      }
    }
  }
}

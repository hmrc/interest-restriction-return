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
import v1.schemas.helpers.fullReturn.AllocatedReactivations

class AllocatedReactivations extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/allocatedReactivations.json", "1.0", json)

  "allocatedReactivations" should {

    "Return valid" when {

      "valid JSON is received" in {
        validate(Json.toJson(AllocatedReactivations())) shouldBe true
      }
    }

    "Return invalid" when {

      "currentPeriodReactivation" when {

        "is None" in {

          val json = Json.toJson(AllocatedReactivations(
            currentPeriodReactivation = None
          ))

          validate(json) shouldBe false
        }

        "is less than 0" in {

          val json = Json.toJson(AllocatedReactivations(
            currentPeriodReactivation = Some(-0.01)
          ))

          validate(json) shouldBe false
        }
      }
    }
  }
}

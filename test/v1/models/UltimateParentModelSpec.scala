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

import data.UltimateParentConstants.*
import play.api.libs.json.{JsError, Json}
import utils.BaseSpec

class UltimateParentModelSpec extends BaseSpec {

  "UltimateParentModel" when {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = ultimateParentJsonMax
        val actualValue   = Json.toJson(ultimateParentModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = ultimateParentJsonMin
        val actualValue   = Json.toJson(ultimateParentModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = ultimateParentModelMax
        val actualValue   = ultimateParentJsonMax.as[UltimateParentModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = ultimateParentModelMin
        val actualValue   = ultimateParentJsonMin.as[UltimateParentModel]

        actualValue shouldBe expectedValue
      }
    }

    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[UltimateParentModel] shouldBe a[JsError]
      }
      "empty json" in {
        Json.obj().validate[UltimateParentModel] shouldBe a[JsError]
      }
    }
  }
}

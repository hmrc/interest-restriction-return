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

package v1.models.fullReturn

import play.api.libs.json.{JsError, Json}
import data.fullReturn.AdjustedGroupInterestConstants.*
import utils.BaseSpec

class AdjustedGroupInterestModelSpec extends BaseSpec {

  "AdjustedGroupInterestModel" must {

    "correctly write to json" when {
      "groupEbitda is present" in {
        val expectedValue = adjustedGroupInterestJson
        val actualValue   = Json.toJson(adjustedGroupInterestModel)

        actualValue shouldBe expectedValue
      }

      "groupEbitda is not present" in {
        val expectedValue = adjustedGroupInterestJsonNoEbitda
        val actualValue   = Json.toJson(adjustedGroupInterestModelNoEbitda)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" in {

      val expectedValue = adjustedGroupInterestModel
      val actualValue   = adjustedGroupInterestJson.as[AdjustedGroupInterestModel]

      actualValue shouldBe expectedValue
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[AdjustedGroupInterestModel] shouldBe a[JsError]
      }
      "empty json" in {
        Json.obj().validate[AdjustedGroupInterestModel] shouldBe a[JsError]
      }
    }
  }
}

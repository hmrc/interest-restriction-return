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

import data.GroupRatioConstants.*
import play.api.libs.json.{JsError, Json}
import utils.BaseSpec

class GroupRatioModelSpec extends BaseSpec {

  "GroupRatioModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = groupRatioJsonMax
        val actualValue   = Json.toJson(groupRatioModelMax)

        actualValue shouldBe expectedValue
      }

      "All data is included in the Json.toJson" in {

        val expectedValue = Json.obj(
          "isElected"                        -> false,
          "groupEBITDAChargeableGains"       -> false,
          "activeGroupEBITDAChargeableGains" -> false
        )

        val actualValue = Json.toJson(groupRatioModelMin)
        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {
      "max values given" in {

        val expectedValue = groupRatioModelMax
        val actualValue   = groupRatioJsonMax.as[GroupRatioModel]

        actualValue shouldBe expectedValue
      }
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[GroupRatioModel] shouldBe a[JsError]
      }
      "empty json" in {
        Json.obj().validate[GroupRatioModel] shouldBe a[JsError]
      }
    }
  }
}

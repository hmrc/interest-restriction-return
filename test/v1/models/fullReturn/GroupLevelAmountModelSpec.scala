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

import data.fullReturn.GroupLevelAmountConstants.*
import play.api.libs.json.{JsError, Json}
import utils.BaseSpec

class GroupLevelAmountModelSpec extends BaseSpec {

  "GroupLevelAmountModel" must {

    "correctly write to json" in {

      val expectedValue = groupLevelAmountJson
      val actualValue   = Json.toJson(groupLevelAmountModel)

      actualValue shouldBe expectedValue
    }

    "correctly read from Json" in {

      val expectedValue = groupLevelAmountModel
      val actualValue   = groupLevelAmountJson.as[GroupLevelAmountModel]

      actualValue shouldBe expectedValue
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[GroupLevelAmountModel] shouldBe a[JsError]
      }
      "empty json" in {
        Json.obj().validate[GroupLevelAmountModel] shouldBe a[JsError]
      }
    }
  }
}

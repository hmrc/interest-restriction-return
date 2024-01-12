/*
 * Copyright 2024 HM Revenue & Customs
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

import data.GroupRatioBlendedConstants._
import play.api.libs.json.Json
import utils.BaseSpec

class GroupRatioBlendedModelSpec extends BaseSpec {

  "GroupRatioBlendedModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = groupRatioBlendedJsonMax
        val actualValue   = Json.toJson(groupRatioBlendedModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = groupRatioBlendedJsonMin
        val actualValue   = Json.toJson(groupRatioBlendedModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = groupRatioBlendedModelMax
        val actualValue   = groupRatioBlendedJsonMax.as[GroupRatioBlendedModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = groupRatioBlendedModelMin
        val actualValue   = groupRatioBlendedJsonMin.as[GroupRatioBlendedModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}

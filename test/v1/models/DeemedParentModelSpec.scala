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

import data.DeemedParentConstants._
import play.api.libs.json.Json
import v1.models.DeemedParentModel
import utils.BaseSpec

class DeemedParentModelSpec extends BaseSpec {

  "DeemedParentModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = deemedParentJsonMax
        val actualValue   = Json.toJson(deemedParentModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = deemedParentJsonMin
        val actualValue   = Json.toJson(deemedParentModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = deemedParentModelMax
        val actualValue   = deemedParentJsonMax.as[DeemedParentModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = deemedParentModelMin
        val actualValue   = deemedParentJsonMin.as[DeemedParentModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}

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

package models

import assets.UltimateParentConstants._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json
import v1.models.UltimateParentModel

class UltimateParentModelSpec extends WordSpec with Matchers {

  "UltimateParentModel" when {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = ultimateParentJsonMax
        val actualValue = Json.toJson(ultimateParentModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = ultimateParentJsonMin
        val actualValue = Json.toJson(ultimateParentModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = ultimateParentModelMax
        val actualValue = ultimateParentJsonMax.as[UltimateParentModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = ultimateParentModelMin
        val actualValue = ultimateParentJsonMin.as[UltimateParentModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}


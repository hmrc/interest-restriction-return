/*
 * Copyright 2019 HM Revenue & Customs
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

package models.abbreviatedReturn

import assets.UltimateParentConstants._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class UltimateParentModelSpec extends WordSpec with Matchers {

  "UltimateParentModel" when {

    "UkParent" must {

      "correctly write to json" when {

        "max values given" in {

          val expectedValue = ukParentJsonMax
          val actualValue = Json.toJson(ukParentModelMax)

          actualValue shouldBe expectedValue
        }

        "min values given" in {

          val expectedValue = ukParentJsonMin
          val actualValue = Json.toJson(ukParentModelMin)

          actualValue shouldBe expectedValue
        }
      }

      "correctly read from Json" when {

        "max values given" in {

          val expectedValue = ukParentModelMax
          val actualValue = ukParentJsonMax.as[UkParentModel]

          actualValue shouldBe expectedValue
        }

        "min values given" in {

          val expectedValue = ukParentModelMin
          val actualValue = ukParentJsonMin.as[UkParentModel]

          actualValue shouldBe expectedValue
        }
      }
    }

    "NonUkParent" must {

      "correctly write to json" when {

        "max values given" in {

          val expectedValue = nonUkParentJsonMax
          val actualValue = Json.toJson(nonUkParentModelMax)

          actualValue shouldBe expectedValue
        }

        "min values given" in {

          val expectedValue = nonUkParentJsonMin
          val actualValue = Json.toJson(nonUkParentModelMin)

          actualValue shouldBe expectedValue
        }
      }

      "correctly read from Json" when {

        "max values given" in {

          val expectedValue = nonUkParentModelMax
          val actualValue = nonUkParentJsonMax.as[NonUkParentModel]

          actualValue shouldBe expectedValue
        }

        "min values given" in {

          val expectedValue = nonUkParentModelMin
          val actualValue = nonUkParentJsonMin.as[NonUkParentModel]

          actualValue shouldBe expectedValue
        }
      }
    }
  }
}


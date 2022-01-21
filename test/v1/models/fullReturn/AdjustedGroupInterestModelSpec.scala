/*
 * Copyright 2022 HM Revenue & Customs
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

package models.fullReturn

import play.api.libs.json.Json
import assets.fullReturn.AdjustedGroupInterestConstants._
import v1.models.fullReturn.AdjustedGroupInterestModel
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class AdjustedGroupInterestModelSpec extends AnyWordSpec with Matchers {

  "AdjustedGroupInterestModel" must {

    "correctly write to json" when {
      "groupEbitda is present" in {
        val expectedValue = adjustedGroupInterestJson
        val actualValue = Json.toJson(adjustedGroupInterestModel)

        actualValue shouldBe expectedValue
      }

      "groupEbitda is not present" in {
        val expectedValue = adjustedGroupInterestJsonNoEbitda
        val actualValue = Json.toJson(adjustedGroupInterestModelNoEbitda)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" in {

      val expectedValue = adjustedGroupInterestModel
      val actualValue = adjustedGroupInterestJson.as[AdjustedGroupInterestModel]

      actualValue shouldBe expectedValue
    }
  }
}

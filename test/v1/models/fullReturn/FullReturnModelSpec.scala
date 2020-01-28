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

package models.fullReturn

import assets.BaseConstants
import assets.fullReturn.FullReturnConstants._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json
import v1.models.fullReturn.FullReturnModel

class FullReturnModelSpec extends WordSpec with Matchers with BaseConstants {

  "FullReturnModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = fullReturnReactivationUltimateJson
        val actualValue = Json.toJson(fullReturnUltimateParentModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = fullReturnJsonMin
        val actualValue = Json.toJson(fullReturnModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = fullReturnUltimateParentModelMax
        val actualValue = fullReturnReactivationUltimateJson.as[FullReturnModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = fullReturnModelMin
        val actualValue = fullReturnJsonMin.as[FullReturnModel]

        actualValue shouldBe expectedValue
      }
    }

    "correctly collect all of the ukCrns" when {

      "ultimate parent crn is given" in {

        val expectedValue = Seq(
          FullReturnModel.ultimateParentCrnPath -> crnLetters,
          FullReturnModel.reportingCompanyCrnPath -> crn
        )
        val actualValue = fullReturnUltimateParentModelMax.ukCrns

        actualValue shouldBe expectedValue
      }

      "deemed parent crn is given" in {

        val expectedValue = Seq(
          FullReturnModel.deemedParentCrnPath(0) -> crn,
          FullReturnModel.deemedParentCrnPath(1) -> crn,
          FullReturnModel.deemedParentCrnPath(2) -> crn,
          FullReturnModel.reportingCompanyCrnPath -> crn
        )
        val actualValue = fullReturnDeemedParentModel.ukCrns

        actualValue shouldBe expectedValue
      }

      "min crns given" in {

        val expectedValue = Seq(
          FullReturnModel.reportingCompanyCrnPath -> crn
        )
        val actualValue = fullReturnModelMin.ukCrns

        actualValue shouldBe expectedValue
      }
    }
  }
}
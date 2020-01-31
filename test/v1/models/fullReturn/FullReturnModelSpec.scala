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

      "max values given without a net expense or income" in {

        val expectedValue = fullReturnJsonMax
        val actualValue = Json.toJson(fullReturnModelMax)

        actualValue shouldBe expectedValue
      }

      "max values given with an net expense" in {

        val expectedValue = fullReturnNetTaxExpenseJsonMax
        val actualValue = Json.toJson(fullReturnNetTaxExpenseModelMax)

        actualValue shouldBe expectedValue
      }

      "max values given with an a net income" in {

        val expectedValue = fullReturnNetTaxIncomeJsonMax
        val actualValue = Json.toJson(fullReturnNetTaxIncomeModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = fullReturnJsonMin
        val actualValue = Json.toJson(fullReturnModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given withoput a net expense or income" in {

        val expectedValue = fullReturnModelMax
        val actualValue = fullReturnJsonMax.as[FullReturnModel]

        actualValue shouldBe expectedValue
      }

      "max values given with a net expense" in {

        val expectedValue = fullReturnNetTaxExpenseModelMax
        val actualValue = fullReturnNetTaxExpenseJsonMax.as[FullReturnModel]

        actualValue shouldBe expectedValue
      }

      "max values given with a net income" in {

        val expectedValue = fullReturnNetTaxIncomeModelMax
        val actualValue = fullReturnNetTaxIncomeJsonMax.as[FullReturnModel]

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
        val actualValue = fullReturnUltimateParentModel.ukCrns

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
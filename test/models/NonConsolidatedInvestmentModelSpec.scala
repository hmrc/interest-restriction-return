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

package models

import assets.NonConsolidatedInvestmentConstants._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class NonConsolidatedInvestmentModelSpec extends WordSpec with Matchers {

  "NonConsolidatedInvestmentModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = nonConsolidatedInvestmentJsonMax
        val actualValue = Json.toJson(nonConsolidatedInvestmentModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = nonConsolidatedInvestmentJsonMin
        val actualValue = Json.toJson(nonConsolidatedInvestmentModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {
        val expectedValue = nonConsolidatedInvestmentModelMax
        val actualValue = nonConsolidatedInvestmentJsonMax.as[NonConsolidatedInvestmentModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {
        val expectedValue = nonConsolidatedInvestmentModelMin
        val actualValue = nonConsolidatedInvestmentJsonMin.as[NonConsolidatedInvestmentModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}


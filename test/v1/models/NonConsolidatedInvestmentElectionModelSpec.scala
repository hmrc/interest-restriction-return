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

import data.NonConsolidatedInvestmentElectionConstants.*
import play.api.libs.json.{JsError, Json}
import utils.BaseSpec

class NonConsolidatedInvestmentElectionModelSpec extends BaseSpec {

  "NonConsolidatedInvestmentElectionModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = nonConsolidatedInvestmentJsonMax
        val actualValue   = Json.toJson(nonConsolidatedInvestmentModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = nonConsolidatedInvestmentJsonMin
        val actualValue   = Json.toJson(nonConsolidatedInvestmentModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {
        val expectedValue = nonConsolidatedInvestmentModelMax
        val actualValue   = nonConsolidatedInvestmentJsonMax.as[NonConsolidatedInvestmentElectionModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {
        val expectedValue = nonConsolidatedInvestmentModelMin
        val actualValue   = nonConsolidatedInvestmentJsonMin.as[NonConsolidatedInvestmentElectionModel]

        actualValue shouldBe expectedValue
      }
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[NonConsolidatedInvestmentElectionModel] shouldBe a[JsError]
      }
      "empty json" in {
        Json.obj().validate[NonConsolidatedInvestmentElectionModel] shouldBe a[JsError]
      }
    }
  }
}

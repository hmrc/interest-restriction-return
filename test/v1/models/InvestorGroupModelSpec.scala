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

import data.InvestorGroupConstants._
import play.api.libs.json.Json
import utils.BaseSpec

class InvestorGroupModelSpec extends BaseSpec {

  "InvestorGroupModel" must {

    "correctly write (serialise) to json" when {

      "no elections are provided" in {

        val expectedValue = investorGroupsJsonMin
        val actualValue   = Json.toJson(investorGroupsModelMin)

        actualValue shouldBe expectedValue
      }

      "fixed ratio elections are given" in {

        val expectedValue = investorGroupsFixedRatioJson
        val actualValue   = Json.toJson(investorGroupsFixedRatioModel)

        actualValue shouldBe expectedValue
      }

      "Group ratio elections are given" in {

        val expectedValue = investorGroupsGroupRatioJson
        val actualValue   = Json.toJson(investorGroupsGroupRatioModel)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read (deserialise) from json" when {

      "no elections are provided" in {

        val expectedValue = investorGroupsModelMin
        val actualValue   = investorGroupsJsonMin.as[InvestorGroupModel]

        actualValue shouldBe expectedValue
      }

      "fixed ratio elections are given" in {

        val expectedValue = investorGroupsFixedRatioModel
        val actualValue   = investorGroupsFixedRatioJson.as[InvestorGroupModel]

        actualValue shouldBe expectedValue
      }

      "Group ratio elections are given" in {

        val expectedValue = investorGroupsGroupRatioModel
        val actualValue   = investorGroupsGroupRatioJson.as[InvestorGroupModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}

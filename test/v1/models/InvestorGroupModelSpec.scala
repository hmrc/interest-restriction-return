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

package v1.models

import assets.InvestorGroupConstants._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class InvestorGroupModelSpec extends WordSpec with Matchers {

  "InvestorGroupModel" must {

    "correctly write to json" when {

      "no elections are provided" in {

        val expectedValue = investorGroupsJsonMin
        val actualValue = Json.toJson(investorGroupsModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = groupLevelElectionsModelMax
        val actualValue = groupLevelElectionsJsonMax.as[GroupLevelElectionsModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}

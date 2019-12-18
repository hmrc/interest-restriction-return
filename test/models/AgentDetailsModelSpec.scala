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

import assets.AgentDetailsConstants._
import cats.data.Validated.{Invalid, Valid}
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json
import validation.{AgentNameLengthError, AgentNameNotSuppliedError, AgentNameSuppliedError}

class AgentDetailsModelSpec extends WordSpec with Matchers {

  "AgentDetailsModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = agentDetailsJsonMax
        val actualValue = Json.toJson(agentDetailsModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = agentDetailsJsonMin
        val actualValue = Json.toJson(agentDetailsModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = agentDetailsModelMax
        val actualValue = agentDetailsJsonMax.as[AgentDetailsModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = agentDetailsModelMin
        val actualValue = agentDetailsJsonMin.as[AgentDetailsModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}

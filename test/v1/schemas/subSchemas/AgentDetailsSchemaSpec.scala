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

package v1.schemas.subSchemas

import play.api.libs.json.{JsValue, Json}
import v1.schemas.BaseSchemaSpec
import v1.schemas.helpers._

class AgentDetailsSchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean =
    validateJson("subSchemas/agentDetails.json", "1.0", json)

  "AgentDetails Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload" in {
        validate(Json.toJson(AgentDetails())) shouldBe true
      }
    }

    "Return invalid" when {

      "agentActingOnBehalfOfCompany" when {

        s"is None" in {

          val json = Json.toJson(AgentDetails(
            agentActingOnBehalfOfCompany = None
          ))

          validate(json) shouldBe false
        }
      }

      "agentName" when {

        s"is supplied but blank" in {

          val json = Json.toJson(AgentDetails(
            agentName = Some("")
          ))

          validate(json) shouldBe false
        }

        s"is supplied but exceeds $maxAgentNameLength" in {

          val json = Json.toJson(AgentDetails(
            agentName = Some("A" * (maxAgentNameLength + 1))
          ))

          validate(json) shouldBe false
        }
      }
    }
  }
}
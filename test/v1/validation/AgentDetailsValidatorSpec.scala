/*
 * Copyright 2023 HM Revenue & Customs
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

package v1.validation

import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.AgentDetailsModel

class AgentDetailsValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "AgentDetailsValidator" when {
    "passed false and No name should succeed" in {
      val model = AgentDetailsModel(agentActingOnBehalfOfCompany = false, None)
      rightSide(model.validate) shouldBe model
    }

    "passed true and Some name (with correct name length) should succeed" in {
      val model = AgentDetailsModel(agentActingOnBehalfOfCompany = true, Some("AgentYang"))
      rightSide(model.validate) shouldBe model
    }

    "passed true and Some name (with incorrect name length) should not succeed" in {
      val model = AgentDetailsModel(agentActingOnBehalfOfCompany = true, Some(""))
      leftSideError(model.validate).errorMessage shouldBe AgentNameLengthError("").errorMessage
    }

    "passed false and Some name (with correct name length) should not succeed" in {
      val model = AgentDetailsModel(agentActingOnBehalfOfCompany = false, Some("Yangksy"))
      leftSideError(model.validate).errorMessage shouldBe AgentNameSuppliedError("Yangksy").errorMessage

    }

    "passed false and Some name (with incorrect name length) should not succeed with 2 errors" in {
      val model = AgentDetailsModel(agentActingOnBehalfOfCompany = false, Some(""))
      leftSideError(model.validate, 0).errorMessage shouldBe AgentNameLengthError("").errorMessage
      leftSideError(model.validate, 1).errorMessage shouldBe AgentNameSuppliedError("").errorMessage
    }

    "passed true and None should not succeed" in {
      val model = AgentDetailsModel(agentActingOnBehalfOfCompany = true, None)
      leftSideError(model.validate).errorMessage shouldBe AgentNameNotSuppliedError().errorMessage
    }

    "passed true and Some name (with incorrect name length > maxLength) should not succeed" in {
      val model = AgentDetailsModel(agentActingOnBehalfOfCompany = true, Some("a" * 180))
      leftSideError(model.validate).errorMessage shouldBe AgentNameLengthError("a" * 180).errorMessage
    }

    "passed true and Some name with invalid characters should not succeed" in {
      val invalidName = "ʰʲʺ£$%˦˫qwNew!£$%^&*()_ComPan\n with spacs Ā to ʯ, Ḁ to ỿ :' ₠ to ₿ Å and K lenth is" +
        " 160 no numbers allowed New!£$%^&*()_ComPany with spaces Ā to ʯ, Ḁ to ỿ"

      val model = AgentDetailsModel(agentActingOnBehalfOfCompany = true, Some(invalidName))
      leftSideError(model.validate).errorMessage shouldBe AgentNameCharactersError(invalidName).errorMessage
    }

    "Passing true and invalid characters should not succeed" in {
      val model = AgentDetailsModel(agentActingOnBehalfOfCompany = true, Some("ʰʲʺ˦˫˥ʺ˦˫˥"))
      leftSideError(model.validate).errorMessage shouldBe AgentNameCharactersError("ʰʲʺ˦˫˥ʺ˦˫˥").errorMessage
    }

    "passed true and Some name with an end of line should not succeed" in {
      val invalidName = "\n"
      val model       = AgentDetailsModel(agentActingOnBehalfOfCompany = true, Some(invalidName))
      leftSideError(model.validate).errorMessage shouldBe AgentNameCharactersError(invalidName).errorMessage
    }
  }
}

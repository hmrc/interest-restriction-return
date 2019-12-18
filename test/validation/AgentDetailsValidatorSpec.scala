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

package validation

import models.AgentDetailsModel
import play.api.libs.json.JsPath

class AgentDetailsValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Agent Details Validation" when {
    "passed false and No name should succeed" in {
      val model = AgentDetailsModel(false, None)
      rightSide(model.validate) shouldBe model
    }

    "passed true and Some name (with correct name length) should succeed" in {
      val model = AgentDetailsModel(true, Some("AgentYang"))
      rightSide(model.validate) shouldBe model
    }

    "passed true and Some name (with incorrect name length) should not succeed" in {
      val model = AgentDetailsModel(true, Some(""))
      leftSideError(model.validate).errorMessage shouldBe AgentNameLengthError("").errorMessage
    }

    "passed false and Some name (with correct name length) should not succeed" in {
      val model = AgentDetailsModel(false, Some("Yangksy"))
      leftSideError(model.validate).errorMessage shouldBe AgentNameSuppliedError("Yangksy").errorMessage

    }

    "passed false and Some name (with incorrect name length) should not succeed with 2 errors" in {
      val model = AgentDetailsModel(false , Some(""))
      leftSideError(model.validate).errorMessage shouldBe
        errorMessages(AgentNameLengthError("").errorMessage, AgentNameSuppliedError("").errorMessage)
    }

    "passed true and None should not succeed" in {
      val model = AgentDetailsModel(true, None)
      leftSideError(model.validate).errorMessage shouldBe AgentNameNotSuppliedError().errorMessage
    }

    "passed true and Some name (with incorrect name length > maxLength) should not succeed" in {
      val model = AgentDetailsModel(true, Some("a" * 180))
      leftSideError(model.validate).errorMessage shouldBe AgentNameLengthError("a" * 180).errorMessage
    }

  }
}

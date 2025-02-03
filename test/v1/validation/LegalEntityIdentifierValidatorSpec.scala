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

package v1.validation

import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.LegalEntityIdentifierModel

class LegalEntityIdentifierValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "LegalEntityIdentifierValidator" when {

    "is supplied and contains invalid character" in {
      val model = LegalEntityIdentifierModel("abcdeabcdeabcdeabc!12")
      leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(model).errorMessage
    }

    "is supplied and contains lower case" in {
      val model = LegalEntityIdentifierModel("abcdeabcdeabcdeabc12")
      leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(model).errorMessage
    }

    "is supplied and is too long" in {
      val model = LegalEntityIdentifierModel("abcdeabcdeabcdeabc123")
      leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(model).errorMessage
    }

    "is supplied and is too short" in {
      val model = LegalEntityIdentifierModel("abcdeabcdeabcdeabc1")
      leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(model).errorMessage
    }
  }
}

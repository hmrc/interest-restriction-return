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

package v1.validation

import assets.DeemedParentConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec

class DeemedParentValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Deemed Parent Validation" when {

    "Uk and NonUK fields are populated" should {

      "Return invalid" in {

        val model = deemedParentModelMax

        leftSideError(model.validate).errorMessage shouldBe DeemedParentCannotBeUkAndNonUk(model).errorMessage
      }
    }

    "Uk fields are populated" should {

      "Return valid" in {
        val model = deemedParentModelUkCompany

        rightSide(model.validate) shouldBe model
      }
    }

    "NonUk fields are populated" should {

      "Return valid" in {
        val model = deemedParentModelNonUkCompany

        rightSide(model.validate) shouldBe model
      }
    }

    "Both versions of UTR are supplied for Uk Company" in {
      val model = deemedParentModelUkCompany.copy(
        sautr = Some(sautr))

      leftSideError(model.validate).errorMessage shouldBe DeemedParentUTRSuppliedError(model).errorMessage
    }
  }
}

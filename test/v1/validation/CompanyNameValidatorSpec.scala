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
import v1.models.CompanyNameModel
import utils.BaseSpec

class CompanyNameValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "CompanyNameValidator" should {

    "return invalid" when {

      "company name is below the minimum length" in {

        val model = CompanyNameModel(
          name = ""
        )
        model.validate.toEither.left.value.head.errorMessage shouldBe CompanyNameLengthError(model.name).errorMessage
      }

      "company name is above the maximum length" in {

        val model = CompanyNameModel(
          name = "a" * 161
        )
        model.validate.toEither.left.value.head.errorMessage shouldBe CompanyNameLengthError(model.name).errorMessage
      }

      "company name contains an invalid character" in {
        val invalidName = "ʰʲʺ£$%˦˫qwNew!£$%^&*()_ComPan\n with spacs Ā to ʯ, Ḁ to ỿ :' ₠ to ₿ Å and K lenth is" +
          " 160 no numbers allowed New!£$%^&*()_ComPany with spaces Ā to ʯ, Ḁ to ỿ"

        val model = CompanyNameModel(invalidName)
        model.validate.toEither.left.value.head.errorMessage shouldBe CompanyNameCharactersError(
          model.name
        ).errorMessage
      }

      "company name contains an end of line" in {
        val model = CompanyNameModel("1111\n222222")
        model.validate.toEither.left.value.head.errorMessage shouldBe CompanyNameCharactersError(
          model.name
        ).errorMessage
      }
    }
  }
}

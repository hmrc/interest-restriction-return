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

import data.NonConsolidatedInvestmentConstants.*
import play.api.libs.json.JsPath

class NonConsolidatedInvestmentValidatorSpec extends BaseValidationSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "NonConsolidatedInvestmentValidator" should {

    "Return valid" when {

      "isElected is true and no investment names are given" in {
        val model = nonConsolidatedModel.copy(investmentName = investmentName)
        model.validate.toEither.value shouldBe model
      }
    }

    "Return invalid" when {

      "Investment Name" when {
        "is greater than 5000" in {
          val model = nonConsolidatedModel.copy(investmentName = "a" * (32767 + 1))
          model.validate.toEither.left.value.head.errorMessage shouldBe NonConsolidatedInvestmentNameLengthError(
            "a" * (32767 + 1)
          ).errorMessage
        }

        "is zero " in {
          val model = nonConsolidatedModel.copy(investmentName = "")
          model.validate.toEither.left.value.head.errorMessage shouldBe NonConsolidatedInvestmentNameLengthError(
            ""
          ).errorMessage
        }

        "contains invalid characters" in {
          val name = "ʰʲʺ£$%˦˫qwNew!£$%^&*()_ComPan\n with spacs Ā to ʯ, Ḁ to ỿ :' ₠ to ₿ Å and K lenth is" +
            " 160 characters no numbers allowed New!£$%^&*()_ComPany with spaces Ā to ʯ, Ḁ to ỿ"

          val model = nonConsolidatedModel.copy(investmentName = name)
          model.validate.toEither.left.value.head.errorMessage shouldBe NonConsolidatedInvestmentNameCharacterError(
            investmentName
          ).errorMessage
        }

        "contains an end of line" in {
          val name  = "\n"
          val model = nonConsolidatedModel.copy(investmentName = name)
          model.validate.toEither.left.value.head.errorMessage shouldBe NonConsolidatedInvestmentNameCharacterError(
            investmentName
          ).errorMessage
        }

        "isElected is true and no investment names are given" in {
          val model = nonConsolidatedModel.copy(investmentName = "")
          model.validate.toEither.left.value.head.errorMessage shouldBe NonConsolidatedInvestmentNameLengthError(
            ""
          ).errorMessage
        }
      }
    }
  }
}

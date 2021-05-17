/*
 * Copyright 2021 HM Revenue & Customs
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

import utils.BaseSpec
import play.api.libs.json.JsPath
import v1.models.RevisedReturnDetailsModel

class RevisedReturnDetailsValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "RevisedReturnDetails Validation" should {

    "return invalid" when {

      "Return type is Revised and the revised return details are less than 1 character long" in {

        leftSideError(RevisedReturnDetailsModel("").validate).errorMessage shouldBe RevisedReturnDetailsLengthError("").errorMessage
      }

      "Return type is Revised and the revised return details are more than 5000 character longs" in {

        leftSideError(RevisedReturnDetailsModel("a" * 5001).validate).errorMessage shouldBe RevisedReturnDetailsLengthError("a" * 5001).errorMessage
      }

      "Return type is Revised and the revised return details contains invalid characters" in {
        val returnDetails = "ʰʲʺ£$%˦˫qwNew!£$%^&*()_ComPan\n with spacs Ā to ʯ, Ḁ to ỿ :' ₠ to ₿ Å and K lenth is" +
          " 160 characters no numbers allowed New!£$%^&*()_ComPany with spaces Ā to ʯ, Ḁ to ỿ"
        leftSideError(RevisedReturnDetailsModel(returnDetails).validate).errorMessage shouldBe RevisedReturnDetailsCharacterError(returnDetails).errorMessage
      }

      "Return type is Revised and the revised return details contain an end of line character" in {
        val returnDetails = "\n"
        leftSideError(RevisedReturnDetailsModel(returnDetails).validate).errorMessage shouldBe RevisedReturnDetailsCharacterError(returnDetails).errorMessage
      }
    }
  }
}

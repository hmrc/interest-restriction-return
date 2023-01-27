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

import assets.PartnershipsConstants._
import play.api.libs.json.JsPath
import v1.models.{CompanyNameModel, UTRModel}

class PartnershipValidatorSpec extends BaseValidationSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "PartnershipValidator" when {
    ".validate" should {
      "return the valid model" when {
        "partnership name supplied is valid" in {
          val model = partnershipModel.copy(CompanyNameModel(partnerName))
          rightSide(model.validate) shouldBe model
        }

        "sautr is not supplied" in {
          val model = partnershipModel.copy(sautr = None)
          rightSide(model.validate) shouldBe model
        }
      }

      "return appropriate error message" when {
        "the length of the partnership name" which {
          "is supplied is greater than 160" in {
            val model = partnershipModel.copy(partnershipName = CompanyNameModel("a" * (32767 + 1)))
            leftSideError(model.validate).errorMessage shouldBe CompanyNameLengthError("a" * (32767 + 1)).errorMessage
          }

          "is supplied is less than 1" in {
            val model = partnershipModel.copy(partnershipName = CompanyNameModel(""))
            leftSideError(model.validate).errorMessage shouldBe CompanyNameLengthError("").errorMessage
          }
        }

        "sautr supplied is invalid" in {
          val utr   = UTRModel("11234567890")
          val model = partnershipModel.copy(sautr = Some(utr))
          leftSideError(model.validate).errorMessage shouldBe UTRLengthError(utr).errorMessage
        }
      }
    }
  }
}

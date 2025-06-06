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

import data.AuthorisingCompanyConstants.*
import play.api.libs.json.JsPath
import v1.models.{CompanyNameModel, UTRModel}

class AuthorisingCompanyValidatorSpec extends BaseValidationSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "AuthorisingCompanyValidator" should {

    "Return valid" when {

      "a valid Authorising Company model is validated" in {
        rightSide(authorisingCompanyModel.validate) shouldBe authorisingCompanyModel
      }
    }

    "Return invalid" when {

      "Company name" when {

        "Company name is empty" in {
          leftSideError(
            authorisingCompanyModel.copy(companyName = CompanyNameModel("")).validate
          ).errorMessage shouldBe CompanyNameLengthError("").errorMessage
        }

        "Company name contains invalid characters" in {
          leftSideError(
            authorisingCompanyModel.copy(companyName = CompanyNameModel("ʰʲʺ˦˫˥ʺ˦˫˥")).validate
          ).errorMessage shouldBe CompanyNameCharactersError("ʰʲʺ˦˫˥ʺ˦˫˥").errorMessage
        }

        s"Company name is longer that $companyNameMaxLength" in {
          leftSideError(
            authorisingCompanyModel.copy(companyName = companyNameTooLong).validate
          ).errorMessage shouldBe CompanyNameLengthError("a" * (companyNameMaxLength + 1)).errorMessage
        }
      }

      "UTR is invalid" in {
        leftSideError(authorisingCompanyModel.copy(utr = invalidUtr).validate).errorMessage shouldBe UTRChecksumError(
          invalidUtr
        ).errorMessage
      }

      "UTR is contains invalid characters" in {
        leftSideError(
          authorisingCompanyModel.copy(utr = UTRModel("ʰʲʺ˦˫˥ʺ˦˫˥")).validate
        ).errorMessage shouldBe UTRChecksumError(UTRModel("ʰʲʺ˦˫˥ʺ˦˫˥")).errorMessage
      }

      "UTR is empty" in {
        leftSideError(authorisingCompanyModel.copy(utr = UTRModel("")).validate).errorMessage shouldBe UTRLengthError(
          UTRModel("")
        ).errorMessage
      }

      "UTR is too short" in {
        leftSideError(
          authorisingCompanyModel.copy(utr = invalidShortUtr).validate
        ).errorMessage shouldBe UTRLengthError(invalidShortUtr).errorMessage
      }

      "UTR is too long" in {
        leftSideError(authorisingCompanyModel.copy(utr = invalidLongUtr).validate).errorMessage shouldBe UTRLengthError(
          invalidLongUtr
        ).errorMessage
      }
    }
  }
}

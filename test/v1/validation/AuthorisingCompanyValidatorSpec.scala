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

import assets.AuthorisingCompanyConstants._
import play.api.libs.json.JsPath
import v1.models.CompanyNameModel

class AuthorisingCompanyValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Authorising Company Validation" should {

    "Return valid" when {

      "a valid Authorising Company model is validated" in {
        rightSide(authorisingCompanyModel.validate) shouldBe authorisingCompanyModel
      }
    }

    "Return invalid" when {

      "Company name" when {

        "Company name is empty" in {
          leftSideError(authorisingCompanyModel.copy(companyName = CompanyNameModel("")).validate).message shouldBe CompanyNameLengthError("").message
        }

        s"Company name is longer that ${companyNameMaxLength}" in {
          leftSideError(authorisingCompanyModel.copy(companyName = companyNameTooLong).validate).message shouldBe CompanyNameLengthError("a" * (companyNameMaxLength + 1)).message
        }
      }

      "UTR is invalid" in {
        leftSideError(authorisingCompanyModel.copy(ctutr = invalidUtr).validate).message shouldBe UTRChecksumError(invalidUtr).message
      }
    }
  }
}

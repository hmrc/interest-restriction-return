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

import assets.IdentityOfCompanySubmittingConstants._
import play.api.libs.json.JsPath

class IdentityOfCompanySubmittingValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Identity of Company Submitting Validation" should {

    "Return valid" when {

      "Valid UK fields are populated" in {
        val model = identityOfCompanySubmittingModelMax.copy(
          nonUkCrn = None,
          countryOfIncorporation = None
        )

        rightSide(model.validate) shouldBe model
      }

      "Valid NonUk fields are populated" in {

        val model = identityOfCompanySubmittingModelMax.copy(
          crn = None,
          ctutr = None
        )

        rightSide(model.validate) shouldBe model
      }
    }

    "Return invalid" when {

      "Uk and NonUK fields are populated" in {

        val model = identityOfCompanySubmittingModelMax.copy(
          nonUkCrn = Some("1234567"),
          countryOfIncorporation = Some(nonUkCountryCode)
        )

        leftSideError(model.validate).errorMessage shouldBe CannotBeUkAndNonUk(model).errorMessage
      }

      "CTUTR is invalid" in {
        val model = identityOfCompanySubmittingModelMax.copy(
          nonUkCrn = None,
          countryOfIncorporation = None,
          ctutr = Some(invalidUtr))

        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "CRN is invalid" in {
        val model = identityOfCompanySubmittingModelMax.copy(
          nonUkCrn = None,
          countryOfIncorporation = None,
          crn = Some(invalidCrn))

        leftSideError(model.validate).errorMessage shouldBe CRNFormatCheck(invalidCrn).errorMessage
      }

      "CountryOfIncorporation is invalid" in {
        val model = identityOfCompanySubmittingModelMax.copy(
          crn = None,
          ctutr = None,
          countryOfIncorporation = Some(invalidCountryCode))

        leftSideError(model.validate).errorMessage shouldBe CountryCodeValueError(invalidCountryCode).errorMessage
      }
    }
  }
}

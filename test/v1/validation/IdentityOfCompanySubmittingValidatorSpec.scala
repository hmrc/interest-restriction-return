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

import assets.IdentityOfCompanySubmittingConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec

class IdentityOfCompanySubmittingValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Identity of Company Submitting Validation" should {

    "Return valid" when {

      "Valid UK fields are populated" in {
        val model = identityOfCompanySubmittingModelMax

        rightSide(model.validate) shouldBe model
      }

      "Valid NonUk fields are populated" in {

        val model = identityOfCompanySubmittingModelMax.copy(
          ctutr = None,
          countryOfIncorporation = Some(nonUkCountryCode)
        )

        rightSide(model.validate) shouldBe model
      }
    }

    "Return invalid" when {

      "Uk and NonUK fields are populated" in {

        val model = identityOfCompanySubmittingModelMax.copy(countryOfIncorporation = Some(nonUkCountryCode))

        leftSideError(model.validate).errorMessage shouldBe CannotBeUkAndNonUk(model).errorMessage
      }

      "CTUTR is invalid" in {
        val model = identityOfCompanySubmittingModelMax.copy(
          countryOfIncorporation = None,
          ctutr = Some(invalidUtr))

        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "CountryOfIncorporation is invalid" in {
        val model = identityOfCompanySubmittingModelMax.copy(
          ctutr = None,
          countryOfIncorporation = Some(invalidCountryCode))

        leftSideError(model.validate).errorMessage shouldBe CountryCodeValueError(invalidCountryCode).errorMessage
      }

      "LegalEntityIdentifier is invalid" in {
        val model = identityOfCompanySubmittingModelMax.copy(
          ctutr = None,
          legalEntityIdentifier = Some(invalidLei))

        leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(invalidLei).errorMessage
      }
    }
  }
}

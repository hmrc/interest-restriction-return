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

import data.IdentityOfCompanySubmittingConstants.*
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.{CompanyNameModel, LegalEntityIdentifierModel, UTRModel}

class IdentityOfCompanySubmittingValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "IdentityOfCompanySubmittingValidator" should {

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

      "Company Name is valid" in {
        val model = identityOfCompanySubmittingModelMax.copy(companyName = companyName)
        rightSide(model.validate) shouldBe model
      }

      "CTUTR contains invalid characters" in {
        val model = identityOfCompanySubmittingModelMax.copy(countryOfIncorporation = None, ctutr = Some(ctutr))

        rightSide(model.validate) shouldBe model
      }
    }

    "Return invalid" when {

      "Uk and NonUK fields are populated" in {

        val model = identityOfCompanySubmittingModelMax.copy(countryOfIncorporation = Some(nonUkCountryCode))
        leftSideError(model.validate).errorMessage shouldBe CannotBeUkAndNonUk(model).errorMessage
      }

      "Company Name is empty" in {
        val model = identityOfCompanySubmittingModelMax.copy(companyName = companyNameIsZero)
        leftSideError(model.validate).errorMessage shouldBe CompanyNameLengthError(companyNameIsZero.name).errorMessage
      }

      "Company Name is invalid" in {
        val model = identityOfCompanySubmittingModelMax.copy(companyName = CompanyNameModel("ʰʲʺ˦˫˥ʺ˦˫˥"))
        leftSideError(model.validate).errorMessage shouldBe CompanyNameCharactersError("ʰʲʺ˦˫˥ʺ˦˫˥").errorMessage
      }

      "Company Name is too long" in {
        val model = identityOfCompanySubmittingModelMax.copy(companyName = companyNameTooLong)
        leftSideError(model.validate).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }

      "CTUTR contains invalid characters" in {
        val model =
          identityOfCompanySubmittingModelMax.copy(countryOfIncorporation = None, ctutr = Some(UTRModel("ʰʲʺ˦˫˥ʺ˦˫˥")))

        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(UTRModel("ʰʲʺ˦˫˥ʺ˦˫˥")).errorMessage
      }

      "CTUTR is invalid" in {
        val model = identityOfCompanySubmittingModelMax.copy(countryOfIncorporation = None, ctutr = Some(invalidUtr))

        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "CTUTR is empty" in {
        val model = identityOfCompanySubmittingModelMax.copy(countryOfIncorporation = None, ctutr = Some(UTRModel("")))

        leftSideError(model.validate).errorMessage shouldBe UTRLengthError(UTRModel("")).errorMessage
      }

      "CTUTR is too short" in {
        val model =
          identityOfCompanySubmittingModelMax.copy(countryOfIncorporation = None, ctutr = Some(invalidShortUtr))

        leftSideError(model.validate).errorMessage shouldBe UTRLengthError(invalidShortUtr).errorMessage
      }

      "CTUTR is too long" in {
        val model =
          identityOfCompanySubmittingModelMax.copy(countryOfIncorporation = None, ctutr = Some(invalidLongUtr))

        leftSideError(model.validate).errorMessage shouldBe UTRLengthError(invalidLongUtr).errorMessage
      }

      "CountryOfIncorporation is invalid" in {
        val model =
          identityOfCompanySubmittingModelMax.copy(ctutr = None, countryOfIncorporation = Some(invalidCountryCode))

        leftSideError(model.validate).errorMessage shouldBe CountryCodeValueError(invalidCountryCode).errorMessage
      }

      "LegalEntityIdentifier is invalid" in {
        val model = identityOfCompanySubmittingModelMax.copy(ctutr = None, legalEntityIdentifier = Some(invalidLei))
        leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(invalidLei).errorMessage
      }

      "LEI is not supplied" in {
        val model =
          identityOfCompanySubmittingModelMax.copy(legalEntityIdentifier = Some(LegalEntityIdentifierModel("")))
        leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
          LegalEntityIdentifierModel("")
        ).errorMessage
      }

      "LEI is too long" in {
        val model = identityOfCompanySubmittingModelMax.copy(
          legalEntityIdentifier = Some(LegalEntityIdentifierModel("QWERTYUIOPASDFGHJq11QWERTYUIOPASDFGHJq11"))
        )

        leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
          LegalEntityIdentifierModel("QWERTYUIOPASDFGHJq11QWERTYUIOPASDFGHJq11")
        ).errorMessage
      }

      "LEI is too short" in {
        val model = identityOfCompanySubmittingModelMax.copy(
          legalEntityIdentifier = Some(LegalEntityIdentifierModel("QWERTYUIOPA"))
        )

        leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
          LegalEntityIdentifierModel("QWERTYUIOPA")
        ).errorMessage
      }

      "LEI is invalid values" in {
        val model = identityOfCompanySubmittingModelMax.copy(
          legalEntityIdentifier = Some(LegalEntityIdentifierModel("QWERTYUIOPASDFGHJAA1"))
        )

        leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
          LegalEntityIdentifierModel("QWERTYUIOPASDFGHJAA1")
        ).errorMessage
      }

      "LEI is contains an invalid character" in {
        val model = identityOfCompanySubmittingModelMax.copy(legalEntityIdentifier =
          Some(LegalEntityIdentifierModel("QWERTYUIOPASDFGHJ!11"))
        )

        leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
          LegalEntityIdentifierModel("QWERTYUIOPASDFGHJ!11")
        ).errorMessage
      }
    }
  }
}

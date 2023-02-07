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

import assets.DeemedParentConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.{CompanyNameModel, LegalEntityIdentifierModel, UTRModel}

class DeemedParentValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "DeemedParentValidator" should {

    "Return valid" when {

      "Uk fields are populated and non-Uk fields are not populated" in {
        val model = deemedParentModelUkCompany

        rightSide(model.validate) shouldBe model
      }

      "NonUk fields are populated and Uk fields are not populated" in {

        val model = deemedParentModelNonUkCompany

        rightSide(model.validate) shouldBe model
      }

      "UK Company Details Supplied" in {

        val model = deemedParentModelUkCompany

        rightSide(model.validate) shouldBe model
      }

      "UK Partnership Details Supplied" in {

        val model = deemedParentModelUkPartnership

        rightSide(model.validate) shouldBe model
      }

      "NonUK Company Details Supplied" in {

        val model = deemedParentModelNonUkCompany

        rightSide(model.validate) shouldBe model
      }

    }

    "Return invalid" when {

      "Company name" when {

        "Company name is empty" in {
          val model = deemedParentModelUkCompany.copy(companyName = CompanyNameModel(""))
          leftSideError(model.validate).errorMessage shouldBe CompanyNameLengthError("").errorMessage
        }

        "Company name contains invalid characters" in {
          val model = deemedParentModelUkCompany.copy(companyName = CompanyNameModel("ʰʲʺ£$%˦˫qw"))
          leftSideError(model.validate).errorMessage shouldBe CompanyNameCharactersError("ʰʲʺ£$%˦˫qw").errorMessage
        }

        s"Company name is longer that $companyNameMaxLength" in {
          val model = deemedParentModelUkCompany.copy(companyName = companyNameTooLong)
          leftSideError(model.validate).errorMessage shouldBe CompanyNameLengthError(
            "a" * (companyNameMaxLength + 1)
          ).errorMessage
        }
      }

      "CTUTR is invalid" in {
        val model = deemedParentModelUkCompany.copy(ctutr = Some(invalidUtr))
        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "CTUTR is empty" in {
        val model = deemedParentModelUkCompany.copy(ctutr = Some(UTRModel("")))
        leftSideError(model.validate).errorMessage shouldBe UTRLengthError(UTRModel("")).errorMessage
      }

      "CTUTR is to short" in {
        leftSideError(
          deemedParentModelUkCompany.copy(ctutr = Some(UTRModel("1"))).validate
        ).errorMessage shouldBe UTRLengthError(invalidShortUtr).errorMessage
      }

      "CTUTR is to long" in {
        leftSideError(
          deemedParentModelUkCompany.copy(ctutr = Some(UTRModel("11234567890"))).validate
        ).errorMessage shouldBe UTRLengthError(invalidLongUtr).errorMessage
      }

      "SAUTR is invalid" in {
        val model = deemedParentModelUkCompany.copy(ctutr = None, sautr = Some(invalidUtr))

        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "SAUTR is to empty" in {
        leftSideError(
          deemedParentModelUkCompany.copy(ctutr = None, sautr = Some(UTRModel(""))).validate
        ).errorMessage shouldBe UTRLengthError(UTRModel("")).errorMessage
      }

      "SAUTR is to short" in {
        leftSideError(
          deemedParentModelUkCompany.copy(ctutr = None, sautr = Some(UTRModel("1"))).validate
        ).errorMessage shouldBe UTRLengthError(invalidShortUtr).errorMessage
      }

      "SAUTR is to long" in {
        leftSideError(
          deemedParentModelUkCompany.copy(ctutr = None, sautr = Some(UTRModel("11234567890"))).validate
        ).errorMessage shouldBe UTRLengthError(invalidLongUtr).errorMessage
      }

      "CountryOfIncorporation is invalid" in {

        val model = deemedParentModelNonUkCompany.copy(countryOfIncorporation = Some(invalidCountryCode))
        leftSideError(model.validate).errorMessage shouldBe CountryCodeValueError(invalidCountryCode).errorMessage
      }

      "wrong models given" when {

        "Both UK Company and Partnership Details Supplied" in {

          val model = deemedParentModelUkCompany.copy(sautr = Some(sautr))
          leftSideError(model.validate).errorMessage shouldBe DeemedParentWrongDetailsSuppliedError(model).errorMessage
        }

        "Both UK Company and NonUK Details Supplied" in {

          val model = deemedParentModelUkCompany.copy(countryOfIncorporation = Some(nonUkCountryCode))
          leftSideError(model.validate).errorMessage shouldBe DeemedParentWrongDetailsSuppliedError(model).errorMessage
        }

        "Both UK Company and NonUK Details Supplied when uk is false" in {
          val model = deemedParentModelUkCompany.copy(isUk = false, countryOfIncorporation = Some(nonUkCountryCode))
          leftSideError(model.validate).errorMessage shouldBe DeemedParentWrongDetailsSuppliedError(model).errorMessage
        }

        "Both UK Partnership and NonUk Details Supplied" in {

          val model = deemedParentModelUkPartnership.copy(countryOfIncorporation = Some(nonUkCountryCode))
          leftSideError(model.validate).errorMessage shouldBe DeemedParentWrongDetailsSuppliedError(model).errorMessage
        }

        "Both UK Partnership and NonUk Details Supplied when Uk is false" in {
          val model = deemedParentModelUkPartnership.copy(isUk = false, countryOfIncorporation = Some(nonUkCountryCode))
          leftSideError(model.validate).errorMessage shouldBe DeemedParentWrongDetailsSuppliedError(model).errorMessage
        }

        "IsUK and CountryOfIncorporation is supplied" in {

          val model = deemedParentModelNonUkCompany.copy(isUk = true)
          leftSideError(model.validate).errorMessage shouldBe UKDeemedMissingUTR(model).errorMessage
        }

        "NonUK and No SAUTR is supplied" in {

          val model = deemedParentModelNonUkCompany.copy(isUk = false, sautr = Some(UTRModel("1123456789")))
          leftSideError(model.validate).errorMessage shouldBe DeemedParentWrongDetailsSuppliedError(model).errorMessage
        }

        "NonUK and missing Country of Incorporation is supplied" in {

          val model = deemedParentModelNonUkCompany.copy(isUk = false, countryOfIncorporation = None)
          leftSideError(model.validate).errorMessage shouldBe NonUKDeemedParentMissingCountryOfIncorporation(
            model
          ).errorMessage
        }

        "No UTR or Country of Incorporation and IsUk is true" in {

          val model =
            deemedParentModelUkPartnership.copy(isUk = true, ctutr = None, sautr = None, countryOfIncorporation = None)
          leftSideError(model.validate).errorMessage shouldBe UKDeemedMissingUTR(model).errorMessage
        }

        "LEI is supplied but invalid" in {
          val model =
            deemedParentModelUkPartnership.copy(legalEntityIdentifier = Some(LegalEntityIdentifierModel("123")))
          leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
            LegalEntityIdentifierModel("123")
          ).errorMessage
        }

        "LEI is not supplied" in {
          val model = deemedParentModelUkPartnership.copy(legalEntityIdentifier = Some(LegalEntityIdentifierModel("")))
          leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
            LegalEntityIdentifierModel("")
          ).errorMessage
        }

        "LEI is to long" in {
          val model = deemedParentModelUkPartnership.copy(legalEntityIdentifier =
            Some(LegalEntityIdentifierModel("QWERTYUIOPASDFGHJq11QWERTYUIOPASDFGHJq11"))
          )

          leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
            LegalEntityIdentifierModel("QWERTYUIOPASDFGHJq11QWERTYUIOPASDFGHJq11")
          ).errorMessage
        }

        "LEI is invalid" in {
          val model = deemedParentModelUkPartnership.copy(legalEntityIdentifier =
            Some(LegalEntityIdentifierModel("QWERTYUIOPASDFGHJAA1"))
          )

          leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
            LegalEntityIdentifierModel("QWERTYUIOPASDFGHJAA1")
          ).errorMessage
        }

        "LEI is contains an invalid character" in {
          val model = deemedParentModelUkPartnership.copy(legalEntityIdentifier =
            Some(LegalEntityIdentifierModel("QWERTYUIOPASDFGHJ"))
          )
          leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
            LegalEntityIdentifierModel("QWERTYUIOPASDFGHJ")
          ).errorMessage
        }
      }
    }
  }
}

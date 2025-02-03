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

import data.UltimateParentConstants.*
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.{CompanyNameModel, LegalEntityIdentifierModel, UTRModel}

class UltimateParentValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "UltimateParentValidator" should {

    "Return valid" when {

      "Uk fields are populated and non-Uk fields are not populated" in {
        val model = ultimateParentModelUkCompany

        rightSide(model.validate) shouldBe model
      }

      "NonUk fields are populated and Uk fields are not populated" in {

        val model = ultimateParentModelNonUkCompany

        rightSide(model.validate) shouldBe model
      }

      "UK Company Details Supplied" in {

        val model = ultimateParentModelUkCompany

        rightSide(model.validate) shouldBe model
      }

      "UK Partnership Details Supplied" in {

        val model = ultimateParentModelUkPartnership

        rightSide(model.validate) shouldBe model
      }

      "NonUK Company Details Supplied" in {

        val model = ultimateParentModelNonUkCompany

        rightSide(model.validate) shouldBe model
      }

    }

    "Return invalid" when {

      "Company name" when {

        "Company name is empty" in {
          val model = ultimateParentModelUkCompany.copy(companyName = CompanyNameModel(""))
          leftSideError(model.validate).errorMessage shouldBe CompanyNameLengthError("").errorMessage
        }

        "Company name contains invalid characters" in {
          val model = ultimateParentModelUkCompany.copy(companyName = CompanyNameModel("ʰʲʺ£$%˦˫qw"))
          leftSideError(model.validate).errorMessage shouldBe CompanyNameCharactersError("ʰʲʺ£$%˦˫qw").errorMessage
        }

        s"Company name is longer that $companyNameMaxLength" in {
          val model = ultimateParentModelUkCompany.copy(companyName = companyNameTooLong)
          leftSideError(model.validate).errorMessage shouldBe CompanyNameLengthError(
            "a" * (companyNameMaxLength + 1)
          ).errorMessage
        }
      }

      "CTUTR is invalid" in {
        val model = ultimateParentModelUkCompany.copy(ctutr = Some(invalidUtr))
        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "CTUTR contains invalid characters" in {
        val model = ultimateParentModelUkCompany.copy(ctutr = Some(UTRModel("ʰʲʺ˦˫˥ʺ˦˫˥")))
        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(UTRModel("ʰʲʺ˦˫˥ʺ˦˫˥")).errorMessage
      }

      "CTUTR is empty" in {
        val model = ultimateParentModelUkCompany.copy(ctutr = Some(UTRModel("")))
        leftSideError(model.validate).errorMessage shouldBe UTRLengthError(UTRModel("")).errorMessage
      }

      "CTUTR is to short" in {
        leftSideError(
          ultimateParentModelUkCompany.copy(ctutr = Some(UTRModel("1"))).validate
        ).errorMessage shouldBe UTRLengthError(invalidShortUtr).errorMessage
      }

      "CTUTR is to long" in {
        leftSideError(
          ultimateParentModelUkCompany.copy(ctutr = Some(UTRModel("11234567890"))).validate
        ).errorMessage shouldBe UTRLengthError(invalidLongUtr).errorMessage
      }

      "SAUTR is invalid" in {
        val model = ultimateParentModelUkCompany.copy(ctutr = None, sautr = Some(invalidUtr))

        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "SAUTR is to short" in {
        val model = ultimateParentModelUkCompany.copy(ctutr = None, sautr = Some(UTRModel("1")))

        leftSideError(model.validate).errorMessage shouldBe UTRLengthError(invalidShortUtr).errorMessage
      }

      "SAUTR is to long" in {
        val model = ultimateParentModelUkCompany.copy(ctutr = None, sautr = Some(UTRModel("11234567890")))

        leftSideError(model.validate).errorMessage shouldBe UTRLengthError(invalidLongUtr).errorMessage
      }

      "SAUTR is empty" in {
        val model = ultimateParentModelUkCompany.copy(ctutr = None, sautr = Some(UTRModel("")))

        leftSideError(model.validate).errorMessage shouldBe UTRLengthError(UTRModel("")).errorMessage
      }

      "SAUTR contains invalid characters" in {
        val model = ultimateParentModelUkCompany.copy(ctutr = None, sautr = Some(UTRModel("ʰʲʺ˦˫˥ʺ˦˫˥")))

        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(UTRModel("ʰʲʺ˦˫˥ʺ˦˫˥")).errorMessage
      }

      "CountryOfIncorporation is invalid" in {
        val model = ultimateParentModelNonUkCompany.copy(countryOfIncorporation = Some(invalidCountryCode))
        leftSideError(model.validate).errorMessage shouldBe CountryCodeValueError(invalidCountryCode).errorMessage
      }

      "wrong models given" when {

        "Both UK Company and Partnership Details Supplied" in {
          val model = ultimateParentModelUkCompany.copy(sautr = Some(sautr))
          leftSideError(model.validate).errorMessage shouldBe UltimateParentWrongDetailsSuppliedError(
            model
          ).errorMessage
        }

        "Both UK Company and NonUK Details Supplied" in {
          val model = ultimateParentModelUkCompany.copy(countryOfIncorporation = Some(nonUkCountryCode))
          leftSideError(model.validate).errorMessage shouldBe UltimateParentWrongDetailsSuppliedError(
            model
          ).errorMessage
        }

        "Both UK Company and NonUK Details Supplied when uk is false" in {
          val model = ultimateParentModelUkCompany.copy(isUk = false, countryOfIncorporation = Some(nonUkCountryCode))
          leftSideError(model.validate).errorMessage shouldBe UltimateParentWrongDetailsSuppliedError(
            model
          ).errorMessage
        }

        "Both UK Partnership and NonUk Details Supplied" in {
          val model = ultimateParentModelUkPartnership.copy(countryOfIncorporation = Some(nonUkCountryCode))
          leftSideError(model.validate).errorMessage shouldBe UltimateParentWrongDetailsSuppliedError(
            model
          ).errorMessage
        }

        "Both UK Partnership and NonUk Details Supplied when uk is false" in {
          val model =
            ultimateParentModelUkPartnership.copy(isUk = false, countryOfIncorporation = Some(nonUkCountryCode))
          leftSideError(model.validate).errorMessage shouldBe UltimateParentWrongDetailsSuppliedError(
            model
          ).errorMessage
        }

        "IsUK and CountryOfIncorporation is supplied" in {
          val model = ultimateParentModelNonUkCompany.copy(isUk = true)
          leftSideError(model.validate).errorMessage shouldBe UKParentMissingUTR(model).errorMessage
        }

        "IsUk is false and ctutr is supplied by default" in {
          val model = ultimateParentModelUkCompany.copy(isUk = false)
          leftSideError(model.validate).errorMessage shouldBe NonUKUltimateParentMissingCountryOfIncorporation(
            model
          ).errorMessage
        }

        "NonUK and CTUTR is supplied" in {

          val model = ultimateParentModelNonUkCompany.copy(ctutr = Some(ctutr), countryOfIncorporation = None)
          leftSideError(model.validate).errorMessage shouldBe NonUKUltimateParentMissingCountryOfIncorporation(
            model
          ).errorMessage
        }

        "NonUK and no country of incorporation is supplied" in {
          val model = ultimateParentModelUkPartnership.copy(
            isUk = false,
            ctutr = None,
            sautr = None,
            countryOfIncorporation = None
          )
          leftSideError(model.validate).errorMessage shouldBe NonUKUltimateParentMissingCountryOfIncorporation(
            model
          ).errorMessage
        }

        "No UTR or Country of Incorporation" in {
          val model = ultimateParentModelUkPartnership.copy(
            isUk = true,
            ctutr = None,
            sautr = None,
            countryOfIncorporation = None
          )
          leftSideError(model.validate).errorMessage shouldBe UKParentMissingUTR(model).errorMessage
        }

        "IsUk = false, a SAUTR cannot be populated" in {
          val model = ultimateParentModelUkPartnership.copy(isUk = false, sautr = Some(sautr))
          leftSideError(model.validate).errorMessage shouldBe NonUKUltimateParentMissingCountryOfIncorporation(
            model
          ).errorMessage
        }

        "LEI is supplied but invalid" in {
          val model =
            ultimateParentModelUkPartnership.copy(legalEntityIdentifier = Some(LegalEntityIdentifierModel("123")))
          leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
            LegalEntityIdentifierModel("123")
          ).errorMessage
        }

        "LEI is not supplied" in {
          val model =
            ultimateParentModelUkPartnership.copy(legalEntityIdentifier = Some(LegalEntityIdentifierModel("")))
          leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
            LegalEntityIdentifierModel("")
          ).errorMessage
        }

        "LEI is too long" in {
          val model = ultimateParentModelUkPartnership.copy(
            legalEntityIdentifier = Some(LegalEntityIdentifierModel("QWERTYUIOPASDFGHJq11QWERTYUIOPASDFGHJq11"))
          )

          leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
            LegalEntityIdentifierModel("QWERTYUIOPASDFGHJq11QWERTYUIOPASDFGHJq11")
          ).errorMessage
        }

        "LEI is too short" in {
          val model = ultimateParentModelUkPartnership.copy(
            legalEntityIdentifier = Some(LegalEntityIdentifierModel("QWERTYUIOPA"))
          )

          leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
            LegalEntityIdentifierModel("QWERTYUIOPA")
          ).errorMessage
        }

        "LEI is invalid values" in {
          val model = ultimateParentModelUkPartnership.copy(
            legalEntityIdentifier = Some(LegalEntityIdentifierModel("QWERTYUIOPASDFGHJAA1"))
          )

          leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
            LegalEntityIdentifierModel("QWERTYUIOPASDFGHJAA1")
          ).errorMessage
        }

        "LEI is contains an invalid character" in {
          val model = ultimateParentModelUkPartnership.copy(legalEntityIdentifier =
            Some(LegalEntityIdentifierModel("QWERTYUIOPASDFGHJ!11"))
          )

          leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(
            LegalEntityIdentifierModel("QWERTYUIOPASDFGHJ!11")
          ).errorMessage
        }
      }
    }
  }
}

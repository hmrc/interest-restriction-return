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

import assets.DeemedParentConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.{CompanyNameModel, LegalEntityIdentifierModel}

class DeemedParentValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Deemed Parent Validation" should {

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

        s"Company name is longer that ${
          companyNameMaxLength
        }" in {
          val model = deemedParentModelUkCompany.copy(companyName = companyNameTooLong)
          leftSideError(model.validate).errorMessage shouldBe CompanyNameLengthError("a" * (companyNameMaxLength + 1)).errorMessage
        }
      }

      "CTUTR is invalid" in {
        val model = deemedParentModelUkCompany.copy(
          ctutr = Some(invalidUtr))

        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "SAUTR is invalid" in {
        val model = deemedParentModelUkCompany.copy(
          ctutr = None,
          sautr = Some(invalidUtr))

        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "CountryOfIncorporation is invalid" in {

        val model = deemedParentModelNonUkCompany.copy(
          countryOfIncorporation = Some(invalidCountryCode))

        leftSideError(model.validate).errorMessage shouldBe CountryCodeValueError(invalidCountryCode).errorMessage
      }

      "wrong models given" when {

        "Both UK Company and Partnership Details Supplied" in {

          val model = deemedParentModelUkCompany.copy(sautr = Some(sautr))

          leftSideError(model.validate).errorMessage shouldBe WrongDeemedParentIsUkCompanyAndPartnership(model).errorMessage
        }

        "Both UK Company and NonUK Details Supplied" in {

          val model = deemedParentModelUkCompany.copy(countryOfIncorporation = Some(nonUkCountryCode))

          leftSideError(model.validate).errorMessage shouldBe WrongDeemedParentIsUKCompanyAndNonUK(model).errorMessage
        }

        "Both UK Partnership and NonUk Details Supplied" in {

          val model = deemedParentModelUkPartnership.copy(countryOfIncorporation = Some(nonUkCountryCode))

          leftSideError(model.validate).errorMessage shouldBe WrongDeemedParentIsUkPartnershipAndNonUKCompany(model).errorMessage
        }

        "IsUK and CountryOfIncorporation is supplied" in {

          val model = deemedParentModelNonUkCompany.copy(
            isUk = true
          )

          leftSideError(model.validate).errorMessage shouldBe WrongDeemedParentIsUkPartnershipAndNonUKCompany(model).errorMessage
        }

        "NonUK and SAUTR is supplied" in {

          val model = deemedParentModelUkCompany.copy(
            isUk = false
          )

          leftSideError(model.validate).errorMessage shouldBe WrongDeemedParentIsUkPartnershipAndNonUKCompany(model).errorMessage
        }

        "NonUK and CTUTR is supplied" in {

          val model = deemedParentModelNonUkCompany.copy(
            countryOfIncorporation = None
          )

          leftSideError(model.validate).errorMessage shouldBe NonUKDeemedParentMissingCountryOfIncorporation(model).errorMessage
        }

        "NonUK and no country of incorporation is supplied" in {

          val model = deemedParentModelUkPartnership.copy(
            isUk = false
          )

          leftSideError(model.validate).errorMessage shouldBe WrongDeemedParentIsUkPartnershipAndNonUKCompany(model).errorMessage
        }

        "No UTR or Country of Incorporation" in {

          val model = deemedParentModelUkPartnership.copy(ctutr = None, sautr = None, countryOfIncorporation = None)

          leftSideError(model.validate).errorMessage shouldBe NoUTROrCountryCodeOnDeemedParent(model).errorMessage
        }

        "LEI is supplied but invalid" in {
          val model = deemedParentModelUkPartnership.copy(legalEntityIdentifier = Some(LegalEntityIdentifierModel("123")))
          leftSideError(model.validate).errorMessage shouldBe LegalEntityIdentifierCharacterError(LegalEntityIdentifierModel("123")).errorMessage
        }
      }


    }
  }

}

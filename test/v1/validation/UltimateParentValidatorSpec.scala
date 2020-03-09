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

import assets.UltimateParentConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.CompanyNameModel

class UltimateParentValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Ultimate Parent Validation" should {

    "Return valid" when {

      "Uk fields are populated and non-Uk fields are not populated" in {
        val model = ultimateParentModelUkCompany

        rightSide(model.validate) shouldBe model
      }

      "NonUk fields are populated and Uk fields are not populated" in {

        val model = ultimateParentModelNonUkCompany

        rightSide(model.validate) shouldBe model
      }

      "knownAs" when {

        "is not supplied" in {
          val model = ultimateParentModelNonUkCompany.copy(
            knownAs = None)

          rightSide(model.validate) shouldBe model
        }

        "is empty" in {
          val model = ultimateParentModelNonUkCompany.copy(
            knownAs = Some(""))

          rightSide(model.validate) shouldBe model
        }
      }
    }

    "Return invalid" when {

      "Uk and NonUK fields are populated" in {

        val model = ultimateParentModelMax

        leftSideError(model.validate).message shouldBe UltimateParentCannotBeUkAndNonUk(model).message
      }

      "Company name" when {

        "Company name is empty" in {
          val model = ultimateParentModelUkCompany.copy(companyName = CompanyNameModel(""))
          leftSideError(model.validate).message shouldBe CompanyNameLengthError("").message
        }

        s"Company name is longer that ${companyNameMaxLength}" in {
          val model = ultimateParentModelUkCompany.copy(companyName = companyNameTooLong)
          leftSideError(model.validate).message shouldBe CompanyNameLengthError("a" * (companyNameMaxLength + 1)).message
        }
      }

      "CTUTR is invalid" in {
        val model = ultimateParentModelUkCompany.copy(
          ctutr = Some(invalidUtr))

        leftSideError(model.validate).message shouldBe UTRChecksumError(invalidUtr).message
      }

      "CRN is invalid" in {
        val model = ultimateParentModelUkCompany.copy(
          crn = Some(invalidCrn))

        leftSideError(model.validate).message shouldBe CRNFormatCheck(invalidCrn).message
      }

      "CountryOfIncorporation is invalid" in {
        val model = ultimateParentModelNonUkCompany.copy(
          countryOfIncorporation = Some(invalidCountryCode))

        leftSideError(model.validate).message shouldBe CountryCodeValueError(invalidCountryCode).message
      }

      "Both versions of UTR are supplied for Uk Company" in {
        val model = ultimateParentModelUkCompany.copy(
          sautr = Some(sautr))

        leftSideError(model.validate).message shouldBe UltimateParentUTRSuppliedError(model).message
      }
    }
  }
}

/*
 * Copyright 2022 HM Revenue & Customs
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

import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.CountryCodeModel

class CountryCodeValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Country Code Validation" when {

    "Country Code is supplied and is not the correct length as its too short" in {
      val model  = CountryCodeModel("A")
      leftSideError(model.validate, 0).errorMessage shouldBe CountryCodeLengthError(model).errorMessage
      leftSideError(model.validate, 1).errorMessage shouldBe CountryCodeValueError(model).errorMessage
    }

    "Country Code is supplied and is not the correct length as its too long" in {
      val model  = CountryCodeModel("AAA")
      leftSideError(model.validate, 0).errorMessage shouldBe CountryCodeLengthError(model).errorMessage
      leftSideError(model.validate, 1).errorMessage shouldBe CountryCodeValueError(model).errorMessage
    }

    "Invalid Country Code supplied" in {
      leftSideError(invalidCountryCode.validate).errorMessage shouldBe CountryCodeValueError(invalidCountryCode).errorMessage
    }

    "Valid Country Code supplied" in {
      rightSide(nonUkCountryCode.validate) shouldBe nonUkCountryCode
    }
  }

  "Country Code Invalid List" when {
    "DD" in {
      leftSideError(CountryCodeModel("DD").validate).errorMessage shouldBe CountryCodeValueError(CountryCodeModel("DD")).errorMessage
    }

    "EU" in {
      leftSideError(CountryCodeModel("EU").validate).errorMessage shouldBe CountryCodeValueError(CountryCodeModel("EU")).errorMessage
    }

    "NT" in {
      leftSideError(CountryCodeModel("NT").validate).errorMessage shouldBe CountryCodeValueError(CountryCodeModel("NT")).errorMessage
    }

    "TP" in {
      leftSideError(CountryCodeModel("TP").validate).errorMessage shouldBe CountryCodeValueError(CountryCodeModel("TP")).errorMessage
    }

    "UK" in {
      leftSideError(CountryCodeModel("UK").validate).errorMessage shouldBe CountryCodeValueError(CountryCodeModel("UK")).errorMessage
    }

    "UN" in {
      leftSideError(CountryCodeModel("UN").validate).errorMessage shouldBe CountryCodeValueError(CountryCodeModel("UN")).errorMessage
    }

    "GB" in {
      leftSideError(CountryCodeModel("GB").validate).errorMessage shouldBe CountryCodeCantBeGB(CountryCodeModel("GB")).errorMessage
    }
  }
}

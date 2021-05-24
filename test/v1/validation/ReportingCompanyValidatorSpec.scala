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

import assets.ReportingCompanyConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.{CompanyNameModel, UTRModel}

class ReportingCompanyValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Reporting Company Validation" should {

    "Return valid" when {

      "a valid Reporting Company model is validated" in {
        rightSide(reportingCompanyModel.validate) shouldBe reportingCompanyModel
      }
    }

    "Return invalid" when {

      "Company name" when {

        "Company name is empty" in {
          leftSideError(reportingCompanyModel.copy(
            companyName = CompanyNameModel("")).validate).errorMessage shouldBe CompanyNameLengthError("").errorMessage
        }

        "Company name contains invalid characters" in {
          leftSideError(reportingCompanyModel.copy(
            companyName = CompanyNameModel("ʰʲʺ˦˫˥ʺ˦˫˥")).validate).errorMessage shouldBe CompanyNameCharactersError("ʰʲʺ˦˫˥ʺ˦˫˥").errorMessage
        }

        s"Company name is longer that ${companyNameMaxLength}" in {
          leftSideError(reportingCompanyModel.copy(
            companyName = companyNameTooLong).validate).errorMessage shouldBe CompanyNameLengthError("a" * (companyNameMaxLength + 1)).errorMessage
        }
      }

      "CTUTR is invalid" in {
        leftSideError(reportingCompanyModel.copy(ctutr = invalidUtr).validate).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "CTUTR is empty" in {
        leftSideError(reportingCompanyModel.copy(ctutr = UTRModel("")).validate).errorMessage shouldBe UTRLengthError(UTRModel("")).errorMessage
      }

      "CTUTR is contains invalid characters" in {
        leftSideError(reportingCompanyModel.copy(
          ctutr = UTRModel("ʰʲʺ˦˫˥ʺ˦˫˥")).validate).errorMessage shouldBe UTRChecksumError(UTRModel("ʰʲʺ˦˫˥ʺ˦˫˥")).errorMessage
      }

      "CTUTR is to short" in {
        leftSideError(reportingCompanyModel.copy(ctutr = UTRModel("1")).validate).errorMessage shouldBe UTRLengthError(invalidShortUtr).errorMessage
      }

      "CTUTR is to long" in {
        leftSideError(reportingCompanyModel.copy(ctutr = UTRModel("11234567890")).validate).errorMessage shouldBe UTRLengthError(invalidLongUtr).errorMessage
      }
    }
  }
}

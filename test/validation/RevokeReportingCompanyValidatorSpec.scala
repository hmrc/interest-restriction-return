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

import assets.RevokeReportingCompanyConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec

class RevokeReportingCompanyValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Revoke Reporting Company Validation" should {

    "Return valid" when {

      "a valid Revoke Reporting Company model is validated" in {
        rightSide(revokeReportingCompanyModel.validate) shouldBe revokeReportingCompanyModel
      }
    }

    "Return invalid" when {

      "a company submitting on behalf doesn't supply their company details" in {
        leftSideError(revokeReportingCompanyModel.copy(
          isReportingCompanyRevokingItself = false, companyMakingRevocation = None).validate).errorMessage shouldBe
          CompanyMakingAppointmentMustSupplyDetails().errorMessage
      }

      "the declaration hasn't been declared" in {
        leftSideError(revokeReportingCompanyModel.copy(declaration = false).validate).errorMessage shouldBe
          DeclaredFiftyPercentOfEligibleCompanies(declaration = false).errorMessage
      }
    }

  }
}

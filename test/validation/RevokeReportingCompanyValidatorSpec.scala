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
import models.IdentityOfCompanySubmittingModel
import play.api.libs.json.JsPath
import utils.BaseSpec

class RevokeReportingCompanyValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Revoke Reporting Company Validation" should {

    "Return valid" when {

      "a Reporting Company revokes itself" in {
        val testingModel = revokeReportingCompanyModel.copy(companyMakingRevocation = None)
        rightSide(testingModel.validate) shouldBe testingModel
      }

      "a Reporting Company is being revoked by another company and supplies their details" in {
        val testingModel = revokeReportingCompanyModel.copy(isReportingCompanyRevokingItself = false)
        rightSide(testingModel.validate) shouldBe testingModel
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

      "a Company revokes itself but still supplies the revoking company details" in {
        leftSideError(revokeReportingCompanyModel.copy(isReportingCompanyRevokingItself = true).validate).errorMessage shouldBe
          DetailsNotNeededIfCompanyRevokingItself(revokeReportingCompanyModel.companyMakingRevocation.get).errorMessage
      }
    }

  }
}

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

package validation.revokeReportingCompany

import assets.IdentityOfCompanySubmittingConstants.identityOfCompanySubmittingModelMax
import assets.revokeReportingCompany.RevokeReportingCompanyConstants.revokeReportingCompanyModelMax
import play.api.libs.json.JsPath
import utils.BaseSpec

class RevokeReportingCompanyValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Revoke Reporting Company Validation" should {

    "Return valid" when {

      "a Reporting Company revokes itself" in {
        val testingModel = revokeReportingCompanyModelMax

        rightSide(testingModel.validate) shouldBe testingModel
      }

      "a Reporting Company is being revoked by another company and supplies their details" in {
        val testingModel = revokeReportingCompanyModelMax.copy(isReportingCompanyRevokingItself = false,
          companyMakingRevocation = Some(identityOfCompanySubmittingModelMax))

        rightSide(testingModel.validate) shouldBe testingModel
      }

      "a revoking company is the same as the ultimate parent, and ultimate parent was not supplied, " in {
        val testingModel = revokeReportingCompanyModelMax.copy(
          reportingCompany = revokeReportingCompanyModelMax.reportingCompany.copy(sameAsUltimateParent = true),
          ultimateParent = None)

        rightSide(testingModel.validate) shouldBe testingModel
      }
    }

    "Return invalid" when {

      "a revoking company is the same as the ultimate parent, and ultimate parent was supplied, " in {
        val testingModel = revokeReportingCompanyModelMax.copy(
          reportingCompany = revokeReportingCompanyModelMax.reportingCompany.copy(sameAsUltimateParent = true))

        leftSideErrorLength(testingModel.validate) shouldBe 1

        leftSideError(testingModel.validate).errorMessage shouldBe
          UltimateParentCompanyIsSuppliedRevoke(revokeReportingCompanyModelMax.ultimateParent.get).errorMessage
      }

      "a company submitting on behalf doesn't supply their company details" in {
        val testModel = revokeReportingCompanyModelMax.copy(
          isReportingCompanyRevokingItself = false)

        leftSideErrorLength(testModel.validate) shouldBe 1

        leftSideError(testModel.validate).errorMessage shouldBe
          CompanyMakingAppointmentMustSupplyDetails().errorMessage
      }

      "the declaration hasn't been declared" in {
        val testModel = revokeReportingCompanyModelMax.copy(declaration = false)

        leftSideErrorLength(testModel.validate) shouldBe 1

        leftSideError(testModel.validate).errorMessage shouldBe
          DeclaredFiftyPercentOfEligibleCompanies(declaration = false).errorMessage
      }

      "the declaration hasn't been declared and the company is revoking itself but still supplies revoking company details" in {
        val testModel = revokeReportingCompanyModelMax.copy(declaration = false,companyMakingRevocation = Some(identityOfCompanySubmittingModelMax))

        leftSideErrorLength(testModel.validate) shouldBe 2

        leftSideError(testModel.validate,1).errorMessage shouldBe
          DeclaredFiftyPercentOfEligibleCompanies(declaration = false).errorMessage

        leftSideError(testModel.validate).errorMessage shouldBe
          DetailsNotNeededIfCompanyRevokingItself(identityOfCompanySubmittingModelMax).errorMessage
      }

      "a Company revokes itself but still supplies the revoking company details" in {
        val testModel = revokeReportingCompanyModelMax.copy(isReportingCompanyRevokingItself = true,
          companyMakingRevocation = Some(identityOfCompanySubmittingModelMax))

        leftSideErrorLength(testModel.validate) shouldBe 1

        leftSideError(testModel.validate).errorMessage shouldBe
          DetailsNotNeededIfCompanyRevokingItself(identityOfCompanySubmittingModelMax).errorMessage
      }
    }

  }
}

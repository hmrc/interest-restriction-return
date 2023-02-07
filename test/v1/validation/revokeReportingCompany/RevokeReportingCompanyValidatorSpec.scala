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

package v1.validation.revokeReportingCompany

import assets.IdentityOfCompanySubmittingConstants.identityOfCompanySubmittingModelMax
import assets.revokeReportingCompany.RevokeReportingCompanyConstants.revokeReportingCompanyModelMax
import play.api.libs.json.JsPath
import utils.BaseSpec
import assets.AuthorisingCompanyConstants._
import v1.models.{CompanyNameModel, CountryCodeModel, UTRModel}
import v1.validation.errors._

class RevokeReportingCompanyValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "RevokeReportingCompanyValidator" should {

    "Return valid" when {

      "a Reporting Company revokes itself" in {
        val testingModel = revokeReportingCompanyModelMax

        rightSide(testingModel.validate) shouldBe testingModel
      }

      "a Reporting Company is being revoked by another company and supplies their details" in {
        val testingModel = revokeReportingCompanyModelMax.copy(
          isReportingCompanyRevokingItself = false,
          companyMakingRevocation = Some(identityOfCompanySubmittingModelMax)
        )

        rightSide(testingModel.validate) shouldBe testingModel
      }

      "a revoking company is the same as the ultimate parent, and ultimate parent was not supplied, " in {
        val testingModel = revokeReportingCompanyModelMax.copy(
          reportingCompany = revokeReportingCompanyModelMax.reportingCompany.copy(sameAsUltimateParent = true),
          ultimateParentCompany = None
        )

        rightSide(testingModel.validate) shouldBe testingModel
      }

      "appointingCompanies does not contain duplicates" in {
        val companies    =
          Seq(authorisingCompanyModel, authorisingCompanyModel.copy(companyName = CompanyNameModel("Company ABC")))
        val testingModel = revokeReportingCompanyModelMax.copy(authorisingCompanies = companies)

        rightSide(testingModel.validate) shouldBe testingModel
      }
    }

    "Return invalid" when {

      "a revoking company is the same as the ultimate parent, and ultimate parent was supplied, " in {
        val testingModel = revokeReportingCompanyModelMax.copy(
          reportingCompany = revokeReportingCompanyModelMax.reportingCompany.copy(sameAsUltimateParent = true)
        )

        leftSideErrorLength(testingModel.validate) shouldBe 1

        leftSideError(testingModel.validate).errorMessage shouldBe
          UltimateParentCompanyIsSupplied(revokeReportingCompanyModelMax.ultimateParentCompany.get).errorMessage
      }

      "a revoking company does not contain an authorising company" in {
        val testingModel = revokeReportingCompanyModelMax.copy(authorisingCompanies = Seq())
        leftSideError(testingModel.validate).errorMessage shouldBe AuthorisingCompaniesEmpty.errorMessage
      }

      "a revoking company is NOT the same as the ultimate parent, and ultimate parent was NOT supplied, " in {
        val testingModel = revokeReportingCompanyModelMax.copy(
          reportingCompany = revokeReportingCompanyModelMax.reportingCompany.copy(sameAsUltimateParent = false),
          ultimateParentCompany = None
        )

        leftSideErrorLength(testingModel.validate) shouldBe 1

        leftSideError(testingModel.validate).errorMessage shouldBe
          UltimateParentCompanyIsNotSupplied.errorMessage
      }

      "a company submitting on behalf doesn't supply their company details" in {
        val testModel = revokeReportingCompanyModelMax.copy(isReportingCompanyRevokingItself = false)

        leftSideErrorLength(testModel.validate) shouldBe 1

        leftSideError(testModel.validate).errorMessage shouldBe CompanyMakingAppointmentMustSupplyDetails.errorMessage
      }

      "the declaration hasn't been declared" in {
        val testModel = revokeReportingCompanyModelMax.copy(declaration = false)

        leftSideErrorLength(testModel.validate) shouldBe 1

        leftSideError(testModel.validate).errorMessage shouldBe
          DeclaredFiftyPercentOfEligibleCompanies(declaration = false).errorMessage
      }

      "the declaration hasn't been declared and the company is revoking itself but still supplies revoking company details" in {
        val testModel = revokeReportingCompanyModelMax.copy(
          declaration = false,
          companyMakingRevocation = Some(identityOfCompanySubmittingModelMax)
        )

        leftSideErrorLength(testModel.validate) shouldBe 2

        leftSideError(testModel.validate, 1).errorMessage shouldBe
          DeclaredFiftyPercentOfEligibleCompanies(declaration = false).errorMessage

        leftSideError(testModel.validate).errorMessage shouldBe
          DetailsNotNeededIfCompanyRevokingItself(identityOfCompanySubmittingModelMax).errorMessage
      }

      "a Company revokes itself but still supplies the revoking company details" in {
        val testModel = revokeReportingCompanyModelMax.copy(
          isReportingCompanyRevokingItself = true,
          companyMakingRevocation = Some(identityOfCompanySubmittingModelMax)
        )

        leftSideErrorLength(testModel.validate) shouldBe 1

        leftSideError(testModel.validate).errorMessage shouldBe
          DetailsNotNeededIfCompanyRevokingItself(identityOfCompanySubmittingModelMax).errorMessage
      }

      "appointingCompanies contains duplicates" in {
        val companies    = Seq(authorisingCompanyModel, authorisingCompanyModel)
        val testingModel = revokeReportingCompanyModelMax.copy(authorisingCompanies = companies)

        leftSideError(testingModel.validate).errorMessage shouldBe AuthorisingCompaniesContainsDuplicates.errorMessage
      }

      "CompanyMakingRevocation's CompanyName contains invalid characters" in {

        val companyNameInvalid = CompanyNameModel("ʰʲʺ˦˫˥ʺ˦˫˥")
        val identityOfComp     = identityOfCompanySubmittingModelMax.copy(companyName = companyNameInvalid)
        val testModel          = revokeReportingCompanyModelMax.copy(
          isReportingCompanyRevokingItself = true,
          companyMakingRevocation = Some(identityOfComp)
        )

        leftSideError(testModel.validate).errorMessage shouldBe DetailsNotNeededIfCompanyRevokingItself(
          identityOfComp
        ).errorMessage
      }

      "CompanyMakingRevocation's CompanyNameis too long" in {

        val identityOfComp = identityOfCompanySubmittingModelMax.copy(companyName = companyNameTooLong)
        val testModel      = revokeReportingCompanyModelMax.copy(
          isReportingCompanyRevokingItself = true,
          companyMakingRevocation = Some(identityOfComp)
        )

        leftSideError(testModel.validate).errorMessage shouldBe DetailsNotNeededIfCompanyRevokingItself(
          identityOfComp
        ).errorMessage
      }

      "CompanyMakingRevocation's CompanyName is empty" in {

        val companyNameInvalid = CompanyNameModel("")
        val identityOfComp     = identityOfCompanySubmittingModelMax.copy(companyName = companyNameInvalid)
        val testModel          = revokeReportingCompanyModelMax.copy(
          isReportingCompanyRevokingItself = true,
          companyMakingRevocation = Some(identityOfComp)
        )

        leftSideError(testModel.validate).errorMessage shouldBe DetailsNotNeededIfCompanyRevokingItself(
          identityOfComp
        ).errorMessage
      }

      "CompanyMakingRevocation's ctutr is empty" in {

        val identityOfComp = identityOfCompanySubmittingModelMax.copy(ctutr = Some(UTRModel("")))
        val testModel      = revokeReportingCompanyModelMax.copy(
          isReportingCompanyRevokingItself = true,
          companyMakingRevocation = Some(identityOfComp)
        )

        leftSideError(testModel.validate).errorMessage shouldBe DetailsNotNeededIfCompanyRevokingItself(
          identityOfComp
        ).errorMessage
      }

      "CompanyMakingRevocation's ctutr is too long" in {

        val identityOfComp = identityOfCompanySubmittingModelMax.copy(ctutr = Some(invalidLongUtr))
        val testModel      = revokeReportingCompanyModelMax.copy(
          isReportingCompanyRevokingItself = true,
          companyMakingRevocation = Some(identityOfComp)
        )

        leftSideError(testModel.validate).errorMessage shouldBe DetailsNotNeededIfCompanyRevokingItself(
          identityOfComp
        ).errorMessage
      }

      "CompanyMakingRevocation's Country of Incorporation is empty" in {

        val identityOfComp =
          identityOfCompanySubmittingModelMax.copy(countryOfIncorporation = Some(CountryCodeModel("")))
        val testModel      = revokeReportingCompanyModelMax.copy(
          isReportingCompanyRevokingItself = true,
          companyMakingRevocation = Some(identityOfComp)
        )

        leftSideError(testModel.validate).errorMessage shouldBe DetailsNotNeededIfCompanyRevokingItself(
          identityOfComp
        ).errorMessage
      }

      "CompanyMakingRevocation's Country of Incorporation is an invalid country code" in {

        val identityOfComp = identityOfCompanySubmittingModelMax.copy(countryOfIncorporation = Some(invalidCountryCode))
        val testModel      = revokeReportingCompanyModelMax.copy(
          isReportingCompanyRevokingItself = true,
          companyMakingRevocation = Some(identityOfComp)
        )

        leftSideError(testModel.validate).errorMessage shouldBe DetailsNotNeededIfCompanyRevokingItself(
          identityOfComp
        ).errorMessage
      }
    }
  }
}

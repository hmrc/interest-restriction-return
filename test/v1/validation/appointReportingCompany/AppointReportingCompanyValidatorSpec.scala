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

package v1.validation.appointReportingCompany

import assets.IdentityOfCompanySubmittingConstants._
import assets.ReportingCompanyConstants._
import assets.UltimateParentConstants._
import assets.appointReportingCompany.AppointReportingCompanyConstants._
import utils.BaseSpec
import assets.AuthorisingCompanyConstants._
import v1.models.CompanyNameModel
import v1.validation.errors._

class AppointReportingCompanyValidatorSpec extends BaseSpec {

  "Appoint Reporting Company Validation" when {

    "Reporting Company is appointing itself" when {

      "Identity of Appointing Company is supplied" should {

        "Return invalid, as it should not be supplied" in {
          val model = appointReportingCompanyModelMin.copy(identityOfAppointingCompany = Some(identityOfCompanySubmittingModelMax))
          leftSideError(model.validate).errorMessage shouldBe IdentityOfAppointingCompanyIsSupplied(identityOfCompanySubmittingModelMax).errorMessage
        }
      }

      "Identity of Appointing Company is NOT supplied" should {

        "Return valid" in {
          val model = appointReportingCompanyModelMin.copy(reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = true))
          rightSide(model.validate) shouldBe model
        }
      }
    }

    "Reporting Company is NOT appointing itself" when {

      "Identity of Appointing Company is NOT supplied" should {
        "Return invalid, as it should be supplied" in {
          val model = appointReportingCompanyModelMax.copy(identityOfAppointingCompany = None)
          leftSideError(model.validate).errorMessage shouldBe IdentityOfAppointingCompanyIsNotSupplied.errorMessage
        }
      }

      "AuthorisingCompany contains empty Seq" should {
        "Return invalid, as there should be atleast 1 authorising company" in {
          val model = appointReportingCompanyModelMax.copy(authorisingCompanies = Nil)
          leftSideError(model.validate).errorMessage shouldBe AuthorisingCompaniesEmpty.errorMessage
        }
      }

      "Identity of Appointing Company is supplied" should {
        "Return valid" in {
          rightSide(appointReportingCompanyModelMax.validate) shouldBe appointReportingCompanyModelMax
        }
      }
    }

    "Reporting Company is the Same as the Ultimate Parent" when {

      "Ultimate Parent is supplied" should {

        "Return invalid, as it should be NOT be supplied" in {

          val model = appointReportingCompanyModelMax.copy(
            reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = true),
            ultimateParentCompany = Some(ultimateParentModelUkCompany)
          )
          leftSideError(model.validate).errorMessage shouldBe UltimateParentCompanyIsSupplied(ultimateParentModelMax).errorMessage
        }
      }

      "Ultimate Parent is not supplied" should {

        "Return valid" in {

          val model = appointReportingCompanyModelMax.copy(
            reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = true),
            ultimateParentCompany = None
          )
          rightSide(model.validate) shouldBe model
        }
      }
    }

    "Reporting Company is NOT the Same as the Ultimate Parent" when {

      "Ultimate Parent is NOT supplied" should {

        "Return invalid, as it should be supplied" in {

          val model = appointReportingCompanyModelMax.copy(
            reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = false),
            ultimateParentCompany = None
          )

          leftSideError(model.validate).errorMessage shouldBe UltimateParentCompanyIsNotSupplied.errorMessage
        }
      }

      "Ultimate Parent is supplied" should {

        "Return valid" in {

          val model = appointReportingCompanyModelMax.copy(
            reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = false),
            ultimateParentCompany = Some(ultimateParentModelUkCompany)
          )

          rightSide(model.validate) shouldBe model
        }
      }
    }

    "authorisingCompanies" should {

      "cause an error when the list contains duplicates" in {
        val companies = Seq(authorisingCompanyModel, authorisingCompanyModel)
        val testingModel = appointReportingCompanyModelMax.copy(authorisingCompanies = companies)

        leftSideError(testingModel.validate).errorMessage shouldBe AuthorisingCompaniesContainsDuplicates.errorMessage
      }

      "not cause an error when there are no duplicates" in {
        val companies = Seq(authorisingCompanyModel, authorisingCompanyModel.copy(companyName = CompanyNameModel("Company ABC")))
        val testingModel = appointReportingCompanyModelMax.copy(authorisingCompanies = companies)

        rightSide(testingModel.validate) shouldBe testingModel
      }

    }

    "fail validation when the declaration is false" in {
      val testModel = appointReportingCompanyModelMax.copy(declaration = false)

      leftSideErrorLength(testModel.validate) shouldBe 1

      leftSideError(testModel.validate).errorMessage shouldBe
        DeclaredFiftyPercentOfEligibleCompanies(declaration = false).errorMessage
    }
  }
}

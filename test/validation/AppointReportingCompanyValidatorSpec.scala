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

import assets.IdentityOfCompanySubmittingConstants._
import assets.appointReportingCompany.AppointReportingCompanyConstants._
import assets.ReportingCompanyConstants._
import assets.UltimateParentConstants._
import org.scalatest.{Matchers, WordSpec}

class AppointReportingCompanyValidatorSpec extends WordSpec with Matchers {

  "Appoint Reporting Company Validation" when {

    "Reporting Company is appointing itself" when {

      "Identity of Appointing Company is supplied" should {

        "Return invalid, as it should not be supplied" in {

          val model = appointReportingCompanyModelMin.copy(identityOfAppointingCompany = Some(identityOfCompanySubmittingModelMax))
          model.validate.toEither.left.get.head.errorMessage shouldBe IdentityOfAppointingCompanyIsSupplied(identityOfCompanySubmittingModelMax).errorMessage
        }
      }

      "Identity of Appointing Company is NOT supplied" should {

        "Return valid" in {
          appointReportingCompanyModelMin.validate.toEither.right.get shouldBe appointReportingCompanyModelMin
        }
      }
    }

    "Reporting Company is NOT appointing itself" when {

      "Identity of Appointing Company is NOT supplied" should {

        "Return invalid, as it should be supplied" in {

          val model = appointReportingCompanyModelMax.copy(identityOfAppointingCompany = None)
          model.validate.toEither.left.get.head.errorMessage shouldBe IdentityOfAppointingCompanyIsNotSupplied.errorMessage
        }
      }

      "Identity of Appointing Company is supplied" should {

        "Return valid" in {
          appointReportingCompanyModelMax.validate.toEither.right.get shouldBe appointReportingCompanyModelMax
        }
      }
    }

    "Reporting Company is the Same as the Ultimate Parent" when {

      "Ultimate Parent is supplied" should {

        "Return invalid, as it should be NOT be supplied" in {

          val model = appointReportingCompanyModelMax.copy(
            reportingCompany = reportingCompanyModelMax.copy(sameAsUltimateParent = true),
            ultimateParentCompany = Some(ultimateParentModelMax)
          )
          model.validate.toEither.left.get.head.errorMessage shouldBe UltimateParentCompanyIsSupplied(ultimateParentModelMax).errorMessage
        }
      }

      "Ultimate Parent is not supplied" should {

        "Return valid" in {

          val model = appointReportingCompanyModelMax.copy(
            reportingCompany = reportingCompanyModelMax.copy(sameAsUltimateParent = true),
            ultimateParentCompany = None
          )
          model.validate.toEither.right.get shouldBe model
        }
      }
    }

    "Reporting Company is NOT the Same as the Ultimate Parent" when {

      "Ultimate Parent is NOT supplied" should {

        "Return invalid, as it should be supplied" in {

          val model = appointReportingCompanyModelMax.copy(
            reportingCompany = reportingCompanyModelMax.copy(sameAsUltimateParent = false),
            ultimateParentCompany = None
          )
          model.validate.toEither.left.get.head.errorMessage shouldBe UltimateParentCompanyIsNotSupplied.errorMessage
        }
      }

      "Ultimate Parent is supplied" should {

        "Return valid" in {

          val model = appointReportingCompanyModelMax.copy(
            reportingCompany = reportingCompanyModelMax.copy(sameAsUltimateParent = false),
            ultimateParentCompany = Some(ultimateParentModelMax)
          )
          model.validate.toEither.right.get shouldBe model
        }
      }
    }
  }
}

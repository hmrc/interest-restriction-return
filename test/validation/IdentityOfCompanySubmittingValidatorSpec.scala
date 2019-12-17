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
import assets.ReportingCompanyConstants._
import assets.UltimateParentConstants._
import assets.appointReportingCompany.AppointReportingCompanyConstants._
import org.scalatest.{Matchers, WordSpec}

class IdentityOfCompanySubmittingValidatorSpec extends WordSpec with Matchers {

  "Identity of Company Submitting Validation" when {

    "Uk and NonUK fields are populated" should {

      "Return invalid" in {

        val model = identityOfCompanySubmittingModelMax.copy(
          nonUkCrn = Some("1234567"),
          countryOfIncorporation = Some("US")
        )

        model.validate.toEither.left.get.head.errorMessage shouldBe CannotBeUkAndNonUk(model).errorMessage
      }
    }

    "Uk fields are populated" should {

      "Return valid" in {
        val model = identityOfCompanySubmittingModelMax.copy(
          nonUkCrn = None,
          countryOfIncorporation = None
        )

        model.validate.toEither.right.get shouldBe model
      }
    }

    "NonUk fields are populated" should {

      "Return valid" in {
        val model = identityOfCompanySubmittingModelMax.copy(
          crn = None,
          ctutr = None
        )

        model.validate.toEither.right.get shouldBe model
      }
    }
  }
}

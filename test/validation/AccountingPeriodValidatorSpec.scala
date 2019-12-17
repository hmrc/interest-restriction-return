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

import java.time.LocalDate

import assets.AccountingPeriodConstants._
import org.scalatest.{Matchers, WordSpec}

class AccountingPeriodValidatorSpec extends WordSpec with Matchers {

  "Accounting Period Validation" when {

    "Return Valid" when {
      "Start date is in the past" in {
        accountingPeriodModel.validate.toEither.right.get shouldBe accountingPeriodModel
      }
    }

    "Return invalid" when {

      "Start date is in the future" in {

        val model = accountingPeriodModel.copy(
          startDate = LocalDate.now().plusDays(1)
        )
        model.validate.toEither.left.get.head.errorMessages shouldBe StartDateCannotBeInFuture.errorMessages
      }

      "End date is before start date" in {

        val model = accountingPeriodModel.copy(
          endDate = startDate.minusDays(1)
        )
        model.validate.toEither.left.get.head.errorMessages shouldBe EndDateAfterStartDate.errorMessages
      }

      "Accounting period is greater than or equal to 18 months" in {

        val model = accountingPeriodModel.copy(
          endDate = startDate.plusMonths(18)
        )
        model.validate.toEither.left.get.head.errorMessages shouldBe AccountingPeriod18MonthsMax.errorMessages
      }
      "End date is in the future" in {

        val model = accountingPeriodModel.copy(
          startDate = LocalDate.now().minusMonths(12),
          endDate = LocalDate.now().plusDays(1)
        )
        model.validate.toEither.left.get.head.errorMessages shouldBe EndDateCannotBeInTheFuture.errorMessages
      }
    }
  }
}

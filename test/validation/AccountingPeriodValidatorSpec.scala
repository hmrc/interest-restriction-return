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
import play.api.libs.json.JsPath

class AccountingPeriodValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Accounting Period Validation" when {

    "Return Valid" when {
      "Start date is in the past" in {
        rightSide(accountingPeriodModel.validate) shouldBe accountingPeriodModel
      }
    }

    "Return invalid" when {

      "Start date is in the future" in {

        val startDate = LocalDate.now().plusDays(1)
        val model = accountingPeriodModel.copy(
          startDate = startDate
        )
        leftSideError(model.validate).errorMessage shouldBe StartDateCannotBeInFuture(startDate).errorMessage
      }

      "End date is before start date" in {

        val endDate = startDate.minusDays(1)
        val model = accountingPeriodModel.copy(
          endDate = endDate
        )
        leftSideError(model.validate).errorMessage shouldBe EndDateAfterStartDate(endDate).errorMessage
      }

      "End date is equal to start date" in {

        val model = accountingPeriodModel.copy(
          endDate = startDate
        )
        leftSideError(model.validate).errorMessage shouldBe EndDateAfterStartDate(endDate).errorMessage
      }

      "Accounting period is greater than or equal to 18 months" in {

        val startDate = LocalDate.now().minusMonths(20)
        val endDate = startDate.plusMonths(18)
        val model = accountingPeriodModel.copy(
          startDate = startDate,
          endDate = endDate
        )
        leftSideError(model.validate).errorMessage shouldBe AccountingPeriod18MonthsMax(endDate).errorMessage
      }
      "End date is in the future" in {

        val endDate = LocalDate.now().plusDays(1)
        val model = accountingPeriodModel.copy(
          startDate = LocalDate.now().minusMonths(12),
          endDate = endDate
        )
        leftSideError(model.validate).errorMessage shouldBe EndDateCannotBeInTheFuture(endDate).errorMessage
      }
    }
  }
}

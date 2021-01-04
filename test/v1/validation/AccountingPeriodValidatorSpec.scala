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

import java.time.LocalDate

import assets.AccountingPeriodConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec

class AccountingPeriodValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  val minimumStartDate = LocalDate.parse("2016-10-01")

  "Accounting Period Validation" when {

    "Return Valid" when {
      "Start date is in the past" in {
        rightSide(accountingPeriodModel.validate) shouldBe accountingPeriodModel
      }

      "Start date is on 2016/10/01" in {
        val model = accountingPeriodModel.copy(startDate = minimumStartDate, endDate = minimumStartDate.plusMonths(1L))
        rightSide(model.validate) shouldBe model
      }

      "End date is in the future" in {

        val endDate = LocalDate.now().plusDays(1)
        val model = accountingPeriodModel.copy(
          startDate = LocalDate.now().minusMonths(12),
          endDate = endDate
        )
        rightSide(model.validate) shouldBe model
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

      "Start date is before minimum" in {
        val startDate = minimumStartDate.minusDays(1L)
        val model = accountingPeriodModel.copy(
          startDate = startDate,
          endDate = startDate.plusMonths(1L)
        )
        leftSideError(model.validate).errorMessage shouldBe StartDateCannotBeBeforeMinimum(startDate).errorMessage
      }
    }
  }
}

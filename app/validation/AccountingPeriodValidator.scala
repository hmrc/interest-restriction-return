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

import cats.data.{NonEmptyChain, Validated}
import models.Validation.ValidationResult
import models.{AccountingPeriodModel, Validation}

trait AccountingPeriodValidator {

  import cats.implicits._

  val accountingPeriodModel: AccountingPeriodModel

  private def validateStartDateCannotBeInFuture: ValidationResult[LocalDate] =
    if (accountingPeriodModel.startDate.isAfter(LocalDate.now())) {
      StartDateCannotBeInFuture.invalidNec
    } else {
      accountingPeriodModel.startDate.validNec
    }

  private def validateEndDateAfterStartDate: ValidationResult[LocalDate] =
    if (accountingPeriodModel.endDate.isBefore(accountingPeriodModel.startDate)) {
      EndDateAfterStartDate.invalidNec
    } else {
      accountingPeriodModel.endDate.validNec
    }

  private def validateAccountingPeriod18MonthsMax: ValidationResult[LocalDate] =
    if (accountingPeriodModel.endDate.isBefore(accountingPeriodModel.startDate.plusMonths(18))) {
      accountingPeriodModel.endDate.validNec
    } else {
      AccountingPeriod18MonthsMax.invalidNec
    }

  private def validateEndDateCannotBeInTheFuture: ValidationResult[LocalDate] =
    if (accountingPeriodModel.endDate.isBefore(LocalDate.now())) {
      accountingPeriodModel.endDate.validNec
    } else {
      EndDateCannotBeInTheFuture.invalidNec
    }

  def validate: ValidationResult[AccountingPeriodModel] =
    (validateStartDateCannotBeInFuture,
      validateEndDateAfterStartDate,
      validateAccountingPeriod18MonthsMax,
      validateEndDateCannotBeInTheFuture).mapN((_, _, _, _) => accountingPeriodModel)

}

case object StartDateCannotBeInFuture extends Validation {
  def errorMessages: String = "Start date cannot be in the future"
}

case object EndDateAfterStartDate extends Validation {
  def errorMessages: String = "End Date must be after start date"
}

case object AccountingPeriod18MonthsMax extends Validation {
  def errorMessages: String = "The end date must be less than 18 months after the start date"
}

case object EndDateCannotBeInTheFuture extends Validation {
  def errorMessages: String = "The end date must be in the past"
}







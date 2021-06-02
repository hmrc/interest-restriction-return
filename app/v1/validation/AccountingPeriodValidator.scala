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

import play.api.libs.json.{JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.{AccountingPeriodModel, Validation}

trait AccountingPeriodValidator extends BaseValidation {

  import cats.implicits._

  val accountingPeriodModel: AccountingPeriodModel

  val MINIMUM_START_DATE = LocalDate.parse("2016-10-01")
  val MINIMUM_END_DATE = LocalDate.parse("2017-04-01")

  private def validateStartDateCannotBeInFuture(implicit path: JsPath): ValidationResult[LocalDate] =
    if (accountingPeriodModel.startDate.isAfter(LocalDate.now())) {
      StartDateCannotBeInFuture(accountingPeriodModel.startDate).invalidNec
    } else {
      accountingPeriodModel.startDate.validNec
    }

  private def validateStartDateCannotBeBeforeMinimum(implicit path: JsPath): ValidationResult[LocalDate] =
    if (accountingPeriodModel.startDate.isBefore(MINIMUM_START_DATE)) {
      StartDateCannotBeBeforeMinimum(accountingPeriodModel.startDate).invalidNec
    } else {
      accountingPeriodModel.startDate.validNec
    }

  private def validateEndDateCannotBeBeforeMinimum(implicit path: JsPath): ValidationResult[LocalDate] =
    if (accountingPeriodModel.endDate.isBefore(MINIMUM_END_DATE)) {
      EndDateCannotBeBeforeMinimum(accountingPeriodModel.endDate).invalidNec
    } else {
      accountingPeriodModel.endDate.validNec
    }

  private def validateEndDateAfterStartDate(implicit path: JsPath): ValidationResult[LocalDate] =
    if (accountingPeriodModel.endDate.isAfter(accountingPeriodModel.startDate)) {
      accountingPeriodModel.endDate.validNec
    } else {
      EndDateAfterStartDate(accountingPeriodModel.endDate).invalidNec
    }

  private def validateAccountingPeriod18MonthsMax(implicit path: JsPath): ValidationResult[LocalDate] =
    if (accountingPeriodModel.endDate.isBefore(accountingPeriodModel.startDate.plusMonths(18))) {
      accountingPeriodModel.endDate.validNec
    } else {
      AccountingPeriod18MonthsMax(accountingPeriodModel.endDate).invalidNec
    }

  def validate(implicit path: JsPath): ValidationResult[AccountingPeriodModel] = {
    val startDateValidations = combineValidations(
      validateStartDateCannotBeInFuture,
      validateStartDateCannotBeBeforeMinimum
    )
    val endDateValidations = combineValidations(
      validateEndDateAfterStartDate,
      validateAccountingPeriod18MonthsMax,
      validateEndDateCannotBeBeforeMinimum
    )
    (startDateValidations, endDateValidations).mapN((_, _) => accountingPeriodModel)
  }
}

case class StartDateCannotBeInFuture(startDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val code = "START_DATE_CANNOT_BE_IN_FUTURE"
  val errorMessage: String = "Start date cannot be in the future"
  val path = topPath \ "startDate"
  val value = Some(Json.toJson(startDate))
}

case class StartDateCannotBeBeforeMinimum(startDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val code = "START_DATE_CANNOT_BE_BEFORE_MIN"
  val errorMessage: String = "Start date cannot be before 2016-10-01"
  val path = topPath \ "startDate"
  val value = Some(Json.toJson(startDate))
}

case class EndDateCannotBeBeforeMinimum(endDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val code = "END_DATE_CANNOT_BE_BEFORE_MIN"
  val errorMessage: String = "End date must be the same as or after 1 April 2017"
  val path = topPath \ "endDate"
  val value = Some(Json.toJson(endDate))
}

case class EndDateAfterStartDate(endDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val code = "END_DATE_BEFORE_START"
  val errorMessage: String = "End date of the group's period of account must be after start date of the group's period of account"
  val path = topPath \ "endDate"
  val value = Some(Json.toJson(endDate))
}

case class AccountingPeriod18MonthsMax(endDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val code = "END_DATE_18_MONTHS"
  val errorMessage: String = "The end date must be less than 18 months after the start date"
  val path = topPath \ "endDate"
  val value = Some(Json.toJson(endDate))
}

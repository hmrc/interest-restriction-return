/*
 * Copyright 2025 HM Revenue & Customs
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

import play.api.libs.json.{JsPath, JsValue, Json}
import v1.models.Validation.ValidationResult
import v1.models.{AccountingPeriodModel, Validation}

trait AccountingPeriodValidator extends BaseValidation {

  import cats.implicits.*

  val accountingPeriodModel: AccountingPeriodModel

  val MINIMUM_START_DATE: LocalDate = LocalDate.parse("2016-10-01")
  val MINIMUM_END_DATE: LocalDate   = LocalDate.parse("2017-04-01")

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

  private def validateAccountingPeriod18MonthsMax(implicit path: JsPath): ValidationResult[LocalDate] = {
    val validRangeFromCurrentDateInMonths = 18
    if (
      accountingPeriodModel.endDate.isBefore(
        accountingPeriodModel.startDate.plusMonths(validRangeFromCurrentDateInMonths)
      )
    ) {
      accountingPeriodModel.endDate.validNec
    } else {
      AccountingPeriod18MonthsMax(accountingPeriodModel.endDate).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[AccountingPeriodModel] = {
    val startDateValidations = combineValidations(
      validateStartDateCannotBeInFuture,
      validateStartDateCannotBeBeforeMinimum
    )
    val endDateValidations   = combineValidations(
      validateEndDateAfterStartDate,
      validateAccountingPeriod18MonthsMax,
      validateEndDateCannotBeBeforeMinimum
    )
    (startDateValidations, endDateValidations).mapN((_, _) => accountingPeriodModel)
  }
}

case class StartDateCannotBeInFuture(startDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val code: String           = "START_DATE_CANNOT_BE_IN_FUTURE"
  val errorMessage: String   = "Start date cannot be in the future"
  val path: JsPath           = topPath \ "startDate"
  val value: Option[JsValue] = Some(Json.toJson(startDate))
}

case class StartDateCannotBeBeforeMinimum(startDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val code: String           = "START_DATE_CANNOT_BE_BEFORE_MIN"
  val errorMessage: String   = "Start date must be on or after 2016-10-01"
  val path: JsPath           = topPath \ "startDate"
  val value: Option[JsValue] = Some(Json.toJson(startDate))
}

case class EndDateCannotBeBeforeMinimum(endDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val code: String           = "END_DATE_CANNOT_BE_BEFORE_MIN"
  val errorMessage: String   = "End date must be the same as or after 2017-04-01"
  val path: JsPath           = topPath \ "endDate"
  val value: Option[JsValue] = Some(Json.toJson(endDate))
}

case class EndDateAfterStartDate(endDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val code: String           = "END_DATE_BEFORE_START"
  val errorMessage: String   =
    "End date of the group's period of account must be after start date of the group's period of account"
  val path: JsPath           = topPath \ "endDate"
  val value: Option[JsValue] = Some(Json.toJson(endDate))
}

case class AccountingPeriod18MonthsMax(endDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val code: String           = "END_DATE_18_MONTHS"
  val errorMessage: String   = "Period of account cannot be longer than 18 months"
  val path: JsPath           = topPath \ "endDate"
  val value: Option[JsValue] = Some(Json.toJson(endDate))
}

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

import models.Validation.ValidationResult
import models.{AccountingPeriodModel, Validation}
import play.api.libs.json.{JsPath, Json}

trait AccountingPeriodValidator extends BaseValidation {

  import cats.implicits._

  val accountingPeriodModel: AccountingPeriodModel

  private def validateStartDateCannotBeInFuture(implicit path: JsPath): ValidationResult[LocalDate] =
    if (accountingPeriodModel.startDate.isAfter(LocalDate.now())) {
      StartDateCannotBeInFuture(accountingPeriodModel.startDate).invalidNec
    } else {
      accountingPeriodModel.startDate.validNec
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

  private def validateEndDateCannotBeInTheFuture(implicit path: JsPath): ValidationResult[LocalDate] =
    if (accountingPeriodModel.endDate.isBefore(LocalDate.now())) {
      accountingPeriodModel.endDate.validNec
    } else {
      EndDateCannotBeInTheFuture(accountingPeriodModel.endDate).invalidNec
    }

  def validate(implicit path: JsPath): ValidationResult[AccountingPeriodModel] =
    (validateStartDateCannotBeInFuture,
      combineValidationsForField(validateEndDateAfterStartDate,
      validateAccountingPeriod18MonthsMax,
      validateEndDateCannotBeInTheFuture)).mapN((_, _) => accountingPeriodModel)

}

case class StartDateCannotBeInFuture(startDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "Start date cannot be in the future"
  val path = topPath \ "startDate"
  val value = Json.toJson(startDate)
}

case class EndDateAfterStartDate(endDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "End Date must be after start date"
  val path = topPath \ "endDate"
  val value = Json.toJson(endDate)
}

case class AccountingPeriod18MonthsMax(endDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "The end date must be less than 18 months after the start date"
  val path = topPath \ "endDate"
  val value = Json.toJson(endDate)
}

case class EndDateCannotBeInTheFuture(endDate: LocalDate)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "The end date must be in the past"
  val path = topPath \ "endDate"
  val value = Json.toJson(endDate)
}







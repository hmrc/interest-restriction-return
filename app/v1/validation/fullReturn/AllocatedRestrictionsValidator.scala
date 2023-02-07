/*
 * Copyright 2023 HM Revenue & Customs
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

package v1.validation.fullReturn

import play.api.libs.json.{JsPath, JsValue, Json}
import v1.models.{AccountingPeriodModel, Validation}
import v1.models.Validation.ValidationResult
import v1.models.fullReturn.AllocatedRestrictionsModel
import v1.validation.BaseValidation
import java.time.LocalDate

trait AllocatedRestrictionsValidator extends BaseValidation {

  import cats.implicits._

  val allocatedRestrictionsModel: AllocatedRestrictionsModel

  val MINIMUM_DATE: LocalDate = LocalDate.parse("1900-01-01")
  val MAXIMUM_DATE: LocalDate = LocalDate.parse("2099-12-31")
  val aYearInMonths           = 12

  def validate(groupAccountingPeriod: AccountingPeriodModel)(implicit path: JsPath): ValidationResult[_] =
    (
      combineValidations(
        apRestrictionValidator(
          Some(allocatedRestrictionsModel.ap1EndDate),
          Some(allocatedRestrictionsModel.disallowanceAp1),
          1,
          "ap1EndDate"
        ),
        apRestrictionValidator(
          allocatedRestrictionsModel.ap2EndDate,
          allocatedRestrictionsModel.disallowanceAp2,
          2,
          "ap2EndDate"
        ),
        apRestrictionValidator(
          allocatedRestrictionsModel.ap3EndDate,
          allocatedRestrictionsModel.disallowanceAp3,
          3,
          "ap3EndDate"
        ),
        validateAp1(groupAccountingPeriod.startDate),
        validateAp2(groupAccountingPeriod.endDate),
        validateAp3(groupAccountingPeriod.endDate)
      )
    ).map(_ => allocatedRestrictionsModel)

  private def apRestrictionValidator(
    endDate: Option[LocalDate],
    amount: Option[BigDecimal],
    i: Int,
    endDateJsonAttribute: String
  )(implicit path: JsPath): ValidationResult[_] = {
    val period = (endDate, amount)
    period match {
      case (Some(_), None)                 => AllocatedRestrictionNotSupplied(i).invalidNec
      case (None, Some(_))                 => AllocatedRestrictionSupplied(i).invalidNec
      case (Some(_), Some(amt)) if amt < 0 => AllocatedRestrictionNegative(i, amt).invalidNec
      case (Some(_), Some(amt)) if amt % 0.01 != 0 => AllocatedRestrictionDecimalError(i, amt).invalidNec
      case (Some(date), _) => dateInRange(date, endDateJsonAttribute)
      case _               => period.validNec
    }
  }

  private def dateInRange(date: LocalDate, jsonAttribute: String)(implicit path: JsPath): ValidationResult[LocalDate] =
    if (date.isAfter(MAXIMUM_DATE) || date.isBefore(MINIMUM_DATE)) {
      DateRangeError(date, jsonAttribute).invalidNec
    } else {
      date.validNec
    }

  def validateAp1(groupStartDate: LocalDate)(implicit topPath: JsPath): ValidationResult[LocalDate] =
    if (allocatedRestrictionsModel.ap1EndDate.isAfter(groupStartDate)) {
      allocatedRestrictionsModel.ap1EndDate.validNec
    } else {
      Ap1NotAfterGroupStartDate(allocatedRestrictionsModel.ap1EndDate, groupStartDate).invalidNec
    }

  def validateAp2(groupEndDate: LocalDate)(implicit topPath: JsPath): ValidationResult[Option[LocalDate]] =
    (allocatedRestrictionsModel.ap1EndDate, allocatedRestrictionsModel.ap2EndDate) match {
      case (ap1, Some(ap2)) =>
        combineValidations(
          if (!ap2.isAfter(ap1)) {
            AllocatedRestrictionDateBeforePrevious(2).invalidNec
          } else { Some(ap2).validNec },
          if (ap2.minusMonths(aYearInMonths).isAfter(ap1)) {
            AllocatedRestrictionDateGreaterThan12MonthsAfterPrevious(2).invalidNec
          } else { Some(ap2).validNec },
          if (ap1.isAfter(groupEndDate)) { DateAfterGPOA(2).invalidNec }
          else { Some(ap2).validNec }
        )
      case _                =>
        None.validNec
    }

  def validateAp3(groupEndDate: LocalDate)(implicit topPath: JsPath): ValidationResult[Option[LocalDate]] =
    (
      allocatedRestrictionsModel.ap1EndDate,
      allocatedRestrictionsModel.ap2EndDate,
      allocatedRestrictionsModel.ap3EndDate
    ) match {
      case (_, None, Some(_))        =>
        AllocatedRestrictionLaterPeriodSupplied(3).invalidNec
      case (_, Some(ap2), Some(ap3)) =>
        combineValidations(
          if (!ap3.isAfter(ap2)) AllocatedRestrictionDateBeforePrevious(3).invalidNec else Some(ap3).validNec,
          if (ap3.minusMonths(aYearInMonths).isAfter(ap2)) {
            AllocatedRestrictionDateGreaterThan12MonthsAfterPrevious(3).invalidNec
          } else { Some(ap3).validNec },
          if (ap2.isAfter(groupEndDate)) DateAfterGPOA(3).invalidNec else Some(ap3).validNec
        )
      case _                         =>
        None.validNec
    }

}

case class AllocatedRestrictionNotSupplied(i: Int)(implicit topPath: JsPath) extends Validation {
  val code: String           = "RESTRICTION_NOT_SUPPLIED"
  val path: JsPath           = topPath \ s"disallowanceAp$i"
  val errorMessage: String   =
    s"There is an accounting period $i end date so there must be a disallowance for that period. The disallowance can also be zero"
  val value: Option[JsValue] = None
}

case class AllocatedRestrictionSupplied(i: Int)(implicit topPath: JsPath) extends Validation {
  val code: String           = "RESTRICTION_SUPPLIED"
  val path: JsPath           = topPath \ s"disallowanceAp$i"
  val errorMessage: String   = s"No accounting period $i end date so no disallowance needed for that period"
  val value: Option[JsValue] = None
}

case class AllocatedRestrictionLaterPeriodSupplied(i: Int)(implicit topPath: JsPath) extends Validation {
  val code: String           = "RESTRICTION_MISSING_PREVIOUS"
  val path: JsPath           = topPath \ s"ap${i}End"
  val errorMessage: String   = s"Accounting period $i end date cannot be given as previous accounting period is missing"
  val value: Option[JsValue] = None
}

case class AllocatedRestrictionNegative(i: Int, amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code: String           = "RESTRICTION_NEGATIVE"
  val path: JsPath           = topPath \ s"disallowanceAp$i"
  val errorMessage: String   = s"Disallowance for the accounting period $i must be a positive number"
  val value: Option[JsValue] = Some(Json.toJson(amt))
}

case class AllocatedRestrictionDecimalError(i: Int, amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code: String           = "RESTRICTION_DECIMAL"
  val path: JsPath           = topPath \ s"disallowanceAp$i"
  val errorMessage: String   = "Amount of disallowance for this accounting period must be to 2 decimal places or less"
  val value: Option[JsValue] = Some(Json.toJson(amt))
}

case class AllocatedRestrictionDateBeforePrevious(i: Int)(implicit topPath: JsPath) extends Validation {
  val code: String           = "END_DATE_BEFORE_PREVIOUS"
  val path: JsPath           = topPath \ s"ap${i}EndDate"
  val errorMessage: String   =
    "End date of this accounting period cannot be equal to or before the end date of the preceding accounting period"
  val value: Option[JsValue] = None
}

case class AllocatedRestrictionDateGreaterThan12MonthsAfterPrevious(i: Int)(implicit topPath: JsPath)
    extends Validation {
  val code: String           = "END_DATE_GREATER_THAN_12_MONTHS"
  val path: JsPath           = topPath \ s"ap${i}EndDate"
  val errorMessage: String   =
    "End date entered can't be more than 12 months after the end date for the previous accounting period"
  val value: Option[JsValue] = None
}

case class Ap1NotAfterGroupStartDate(ap1Date: LocalDate, groupStartDate: LocalDate)(implicit topPath: JsPath)
    extends Validation {
  val code: String           = "END_DATE_BEFORE_GROUP_START_DATE"
  val path: JsPath           = topPath \ "ap1EndDate"
  val errorMessage: String   =
    "The end date of the first accounting period must be on or after the start date of the group's period of account"
  val value: Option[JsValue] = Some(Json.obj("ap1EndDate" -> ap1Date, "groupStartDate" -> groupStartDate))
}

case class DateRangeError(date: LocalDate, jsonAttribute: String)(implicit topPath: JsPath) extends Validation {
  val code: String           = "DATE_INVALID"
  val path: JsPath           = topPath \ jsonAttribute
  val errorMessage: String   = "Dates must be in the range 1900-01-01 to 2099-12-31"
  val value: Option[JsValue] = Some(Json.toJson(date))
}

case class DateAfterGPOA(i: Int)(implicit topPath: JsPath) extends Validation {
  val code: String           = "ACCOUNTING_PERIOD_OUTSIDE_POA"
  val path: JsPath           = topPath \ s"ap${i}EndDate"
  val errorMessage: String   =
    s"AP${i - 1} end date is after the group's period of account so AP$i end date is not allowed"
  val value: Option[JsValue] = None
}

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

package v1.validation.fullReturn

import play.api.libs.json.{JsPath, Json}
import v1.models.Validation
import v1.models.Validation.ValidationResult
import v1.models.fullReturn.GroupLevelAmountModel
import v1.validation.BaseValidation

trait GroupLevelAmountValidator extends BaseValidation {

  import cats.implicits._

  val groupLevelAmount: GroupLevelAmountModel

  private def validatePositive(amt: BigDecimal, validation: Validation)(implicit topPath: JsPath): ValidationResult[BigDecimal] =
    if(amt < 0) validation.invalidNec else amt.validNec

  private def validateDecimalPlaces(amt: BigDecimal, validation: Validation)(implicit topPath: JsPath): ValidationResult[BigDecimal] =
    if(amt % 0.01 != 0) validation.invalidNec else amt.validNec

  def validate(implicit path: JsPath): ValidationResult[GroupLevelAmountModel] =
    (
      validatePositive("interestReactivationCap", groupLevelAmount.interestReactivationCap),
      validateDecimalPlaces("interestReactivationCap", groupLevelAmount.interestReactivationCap),
      validatePositive("interestAllowanceForPeriod", groupLevelAmount.interestAllowanceForPeriod),
      validatePositive("interestAllowanceBroughtForward", groupLevelAmount.interestAllowanceBroughtForward),
      validatePositive("interestCapacityForPeriod", groupLevelAmount.interestCapacityForPeriod),
      validateDecimalPlaces("interestAllowanceForPeriod", groupLevelAmount.interestAllowanceForPeriod),
      validateDecimalPlaces("interestAllowanceBroughtForward", groupLevelAmount.interestAllowanceBroughtForward),
      validateDecimalPlaces("interestCapacityForPeriod", groupLevelAmount.interestCapacityForPeriod)
    ).mapN((_,_,_,_,_,_,_,_) => groupLevelAmount)
}

case class InterestAllowanceForPeriodCannotBeNegative(amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code = "INTEREST_ALLOWANCE_PERIOD_NEGATIVE"
  val path = topPath \ s"interestAllowanceForPeriod"
  val errorMessage: String = s"Interest allowance for the period must be a positive number"
  val value = Some(Json.toJson(amt))
}

case class InterestAllowanceForPeriodDecimalError(amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code = "INTEREST_ALLOWANCE_PERIOD_DECIMAL"
  val path = topPath \ s"interestAllowanceForPeriod"
  val errorMessage: String = s"Interest allowance brought forward must be to 2 decimal places or less"
  val value = Some(Json.toJson(amt))
}

case class InterestAllowanceBroughtForwardCannotBeNegative(amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code = "INTEREST_ALLOWANCE_FORWARD_NEGATIVE"
  val path = topPath \ s"interestAllowanceBroughtForward"
  val errorMessage: String = s"Interest allowance brought forward must be a positive number"
  val value = Some(Json.toJson(amt))
}

case class InterestAllowanceBroughtForwardDecimalError(amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code = "INTEREST_ALLOWANCE_FORWARD_DECIMAL"
  val path = topPath \ s"interestAllowanceBroughtForward"
  val errorMessage: String = s"Interest allowance brought forward must be to 2 decimal places or less"
  val value = Some(Json.toJson(amt))
}

case class InterestCapacityForPeriodCannotBeNegative(amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code = "INTEREST_CAP_NEGATIVE"
  val path = topPath \ s"interestCapacityForPeriod"
  val errorMessage: String = s"Interest capacity for the period must be a positive number"
  val value = Some(Json.toJson(amt))
}

case class InterestCapacityForPeriodDecimalError(amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code = "INTEREST_CAP_DECIMAL"
  val path = topPath \ s"interestCapacityForPeriod"
  val errorMessage: String = s"Interest capacity for the period must be to 2 decimal places or less"
  val value = Some(Json.toJson(amt))
}
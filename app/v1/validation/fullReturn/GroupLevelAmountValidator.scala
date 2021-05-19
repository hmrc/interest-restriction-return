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

  private def validatePositive(field: String, amt: BigDecimal)(implicit topPath: JsPath): ValidationResult[BigDecimal] =
    if(amt < 0) GroupLevelAmountCannotBeNegative(field, amt).invalidNec else amt.validNec

  private def validateDecimalPlaces(field: String, amt: BigDecimal)(implicit topPath: JsPath): ValidationResult[BigDecimal] =
    if(amt % 0.01 != 0) GroupLevelAmountDecimalError(field, amt).invalidNec else amt.validNec

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

case class GroupLevelAmountCannotBeNegative(field: String, amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code = "GROUP_LEVEL_AMOUNT_NEGATIVE"
  val path = topPath \ s"$field"
  val errorMessage: String = s"$field cannot be negative"
  val value = Some(Json.toJson(amt))
}

case class GroupLevelAmountDecimalError(field: String, amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code = "GROUP_LEVEL_AMOUNT_DECIMAL"
  val path = topPath \ s"$field"
  val errorMessage: String = s"$field has greater than the allowed 2 decimal places."
  val value = Some(Json.toJson(amt))
}

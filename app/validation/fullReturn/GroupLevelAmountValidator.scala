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

package validation.fullReturn

import models.Validation
import models.Validation.ValidationResult
import models.fullReturn.GroupLevelAmountModel
import play.api.libs.json.{JsPath, Json}
import validation.BaseValidation

trait GroupLevelAmountValidator extends BaseValidation {

  import cats.implicits._

  val groupLevelAmount: GroupLevelAmountModel

  private def validatePositive(field: String, amt: BigDecimal)(implicit topPath: JsPath): ValidationResult[BigDecimal] =
    if(amt < 0) GroupLevelAmountCannotBeNegative(field, amt).invalidNec else amt.validNec

  def validate(implicit path: JsPath): ValidationResult[GroupLevelAmountModel] =
    (
      validatePositive("interestReactivationCap", groupLevelAmount.interestReactivationCap),
      validatePositive("interestAllowanceForPeriod", groupLevelAmount.interestAllowanceForPeriod),
      validatePositive("interestAllowanceForward", groupLevelAmount.interestAllowanceForward),
      validatePositive("interestCapacityForPeriod", groupLevelAmount.interestCapacityForPeriod)
    ).mapN((_,_,_,_) => groupLevelAmount)
}

case class GroupLevelAmountCannotBeNegative(field: String, amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val path = topPath \ "field"
  val errorMessage: String = s"$field cannot be negative"
  val value = Json.toJson(amt)
}
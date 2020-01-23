/*
 * Copyright 2020 HM Revenue & Customs
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
import v1.models.fullReturn.AllocatedReactivationsModel
import v1.validation.BaseValidation

trait AllocatedReactivationsValidator extends BaseValidation {

  import cats.implicits._

  val allocatedReactivationsModel: AllocatedReactivationsModel

  def validate(implicit path: JsPath): ValidationResult[AllocatedReactivationsModel] = {
    val reactivations = allocatedReactivationsModel.currentPeriodReactivation
    if(reactivations < 0) {
      AllocatedReactivationsCannotBeNegative(reactivations).invalidNec
    } else {
      reactivations.validNec
    }
  }.map(_ => allocatedReactivationsModel)
}

case class AllocatedReactivationsCannotBeNegative(currentPeriodReactivation: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val path = topPath \ "currentPeriodReactivation"
  val errorMessage: String = s"currentPeriodReactivation cannot be negative"
  val value = Json.toJson(currentPeriodReactivation)
}

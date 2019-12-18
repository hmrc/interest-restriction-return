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

import models.Validation.ValidationResult
import models.{DeemedParentModel, Validation}
import play.api.libs.json.{JsPath, Json}

trait DeemedParentValidator extends BaseValidation {

  import cats.implicits._

  val deemedParentModel: DeemedParentModel

  private def validateDeemedParentCanNotBeUkAndNonUk(implicit path: JsPath): ValidationResult[DeemedParentModel] = {
    val isUk = deemedParentModel.ctutr.isDefined
    val isNonUk = deemedParentModel.countryOfIncorporation.isDefined || deemedParentModel.nonUkCrn.isDefined

    if(isUk && isNonUk) {
      DeemedParentCannotBeUkAndNonUk(deemedParentModel).invalidNec
    } else {
      deemedParentModel.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[DeemedParentModel] =
    (validateDeemedParentCanNotBeUkAndNonUk,
      optionValidations(deemedParentModel.ctutr.map(_.validate(path \ "ctutr"))),
      optionValidations(deemedParentModel.countryOfIncorporation.map(_.validate(path \ "countryOfIncorporation")))
      ).mapN((_,_,_) => deemedParentModel)
}

case class DeemedParentCannotBeUkAndNonUk(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "Deemed Parent Company Model cannot contain data for UK and NonUK fields"
  val value = Json.toJson(model)
}








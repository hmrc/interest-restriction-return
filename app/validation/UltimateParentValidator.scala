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

package validation

import models.Validation.ValidationResult
import models.{UltimateParentModel, Validation}
import play.api.libs.json.{JsPath, Json}

trait UltimateParentValidator extends BaseValidation {

  import cats.implicits._

  val ultimateParentModel: UltimateParentModel

  private def validateParentCanNotBeUkAndNonUk(implicit path: JsPath): ValidationResult[UltimateParentModel] = {
    val isUk = ultimateParentModel.ctutr.isDefined || ultimateParentModel.crn.isDefined
    val isNonUk = ultimateParentModel.countryOfIncorporation.isDefined || ultimateParentModel.nonUkCrn.isDefined

    if(isUk && isNonUk) {
      UltimateParentCannotBeUkAndNonUk(ultimateParentModel).invalidNec
    } else {
      ultimateParentModel.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[UltimateParentModel] =
    (validateParentCanNotBeUkAndNonUk,
      ultimateParentModel.companyName.validate(path \ "companyName"),
      optionValidations(ultimateParentModel.ctutr.map(_.validate(path \ "ctutr"))),
      optionValidations(ultimateParentModel.crn.map(_.validate(path \ "crn"))),
      optionValidations(ultimateParentModel.countryOfIncorporation.map(_.validate(path \ "countryOfIncorporation")))
      ).mapN((_,_,_,_,_) => ultimateParentModel)
}

case class UltimateParentCannotBeUkAndNonUk(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "Ultimate Parent Company Model cannot contain data for UK and NonUK fields"
  val value = Json.toJson(model)
}








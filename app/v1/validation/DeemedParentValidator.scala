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

package v1.validation

import play.api.libs.json.{JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.{DeemedParentModel, Validation}

trait DeemedParentValidator extends BaseValidation {

  import cats.implicits._

  val deemedParentModel: DeemedParentModel

  private def validateDeemedParentCanNotBeUkAndNonUk(implicit path: JsPath): ValidationResult[DeemedParentModel] = {
    val isUk = deemedParentModel.ctutr.isDefined || deemedParentModel.sautr.isDefined
    val isNonUk = deemedParentModel.countryOfIncorporation.isDefined

    (isUk, isNonUk) match {
      case (false, false) => DeemedParentWrongDetailsError(deemedParentModel).invalidNec
      case (true, true) => DeemedParentCannotBeUkAndNonUk(deemedParentModel).invalidNec
      case _ => deemedParentModel.validNec
    }
  }

  private def validateCorrectUTRSupplied(implicit path: JsPath): ValidationResult[DeemedParentModel] = {
    val ctutr = deemedParentModel.ctutr.isDefined
    val sautr = deemedParentModel.sautr.isDefined

    (ctutr, sautr) match {
      case (true, true) => DeemedParentUTRSuppliedError(deemedParentModel).invalidNec
      case _ => deemedParentModel.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[DeemedParentModel] =
    (validateDeemedParentCanNotBeUkAndNonUk,
      validateCorrectUTRSupplied,
      deemedParentModel.companyName.validate(path \ "companyName"),
      optionValidations(deemedParentModel.ctutr.map(_.validate(path \ "ctutr"))),
      optionValidations(deemedParentModel.countryOfIncorporation.map(_.validate(path \ "countryOfIncorporation")))
      ).mapN((_,_,_,_,_) => deemedParentModel)
}

case class DeemedParentCannotBeUkAndNonUk(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "Deemed Parent Company Model cannot contain data for UK and NonUK fields"
  val value = Json.toJson(model)
}

case class DeemedParentUTRSuppliedError(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "both ctutr and sautr cannot be supplied simultaneously for Uk Deemed Parent"
  val value = Json.toJson(model)
}

case class DeemedParentWrongDetailsError(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given the wrong details for the type of deemed parent you have tried to supply"
  val value = Json.toJson(model)
}








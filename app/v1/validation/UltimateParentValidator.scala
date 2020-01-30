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
import v1.models.{UltimateParentModel, Validation}

trait UltimateParentValidator extends BaseValidation {

  import cats.implicits._

  val ultimateParentModel: UltimateParentModel

  private def validateUltimateParentCanNotBeUkAndNonUk(implicit path: JsPath): ValidationResult[UltimateParentModel] = {
    val isUk = ultimateParentModel.ctutr.isDefined || ultimateParentModel.crn.isDefined || ultimateParentModel.sautr.isDefined
    val isNonUk = ultimateParentModel.countryOfIncorporation.isDefined || ultimateParentModel.nonUkCrn.isDefined

    (isUk, isNonUk) match {
      case (false, false) => UltimateParentWrongDetailsError(ultimateParentModel).invalidNec
      case (true, true) => UltimateParentCannotBeUkAndNonUk(ultimateParentModel).invalidNec
      case _ => ultimateParentModel.validNec
    }
  }

  private def validateCorrectUTRSupplied(implicit path: JsPath): ValidationResult[UltimateParentModel] = {
    val ctutr = ultimateParentModel.ctutr.isDefined
    val sautr = ultimateParentModel.sautr.isDefined
    (ctutr, sautr) match {
      case (true, true) => UltimateParentUTRSuppliedError(ultimateParentModel).invalidNec
      case _ => ultimateParentModel.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[UltimateParentModel] =
    (validateUltimateParentCanNotBeUkAndNonUk,
      validateCorrectUTRSupplied,
      ultimateParentModel.companyName.validate(path \ "companyName"),
      optionValidations(ultimateParentModel.ctutr.map(_.validate(path \ "ctutr"))),
      optionValidations(ultimateParentModel.crn.map(_.validate(path \ "crn"))),
      optionValidations(ultimateParentModel.countryOfIncorporation.map(_.validate(path \ "countryOfIncorporation")))
    ).mapN((_, _, _, _, _, _) => ultimateParentModel)
}

case class UltimateParentCannotBeUkAndNonUk(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "Ultimate Parent Company Model cannot contain data for UK and NonUK fields"
  val value = Json.toJson(model)
}

case class UltimateParentUTRSuppliedError(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "both ctutr and sautr cannot be supplied simultaneously for Uk Ultimate Parent"
  val value = Json.toJson(model)
}

case class UltimateParentWrongDetailsError(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given the wrong details for the type of ultimate parent you have tried to supply"
  val value = Json.toJson(model)
}







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

package v1.validation

import play.api.libs.json.{JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.{UltimateParentModel, Validation}

trait UltimateParentValidator extends BaseValidation {

  import cats.implicits._

  val ultimateParentModel: UltimateParentModel

  private def validateCorrectCompanyDetailsSupplied(implicit path: JsPath): ValidationResult[UltimateParentModel] = {
    val ctutr = ultimateParentModel.ctutr.isDefined
    val sautr = ultimateParentModel.sautr.isDefined
    val countryCode = ultimateParentModel.countryOfIncorporation.isDefined

    (ctutr, sautr, countryCode) match {
      case (true, true, true) => UltimateParentWrongDetailsSuppliedError(ultimateParentModel).invalidNec
      case (true, true, false) => WrongUltimateParentIsUkCompanyAndPartnership(ultimateParentModel).invalidNec
      case (true, false, true) => WrongUltimateParentIsUKCompanyAndNonUK(ultimateParentModel).invalidNec
      case (false, true, true) => WrongUltimateParentIsUkPartnershipAndNonUKCompany(ultimateParentModel).invalidNec
      case _ => ultimateParentModel.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[UltimateParentModel] =
    (validateCorrectCompanyDetailsSupplied,
      ultimateParentModel.companyName.validate(path \ "companyName"),
      optionValidations(ultimateParentModel.ctutr.map(_.validate(path \ "ctutr"))),
      optionValidations(ultimateParentModel.countryOfIncorporation.map(_.validate(path \ "countryOfIncorporation")))
    ).mapN((_, _, _, _) => ultimateParentModel)
}

case class UltimateParentWrongDetailsSuppliedError(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given the details for all three ultimate parents please give correct details"
  val value = Json.toJson(model)
}

case class WrongUltimateParentIsUkCompanyAndPartnership(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given details for a UK Company and Partnership"
  val value = Json.toJson(model)
}

case class WrongUltimateParentIsUkPartnershipAndNonUKCompany(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given details for a UK Partnership and NonUK Company"
  val value = Json.toJson(model)
}

case class WrongUltimateParentIsUKCompanyAndNonUK(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given details for a UK and Non UK Company"
  val value = Json.toJson(model)
}





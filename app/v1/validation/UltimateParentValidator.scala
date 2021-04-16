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

import play.api.libs.json.{Json, JsPath, JsValue}
import v1.models.Validation.ValidationResult
import v1.models.{UltimateParentModel, Validation}

trait UltimateParentValidator extends BaseValidation {

  import cats.implicits._

  val ultimateParentModel: UltimateParentModel

  private def validateCorrectCompanyDetailsSupplied(implicit path: JsPath): ValidationResult[UltimateParentModel] = {
    val ctutr = ultimateParentModel.ctutr.isDefined
    val sautr = ultimateParentModel.sautr.isDefined
    val countryCode = ultimateParentModel.countryOfIncorporation.isDefined
    val isUk = ultimateParentModel.isUk

    (ctutr, sautr, countryCode, isUk) match {
      case (true, true, true, _) => UltimateParentWrongDetailsSuppliedError(ultimateParentModel).invalidNec
      case (true, true, false, _) => WrongUltimateParentIsUkCompanyAndPartnership(ultimateParentModel).invalidNec
      case (true, false, true, _) => WrongUltimateParentIsUKCompanyAndNonUK(ultimateParentModel).invalidNec
      case (false, true, true, _) => WrongUltimateParentIsUkPartnershipAndNonUKCompany(ultimateParentModel).invalidNec
      case (_, _, true, true) => WrongUltimateParentIsUkPartnershipAndNonUKCompany(ultimateParentModel).invalidNec
      case (true, _, _, false) | (_, true, _, false) => WrongUltimateParentIsUkPartnershipAndNonUKCompany(ultimateParentModel).invalidNec
      case (_, _, false, false) => NonUKUltimateParentMissingCountryOfIncorporation(ultimateParentModel).invalidNec
      case (false, false, false, _) => NoUTROrCountryCode(ultimateParentModel).invalidNec
      case _ => ultimateParentModel.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[UltimateParentModel] =
    (validateCorrectCompanyDetailsSupplied,
      ultimateParentModel.companyName.validate(path \ "companyName"),
      optionValidations(ultimateParentModel.ctutr.map(_.validate(path \ "ctutr"))),
      optionValidations(ultimateParentModel.sautr.map(_.validate(path \ "sautr"))),
      optionValidations(ultimateParentModel.countryOfIncorporation.map(_.validate(path \ "countryOfIncorporation")))
      optionValidations(ultimateParentModel.legalEntityIdentifier.map(_.validate(path \ "legalEntityIdentifier")))
    ).mapN((_, _, _, _, _, _) => ultimateParentModel)
}

case class UltimateParentWrongDetailsSuppliedError(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given the details for all three ultimate parents please give correct details"
  val value: JsValue = Json.toJson(model)
}

case class WrongUltimateParentIsUkCompanyAndPartnership(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given details for a UK Company and Partnership"
  val value: JsValue = Json.toJson(model)
}

case class WrongUltimateParentIsUkPartnershipAndNonUKCompany(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given details for a UK Partnership and NonUK Company"
  val value: JsValue = Json.toJson(model)
}

case class WrongUltimateParentIsUKCompanyAndNonUK(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given details for a UK and Non UK Company"
  val value: JsValue = Json.toJson(model)
}

case class NoUTROrCountryCode(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "You need to enter a CTUTR, an SAUTR, or a Country Of Incorporation"
  val value: JsValue = Json.toJson(model)
}

case class NonUKUltimateParentMissingCountryOfIncorporation(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "You need to enter a Country Of Incorporation for a NonUK Company"
  val value: JsValue = Json.toJson(model)
}





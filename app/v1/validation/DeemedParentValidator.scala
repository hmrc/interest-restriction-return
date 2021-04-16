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
import v1.models.{DeemedParentModel, Validation}

trait DeemedParentValidator extends BaseValidation {

  import cats.implicits._

  val deemedParentModel: DeemedParentModel

  private def validateCorrectCompanyDetailsSupplied(implicit path: JsPath): ValidationResult[DeemedParentModel] = {
    val ctutr = deemedParentModel.ctutr.isDefined
    val sautr = deemedParentModel.sautr.isDefined
    val countryCode = deemedParentModel.countryOfIncorporation.isDefined
    val isUk = deemedParentModel.isUk
    (ctutr, sautr, countryCode, isUk) match {
      case (true, true, true, _) => DeemedParentWrongDetailsSuppliedError(deemedParentModel).invalidNec
      case (true, true, false, _) => WrongDeemedParentIsUkCompanyAndPartnership(deemedParentModel).invalidNec
      case (true, false, true, _) => WrongDeemedParentIsUKCompanyAndNonUK(deemedParentModel).invalidNec
      case (false, true, true, _) => WrongDeemedParentIsUkPartnershipAndNonUKCompany(deemedParentModel).invalidNec
      case (_, _, true, true) => WrongDeemedParentIsUkPartnershipAndNonUKCompany(deemedParentModel).invalidNec
      case (true, _, _, false) | (_, true, _, false) => WrongDeemedParentIsUkPartnershipAndNonUKCompany(deemedParentModel).invalidNec
      case (_, _, false, false) => NonUKDeemedParentMissingCountryOfIncorporation(deemedParentModel).invalidNec
      case (false, false, false, _) => NoUTROrCountryCodeOnDeemedParent(deemedParentModel).invalidNec
      case _ => deemedParentModel.validNec
    }
  }


  def validate(implicit path: JsPath): ValidationResult[DeemedParentModel] =
    (validateCorrectCompanyDetailsSupplied,
      deemedParentModel.companyName.validate(path \ "companyName"),
      optionValidations(deemedParentModel.ctutr.map(_.validate(path \ "ctutr"))),
      optionValidations(deemedParentModel.sautr.map(_.validate(path \ "sautr"))),
      optionValidations(deemedParentModel.countryOfIncorporation.map(_.validate(path \ "countryOfIncorporation"))),
      optionValidations(deemedParentModel.legalEntityIdentifier.map(_.validate(path \ "legalEntityIdentifier")))
      ).mapN((_, _, _, _, _, _) => deemedParentModel)
}


case class DeemedParentWrongDetailsSuppliedError(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given the details for all three ultimate parents please give correct details"
  val value: JsValue = Json.toJson(model)
}

case class WrongDeemedParentIsUkCompanyAndPartnership(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given details for a UK Company and Partnership"
  val value: JsValue = Json.toJson(model)
}

case class WrongDeemedParentIsUkPartnershipAndNonUKCompany(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given details for a UK Partnership and NonUK Company"
  val value: JsValue = Json.toJson(model)
}

case class WrongDeemedParentIsUKCompanyAndNonUK(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given details for a UK and Non UK Company"
  val value: JsValue = Json.toJson(model)
}

case class NoUTROrCountryCodeOnDeemedParent(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "You need to enter a CTUTR, an SAUTR, or a Country Of Incorporation"
  val value: JsValue = Json.toJson(model)
}

case class NonUKDeemedParentMissingCountryOfIncorporation(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "You need to enter a Country Of Incorporation for a NonUK Company"
  val value: JsValue = Json.toJson(model)
}








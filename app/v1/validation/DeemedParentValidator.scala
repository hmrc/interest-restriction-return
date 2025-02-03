/*
 * Copyright 2024 HM Revenue & Customs
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

import play.api.libs.json.{JsPath, JsValue, Json}
import v1.models.Validation.ValidationResult
import v1.models.{DeemedParentModel, Validation}

trait DeemedParentValidator extends BaseValidation {

  import cats.implicits.*

  val deemedParentModel: DeemedParentModel

  def validateSingleCompanyDetailSupplied(implicit path: JsPath): ValidationResult[DeemedParentModel] = {
    val numberOfDetailsSupplied =
      Seq(deemedParentModel.ctutr, deemedParentModel.sautr, deemedParentModel.countryOfIncorporation).flatten.length
    if (numberOfDetailsSupplied > 1) {
      DeemedParentWrongDetailsSuppliedError(deemedParentModel).invalidNec
    } else {
      deemedParentModel.validNec
    }
  }

  def validateUkCompanyDetails(implicit path: JsPath): ValidationResult[DeemedParentModel] = {
    val utrSupplied = deemedParentModel.ctutr.isDefined || deemedParentModel.sautr.isDefined

    if (deemedParentModel.isUk && !utrSupplied) {
      UKDeemedMissingUTR(deemedParentModel).invalidNec
    } else {
      deemedParentModel.validNec
    }
  }

  def validateNonUkCompanyDetails(implicit path: JsPath): ValidationResult[DeemedParentModel] = {
    val countrySupplied = deemedParentModel.countryOfIncorporation.isDefined

    if (!deemedParentModel.isUk && !countrySupplied) {
      NonUKDeemedParentMissingCountryOfIncorporation(deemedParentModel).invalidNec
    } else {
      deemedParentModel.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[DeemedParentModel] =
    combineValidations(
      validateSingleCompanyDetailSupplied,
      validateUkCompanyDetails,
      validateNonUkCompanyDetails,
      deemedParentModel.companyName.validate(path \ "companyName"),
      optionValidations(deemedParentModel.ctutr.map(_.validate(path \ "ctutr"))),
      optionValidations(deemedParentModel.sautr.map(_.validate(path \ "sautr"))),
      optionValidations(deemedParentModel.countryOfIncorporation.map(_.validate(path \ "countryOfIncorporation"))),
      optionValidations(deemedParentModel.legalEntityIdentifier.map(_.validate(path \ "legalEntityIdentifier")))
    ).map(_ => deemedParentModel)
}

case class DeemedParentWrongDetailsSuppliedError(model: DeemedParentModel)(implicit val path: JsPath)
    extends Validation {
  val code: String           = "DEEMED_PARENT_DETAILS"
  val errorMessage: String   = "Deemed parent must have either a CTUTR, a SAUTR or a country code"
  val value: Option[JsValue] = Some(Json.toJson(model))
}

case class NonUKDeemedParentMissingCountryOfIncorporation(model: DeemedParentModel)(implicit val path: JsPath)
    extends Validation {
  val code: String           = "DEEMED_PARENT_COUNTRY"
  val errorMessage: String   = "Enter a country of incorporation where the deemed parent is non-UK"
  val value: Option[JsValue] = Some(Json.toJson(model))
}

case class UKDeemedMissingUTR(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val code: String           = "DEEMED_PARENT_UTR"
  val errorMessage: String   = "Enter a UTR where the deemed parent is UK"
  val value: Option[JsValue] = Some(Json.toJson(model))
}

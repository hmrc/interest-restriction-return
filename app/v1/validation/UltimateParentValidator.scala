/*
 * Copyright 2025 HM Revenue & Customs
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
import v1.models.{UltimateParentModel, Validation}

trait UltimateParentValidator extends BaseValidation {

  import cats.implicits.*

  val ultimateParentModel: UltimateParentModel

  def validateSingleCompanyDetailSupplied(implicit path: JsPath): ValidationResult[UltimateParentModel] = {
    val numberOfDetailsSupplied = Seq(
      ultimateParentModel.ctutr,
      ultimateParentModel.sautr,
      ultimateParentModel.countryOfIncorporation
    ).flatten.length
    if (numberOfDetailsSupplied > 1) {
      UltimateParentWrongDetailsSuppliedError(ultimateParentModel).invalidNec
    } else {
      ultimateParentModel.validNec
    }
  }

  def validateUkCompanyDetails(implicit path: JsPath): ValidationResult[UltimateParentModel] = {
    val utrSupplied = ultimateParentModel.ctutr.isDefined || ultimateParentModel.sautr.isDefined

    if (ultimateParentModel.isUk && !utrSupplied) {
      UKParentMissingUTR(ultimateParentModel).invalidNec
    } else {
      ultimateParentModel.validNec
    }
  }

  def validateNonUkCompanyDetails(implicit path: JsPath): ValidationResult[UltimateParentModel] = {
    val countrySupplied = ultimateParentModel.countryOfIncorporation.isDefined

    if (!ultimateParentModel.isUk && !countrySupplied) {
      NonUKUltimateParentMissingCountryOfIncorporation(ultimateParentModel).invalidNec
    } else {
      ultimateParentModel.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[UltimateParentModel] =
    combineValidations(
      validateSingleCompanyDetailSupplied,
      validateUkCompanyDetails,
      validateNonUkCompanyDetails,
      ultimateParentModel.companyName.validate(path \ "companyName"),
      optionValidations(ultimateParentModel.ctutr.map(_.validate(path \ "ctutr"))),
      optionValidations(ultimateParentModel.sautr.map(_.validate(path \ "sautr"))),
      optionValidations(ultimateParentModel.countryOfIncorporation.map(_.validate(path \ "countryOfIncorporation"))),
      optionValidations(ultimateParentModel.legalEntityIdentifier.map(_.validate(path \ "legalEntityIdentifier")))
    ).map(_ => ultimateParentModel)
}

case class UltimateParentWrongDetailsSuppliedError(model: UltimateParentModel)(implicit val path: JsPath)
    extends Validation {
  val code: String           = "ULTIMATE_PARENT_DETAILS"
  val errorMessage: String   = "Ultimate parent must have either a CTUTR, a SAUTR or a country code"
  val value: Option[JsValue] = Some(Json.toJson(model))
}

case class NonUKUltimateParentMissingCountryOfIncorporation(model: UltimateParentModel)(implicit val path: JsPath)
    extends Validation {
  val code: String           = "ULTIMATE_PARENT_COUNTRY"
  val errorMessage: String   = "Enter a country of incorporation where the ultimate parent is non-UK"
  val value: Option[JsValue] = Some(Json.toJson(model))
}

case class UKParentMissingUTR(model: UltimateParentModel)(implicit val path: JsPath) extends Validation {
  val code: String           = "ULTIMATE_PARENT_UTR"
  val errorMessage: String   = "Enter a UTR where the ultimate parent is UK"
  val value: Option[JsValue] = Some(Json.toJson(model))
}

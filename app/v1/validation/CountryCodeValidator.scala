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

import config.Constants
import play.api.libs.json.{JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.{CountryCodeModel, Validation}

trait CountryCodeValidator extends BaseValidation {

  import cats.implicits._

  val countryCodeModel: CountryCodeModel

  private def validateLength(implicit path: JsPath): ValidationResult[CountryCodeModel] = {
    if(countryCodeModel.code.length != Constants.countryCodeLength) {
      CountryCodeLengthError(countryCodeModel).invalidNec
    } else {
      countryCodeModel.validNec
    }
  }

  private def validateValue(implicit path: JsPath): ValidationResult[CountryCodeModel] = {
    if (Constants.validCountryCodes.contains(countryCodeModel.code)) {
      countryCodeModel.validNec
    } else {
      CountryCodeValueError(countryCodeModel).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[CountryCodeModel] = combineValidationsForField(validateLength, validateValue)
}

case class CountryCodeValueError(countryCode: CountryCodeModel)(implicit val path: JsPath) extends Validation {
  val code = INVALID_COUNTRY_CODE
  val message: String = s"Country code ${countryCode.code} is not a valid country code"
}

case class CountryCodeLengthError(countryCode: CountryCodeModel)(implicit val path: JsPath) extends Validation {
  val code = INVALID_LENGTH
  val message: String =
    s"UTR is ${countryCode.code.length} character${if (countryCode.code.length != 1) "s" else ""} long and should be ${Constants.countryCodeLength}"
}
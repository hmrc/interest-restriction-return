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

import config.Constants
import models.Validation.ValidationResult
import models.{CountryCodeModel, Validation}
import play.api.libs.json.{JsPath, Json}

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
  val errorMessage: String = s"Country code ${countryCode.code} is not a valid country code"
  val value = Json.toJson(countryCode)
}

case class CountryCodeLengthError(countryCode: CountryCodeModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String =
    s"UTR is ${countryCode.code.length} character${if (countryCode.code.length != 1) "s" else ""} long and should be ${Constants.countryCodeLength}"
  val value = Json.toJson(countryCode)
}
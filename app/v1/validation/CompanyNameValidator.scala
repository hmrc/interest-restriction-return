/*
 * Copyright 2023 HM Revenue & Customs
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

import play.api.libs.json.{JsPath, JsString, JsValue}
import v1.models.Validation.ValidationResult
import v1.models.{CompanyNameModel, Validation}

trait CompanyNameValidator extends BaseValidation {

  import cats.implicits._

  val companyNameModel: CompanyNameModel

  private def validateCompanyNameLength(implicit topPath: JsPath): ValidationResult[String] =
    if (companyNameModel.name.nonEmpty && companyNameModel.name.length <= 160) {
      companyNameModel.name.validNec
    } else {
      CompanyNameLengthError(companyNameModel.name).invalidNec
    }

  private def validateCompanyNameCharacters(implicit topPath: JsPath): ValidationResult[String] = {
    val regex = "^[ -~¡-ÿĀ-ʯḀ-ỿ‐-―‘-‟₠-₿ÅK]*$".r
    companyNameModel.name match {
      case regex(_*) => companyNameModel.name.validNec
      case _         => CompanyNameCharactersError(companyNameModel.name).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[CompanyNameModel] =
    (validateCompanyNameLength, validateCompanyNameCharacters).mapN((_, _) => companyNameModel)
}

case class CompanyNameLengthError(name: String)(implicit val path: JsPath) extends Validation {
  val code: String           = "COMPANY_NAME_LENGTH"
  val errorMessage: String   = "Company name must be 1 to 160 characters long"
  val value: Option[JsValue] = Some(JsString(name))
}

case class CompanyNameCharactersError(name: String)(implicit val path: JsPath) extends Validation {
  val code: String           = "COMPANY_NAME_CHARACTERS"
  val errorMessage: String   = "Company name contains invalid characters"
  val value: Option[JsValue] = Some(JsString(name))
}

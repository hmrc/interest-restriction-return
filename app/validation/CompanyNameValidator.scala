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

package validation

import models.Validation.ValidationResult
import models.{CompanyNameModel, Validation}
import play.api.libs.json.{JsPath, JsString}

trait CompanyNameValidator extends BaseValidation {

  import cats.implicits._

  val companyNameModel: CompanyNameModel

  private def validateCompanyName(implicit topPath: JsPath): ValidationResult[String] = {
    if (companyNameModel.name.length >= 1 && companyNameModel.name.length <= 160) {
      companyNameModel.name.validNec
    } else {
      CompanyNameLengthError(companyNameModel.name).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[CompanyNameModel] =
    validateCompanyName.map(_ => companyNameModel)
}

case class CompanyNameLengthError(name: String)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = s"Company name is ${name.length} character${if (name.length != 1) "s" else ""} long and should be between 1 and 160"
  val value = JsString(name)
}
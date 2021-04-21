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

import config.Constants
import play.api.libs.json.{JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.{LegalEntityIdentifierModel, Validation}

trait LegalEntityIdentifierValidator extends BaseValidation {

  import cats.implicits._

  val legalEntityIdentifierModel: LegalEntityIdentifierModel

  private def validateValue(implicit path: JsPath): ValidationResult[LegalEntityIdentifierModel] = {
    val regex = "^[0-9A-Z]{18}[0-9]{2}$".r
    legalEntityIdentifierModel.code match {
      case regex(_ *) => legalEntityIdentifierModel.validNec
      case _ => LegalEntityIdentifierCharacterError(legalEntityIdentifierModel).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[LegalEntityIdentifierModel] = combineValidationsForField(validateValue)
}

case class LegalEntityIdentifierCharacterError(lei: LegalEntityIdentifierModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = s"legalEntityIdentifier should follow the ISO 17442 standard: 18 uppercase alphanumeric characters followed by 2 numeric characters"
  val value = Json.toJson(lei)
}
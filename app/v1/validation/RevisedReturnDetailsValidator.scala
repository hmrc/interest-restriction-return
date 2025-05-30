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
import v1.models.{RevisedReturnDetailsModel, Validation}

trait RevisedReturnDetailsValidator extends BaseValidation {

  import cats.implicits.*

  val revisedReturnDetailsModel: RevisedReturnDetailsModel

  private def revisedReturnDetailsIsValidLength: Boolean =
    revisedReturnDetailsModel.details.length >= 1 && revisedReturnDetailsModel.details.length <= 5000

  private def revisedReturnDetailsHasValidCharacters: Boolean = {
    val regex = "^[ -~¢-¥©®±×÷‐₠-₿−-∝≈≠≣-≥]*$".r
    revisedReturnDetailsModel.details match {
      case regex(_*) => true
      case _         => false
    }
  }

  def validate(implicit path: JsPath): ValidationResult[RevisedReturnDetailsModel] =
    revisedReturnDetailsModel.details match {
      case details if !revisedReturnDetailsIsValidLength      => RevisedReturnDetailsLengthError(details).invalidNec
      case details if !revisedReturnDetailsHasValidCharacters => RevisedReturnDetailsCharacterError(details).invalidNec
      case _                                                  => revisedReturnDetailsModel.validNec
    }

}

case class RevisedReturnDetailsLengthError(details: String)(implicit topPath: JsPath) extends Validation {
  val code: String           = "REVISION_DETAILS_LENGTH"
  val errorMessage: String   = "Revised return details must be between 1 and 5,000 characters"
  val path: JsPath           = topPath \ "revisedReturnDetails"
  val value: Option[JsValue] = Some(Json.toJson(details))
}

case class RevisedReturnDetailsCharacterError(details: String)(implicit topPath: JsPath) extends Validation {
  val code: String           = "REVISION_DETAILS_CHARACTERS"
  val errorMessage: String   = "Revised return details contains invalid characters"
  val path: JsPath           = topPath \ "revisedReturnDetails"
  val value: Option[JsValue] = Some(Json.toJson(details))
}

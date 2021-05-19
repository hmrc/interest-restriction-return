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

package v1.models.errors

import cats.data.NonEmptyChain
import play.api.libs.json._
import v1.models.Validation

case class ValidationErrorResponseModel(code: String, message: String, errors: Seq[ErrorResponseModel])

object ValidationErrorResponseModel {
  implicit val writes = Json.writes[ValidationErrorResponseModel]

  def apply(errors: Seq[(JsPath, Seq[JsonValidationError])]): ValidationErrorResponseModel = {

    val validationErrors = errors.map {
      case (path, errs) => 
        errs.flatMap(_.messages).map(message => ErrorResponseModel(code = "JSON_VALIDATION_ERROR", message = message, path = Some(path.toString)))
    }.flatten

    ValidationErrorResponseModel(
      code = "BAD_REQUEST",
      message = "Bad request",
      errors = validationErrors
    )
  }

  def apply(errors: NonEmptyChain[Validation]): ValidationErrorResponseModel = {
    ValidationErrorResponseModel(
      code = "BAD_REQUEST",
      message = "Bad request",
      errors = errors.map(ErrorResponseModel(_)).toChain.toList
    )
  }
}



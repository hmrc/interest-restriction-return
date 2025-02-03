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

package v1.models.errors

import cats.data.NonEmptyChain
import play.api.libs.json.*
import v1.models.Validation

case class ValidationErrorResponseModel(
  code: String,
  message: String,
  path: Option[String],
  value: Option[JsValue],
  errors: Option[Seq[ErrorResponseModel]]
)

object ValidationErrorResponseModel {

  implicit val writes: Writes[ValidationErrorResponseModel] = Json.writes[ValidationErrorResponseModel]

  val VALIDATION_ERROR_CODE     = "JSON_VALIDATION_ERROR"
  val BAD_REQUEST_ERROR_CODE    = "INVALID_REQUEST"
  val BAD_REQUEST_ERROR_MESSAGE = "Request contains validation errors"

  def apply(errors: Iterable[(JsPath, Iterable[JsonValidationError])]): ValidationErrorResponseModel = {
    val validationErrors = errors.flatMap { case (path, errs) =>
      errs
        .flatMap(_.messages)
        .map(message => ErrorResponseModel(code = VALIDATION_ERROR_CODE, message = message, path = Some(path.toString)))
    }

    errorsToValidationResponse(validationErrors)
  }

  def apply(errors: NonEmptyChain[Validation]): ValidationErrorResponseModel = {
    val validationErrors = errors.map(ErrorResponseModel(_)).toChain.toList
    errorsToValidationResponse(validationErrors)
  }

  def errorsToValidationResponse(errors: Iterable[ErrorResponseModel]): ValidationErrorResponseModel =
    errors match {
      case error :: Nil =>
        ValidationErrorResponseModel(
          code = error.code,
          message = error.message,
          path = error.path,
          value = error.value,
          errors = None
        )
      case _            =>
        ValidationErrorResponseModel(
          code = BAD_REQUEST_ERROR_CODE,
          message = BAD_REQUEST_ERROR_MESSAGE,
          errors = Some(errors.toList),
          path = None,
          value = None
        )
    }

}

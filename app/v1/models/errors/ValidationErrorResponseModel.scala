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

package v1.models.errors

import cats.data.NonEmptyChain
import play.api.libs.json._
import v1.models.Validation
import v1.validation._

case class ValidationErrorResponseModel(code: ErrorCode, path: JsPath, errors: Seq[String])
case class MultiValidationErrorResponseModel(code: ErrorCode, message: String, errors: Seq[ValidationErrorResponseModel])
object MultiValidationErrorResponseModel {
  implicit val writes: Writes[MultiValidationErrorResponseModel] = Json.writes[MultiValidationErrorResponseModel]
}

object ValidationErrorResponseModel {
  implicit val writes: Writes[ValidationErrorResponseModel] = Writes { model =>
    JsObject(Json.obj(
      "code" -> model.code.toString,
      "path" -> model.path.toString,
      "errors" -> model.errors
    ).fields.filterNot(_._2 == Json.obj()).toMap)
  }

  def apply(errors: Seq[(JsPath, Seq[JsonValidationError])]): MultiValidationErrorResponseModel =
    MultiValidationErrorResponseModel(
      code = BAD_REQUEST,
      message = "The request contained JSON validation errors",
      errors.map {
        case (field, errs) => ValidationErrorResponseModel(INVALID_JSON, field, errors = errs.flatMap(_.messages))
      }
    )

  def apply(errors: NonEmptyChain[Validation]): MultiValidationErrorResponseModel =
    MultiValidationErrorResponseModel(
      code = BAD_REQUEST,
      message = "The request did not pass business rule validation checks, errors are listed below",
      errors = errors.toChain.toList.map(errs =>
        ValidationErrorResponseModel(
          code = errs.code,
          path = errs.path,
          errors = errs.message.split("\\|")
        )
      )
    )
}



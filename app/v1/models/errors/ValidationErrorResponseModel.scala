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

case class ValidationErrorResponseModel(field: String, value: JsValue = Json.obj(), errors: Seq[String])

object ValidationErrorResponseModel {
  implicit val writes: Writes[ValidationErrorResponseModel] = Writes { models =>
    JsObject(Json.obj("field" -> models.field.toString(),
      "value" -> models.value,
      "errors" -> models.errors
    ).fields.filterNot(_._2 == Json.obj()).toMap)
  }

  def apply(errors: Seq[(JsPath, Seq[JsonValidationError])]): Seq[ValidationErrorResponseModel] = {
    errors.map {
      case (field, errs) => ValidationErrorResponseModel(field = field.toString, errors = errs.flatMap(_.messages))
    }
  }

  def apply(errors: NonEmptyChain[Validation]): Seq[ValidationErrorResponseModel] = {
    errors.toChain.toList.map(errs => ValidationErrorResponseModel(
      field = errs.path.toString,
      errors = errs.errorMessage.split("\\|"),
      value = errs.value
    ))
  }
}



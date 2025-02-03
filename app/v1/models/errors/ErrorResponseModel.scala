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

import play.api.libs.json.{JsValue, Json, Writes}
import v1.models.Validation

case class ErrorResponseModel(code: String, message: String, path: Option[String] = None, value: Option[JsValue] = None)

object ErrorResponseModel {
  implicit val writes: Writes[ErrorResponseModel]       = Json.writes[ErrorResponseModel]
  def apply(validation: Validation): ErrorResponseModel =
    ErrorResponseModel(
      code = validation.code,
      message = validation.errorMessage,
      path = Some(validation.path.toString),
      value = validation.value
    )
}

object ErrorResponses {

  // Standard Errors
  val NotFoundError: ErrorResponseModel        =
    ErrorResponseModel("MATCHING_RESOURCE_NOT_FOUND", "Matching resource not found")
  val DownstreamError: ErrorResponseModel      =
    ErrorResponseModel("INTERNAL_SERVER_ERROR", "An internal server error occurred")
  val BadRequestError: ErrorResponseModel      = ErrorResponseModel("INVALID_REQUEST", "Invalid request")
  val InvalidBodyTypeError: ErrorResponseModel =
    ErrorResponseModel("INVALID_BODY_TYPE", "Expecting text/json or application/json body")

  // Authorisation Errors
  val UnauthorisedError: ErrorResponseModel =
    ErrorResponseModel("CLIENT_OR_AGENT_NOT_AUTHORISED", "The client and/or agent is not authorised")

  // Accept header Errors
  val InvalidAcceptHeaderError: ErrorResponseModel =
    ErrorResponseModel("ACCEPT_HEADER_INVALID", "The accept header is missing or invalid")
  val UnsupportedVersionError: ErrorResponseModel  =
    ErrorResponseModel("NOT_FOUND", "The requested resource could not be found")

}

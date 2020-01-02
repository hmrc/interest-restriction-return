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

package connectors

import play.api.Logger
import play.api.http.Status.{INTERNAL_SERVER_ERROR, OK}
import play.api.libs.json.Json
import uk.gov.hmrc.http.HttpResponse

object HttpHelper {
  type SubmissionHttpResponse = Either[ErrorResponse, SuccessResponse]
  type CRNHttpResponse = Either[ErrorResponse, Boolean]

  def read(response: HttpResponse, parserName: String, unexpectedErrorMessage: String): SubmissionHttpResponse = {
    response.status match {
      case OK =>
        Logger.debug(s"[$parserName][read]: Status OK")
        Logger.debug(s"[$parserName][read]: Json Response: ${response.json}")
        response.json.validate[SuccessResponse](SuccessResponse.fmt).fold(
          invalid => {
            Logger.warn(s"[$parserName][read]: Invalid Success Response Json - $invalid")
            Left(InvalidSuccessResponse)
          },
          valid => Right(valid)
        )
      case status =>
        Logger.warn(s"[$parserName][read]: Unexpected response, status $status returned")
        Left(UnexpectedFailure(response.status,s"Status ${response.status} $unexpectedErrorMessage"))
    }
  }
}

case class SuccessResponse(acknowledgementReference: String)
object SuccessResponse {
  implicit val fmt = Json.format[SuccessResponse]
}

trait ErrorResponse {
  val status: Int
  val body: String
}
object InvalidSuccessResponse extends ErrorResponse {
  override val status: Int = INTERNAL_SERVER_ERROR
  override val body: String =  HttpErrorMessages.UNEXPECTED_ERROR
}
case class UnexpectedFailure(override val status: Int,override val body: String) extends ErrorResponse

object HttpErrorMessages {
  val ABBREVIATED_ERROR = "Error returned when trying to submit abbreviated return"
  val APPOINT_ERROR = "Error returned when trying to appoint a reporting company"
  val CRN_UNEXPECTED_ERROR = "Error returned when calling Companies House"
  val CRN_INVALID_ERROR = "CRN Not Found on Companies House"
  val FULL_ERROR = "Error returned when trying to submit a full return"
  val REVOKE_ERROR = "Error returned when trying to revoke a reporting company"
  val UNEXPECTED_ERROR = "Invalid Json returned in Success response"
}

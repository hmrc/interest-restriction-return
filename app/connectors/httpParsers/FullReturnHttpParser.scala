/*
 * Copyright 2019 HM Revenue & Customs
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

package connectors.httpParsers

import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object FullReturnHttpParser {

  type FullReturnResponse = Either[ErrorResponse, SuccessResponse]

  implicit object FullReturnReads extends HttpReads[FullReturnResponse] {

    override def read(method: String, url: String, response: HttpResponse): FullReturnResponse = {

      response.status match {
        case OK =>
          Logger.debug("[FullReturnHttpParser][read]: Status OK")
          Logger.debug(s"[FullReturnHttpParser][read]: Json Response: ${response.json}")
          response.json.validate[SuccessResponse](SuccessResponse.fmt).fold(
            invalid => {
              Logger.warn(s"[FullReturnHttpParser][read]: Invalid Success Response Json - $invalid")
              Left(InvalidSuccessResponse)
            },
            valid => Right(valid)
          )
        case status =>
          Logger.warn(s"[FullReturnReads][read]: Unexpected response, status $status returned")
          Left(UnexpectedFailure(status, s"Status $status Error returned when trying to submit a full return"))
      }
    }
  }

  case class SuccessResponse(acknowledgementReference: String)
  object SuccessResponse {
    implicit val fmt = Json.format[SuccessResponse]
  }

  sealed trait ErrorResponse {
    val status: Int = INTERNAL_SERVER_ERROR
    val body: String
  }
  object InvalidSuccessResponse extends ErrorResponse {
    override val body: String = "Invalid Json returned in Success response"
  }
  case class UnexpectedFailure(override val status: Int, override val body: String) extends ErrorResponse
}

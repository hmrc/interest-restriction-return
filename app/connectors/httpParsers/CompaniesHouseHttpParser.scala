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

object CompaniesHouseHttpParser {

  type CompaniesHouseResponse = Either[ErrorResponse, SuccessResponse]

  implicit object CompaniesHouseReads extends HttpReads[CompaniesHouseResponse] {

    override def read(method: String, url: String, response: HttpResponse): CompaniesHouseResponse = {

      response.status match {
        case OK =>
          Logger.debug("[CompaniesHouseHttpParser][read]: Status OK")
          Logger.debug(s"[CompaniesHouseHttpParser][read]: Json Response: ${response.json}")
          Right(ValidCRN)
        case NOT_FOUND =>
          Left(InvalidCRN)
        case status =>
          Logger.warn(s"[CompaniesHouseReads][read]: Unexpected response, status $status returned")
          Left(UnexpectedFailure(status, s"Status $status Error returned when calling Companies House"))
      }
    }
  }

  sealed trait SuccessResponse
  case object ValidCRN extends SuccessResponse

  sealed trait ErrorResponse {
    val status: Int = INTERNAL_SERVER_ERROR
    val body: String
  }
  case class UnexpectedFailure(override val status: Int, override val body: String) extends ErrorResponse
  case object InvalidCRN extends ErrorResponse {
    override val status: Int = NOT_FOUND
    override val body: String = "CRN Not Found on Companies House"
  }
}

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

package v1.connectors.httpParsers

import v1.connectors.HttpHelper.CompaniesHouseResponse
import v1.connectors.{HttpErrorMessages, InvalidCRN, UnexpectedFailure, ValidCRN}
import play.api.Logging
import play.api.http.Status._
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object CompaniesHouseHttpParser {

  implicit object CompaniesHouseReads extends HttpReads[CompaniesHouseResponse] with Logging {

    override def read(method: String, url: String, response: HttpResponse): CompaniesHouseResponse = {
      response.status match {
        case OK =>
          logger.debug("[CompaniesHouseHttpParser][read]: Status OK")
          logger.debug(s"[CompaniesHouseHttpParser][read]: Json Response: ${response.body}")
          Right(ValidCRN)
        case NOT_FOUND =>
          logger.debug("[CompaniesHouseHttpParser][read]: Status NOT FOUND")
          logger.debug(s"[CompaniesHouseHttpParser][read]: Json Response: ${response.body}")
          Left(InvalidCRN)
        case status =>
          logger.warn(s"[CompaniesHouseReads][read]: Unexpected response, status $status returned")
          Left(UnexpectedFailure(response.status,s"Status ${response.status} ${HttpErrorMessages.CRN_UNEXPECTED_ERROR}"))
      }
    }
  }
}

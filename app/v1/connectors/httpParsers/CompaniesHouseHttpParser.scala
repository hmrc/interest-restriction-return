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

package v1.connectors.httpParsers

import v1.connectors.HttpHelper.CompaniesHouseResponse
import v1.connectors.{HttpErrorMessages, InvalidCRN, UnexpectedFailure, ValidCRN}
import play.api.Logger
import play.api.http.Status._
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object CompaniesHouseHttpParser {

  implicit object CompaniesHouseReads extends HttpReads[CompaniesHouseResponse] {

    override def read(method: String, url: String, response: HttpResponse): CompaniesHouseResponse = {
      response.status match {
        case OK =>
          Logger.debug("[CompaniesHouseHttpParser][read]: Status OK")
          Logger.debug(s"[CompaniesHouseHttpParser][read]: Json Response: ${response.body}")
          Right(ValidCRN)
        case NOT_FOUND =>
          Logger.debug("[CompaniesHouseHttpParser][read]: Status NOT FOUND")
          Logger.debug(s"[CompaniesHouseHttpParser][read]: Json Response: ${response.body}")
          Left(InvalidCRN)
        case status =>
          Logger.warn(s"[CompaniesHouseReads][read]: Unexpected response, status $status returned")
          Left(UnexpectedFailure(response.status,s"Status ${response.status} ${HttpErrorMessages.CRN_UNEXPECTED_ERROR}"))
      }
    }
  }
}

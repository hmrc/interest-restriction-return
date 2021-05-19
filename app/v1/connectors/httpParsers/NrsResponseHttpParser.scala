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

import play.api.Logging
import v1.connectors.HttpHelper.NrsResponse
import uk.gov.hmrc.http.{HttpReads, HttpResponse}
import play.api.http.Status.{OK, INTERNAL_SERVER_ERROR}
import v1.connectors.{InvalidSuccessResponse, HttpErrorMessages, UnexpectedFailure}
import v1.models.nrs.NrSubmissionId

object NrsResponseHttpParser extends Logging {

  implicit object RevokeReportingCompanyReads extends HttpReads[NrsResponse] {

    override def read(method: String, url: String, response: HttpResponse): NrsResponse = {
      response.status match {
        case OK =>
          response.json.validate[NrSubmissionId].fold(
            invalid => {
              logger.error(s"Invalid success response json from NRS - $invalid")
              Left(InvalidSuccessResponse)
            },
            valid => Right(valid)
          )
        case status =>
          logger.error(s"Unexpected response from NRS, status $status returned with body ${response.body}")
          Left(UnexpectedFailure(INTERNAL_SERVER_ERROR,s"Status $INTERNAL_SERVER_ERROR ${HttpErrorMessages.NRS_ERROR}"))
      }
    }
  }
}
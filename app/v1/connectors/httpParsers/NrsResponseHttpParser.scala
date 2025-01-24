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

package v1.connectors.httpParsers

import play.api.Logging
import play.api.http.Status.ACCEPTED
import uk.gov.hmrc.http.{HttpReads, HttpResponse}
import v1.connectors.HttpHelper.NrsResponse
import v1.connectors.{HttpErrorMessages, InvalidSuccessResponse, UnexpectedFailure}
import v1.models.nrs.NrSubmissionId
//import play.api.libs.json.OFormat.oFormatFromReadsAndOWrites
//import play.api.libs.json.Format.GenericFormat

object NrsResponseHttpParser extends Logging {

  implicit object NrsResponseReads extends HttpReads[NrsResponse] {

    override def read(method: String, url: String, response: HttpResponse): NrsResponse =
      response.status match {
        case ACCEPTED =>
          response.json
            .validate[NrSubmissionId]
            .fold(
              invalid => {
                logger.error(s"[NrsResponseHttpParser][read] Invalid success response json from NRS - $invalid")
                Left(InvalidSuccessResponse())
              },
              valid => Right(valid)
            )
        case status   =>
          logger.error(
            s"[NrsResponseHttpParser][read] Unexpected response from NRS, status $status returned with body ${response.body}"
          )
          Left(UnexpectedFailure(status, s"Status $status ${HttpErrorMessages.NRS_ERROR}"))
      }
  }
}

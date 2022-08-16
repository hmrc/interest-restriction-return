/*
 * Copyright 2022 HM Revenue & Customs
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
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.{DesBaseConnector, HttpErrorMessages}
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object AbbreviatedReturnHttpParser extends DesBaseConnector with Logging {

  implicit object AbbreviatedReturnReads extends HttpReads[SubmissionResponse] {

    override def read(method: String, url: String, response: HttpResponse): SubmissionResponse =
      handleHttpResponse(response, "AbbreviatedReturnHttpParser", HttpErrorMessages.ABBREVIATED_ERROR)
  }
}

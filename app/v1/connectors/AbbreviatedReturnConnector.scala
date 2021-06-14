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

package v1.connectors

import config.AppConfig
import play.api.Logging
import play.api.http.HeaderNames
import play.api.libs.json.Json
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}
import utils.FeatureSwitchJsonFormatter
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.httpParsers.AbbreviatedReturnHttpParser.AbbreviatedReturnReads
import v1.models.abbreviatedReturn.AbbreviatedReturnModel
import v1.models.requests.IdentifierRequest

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AbbreviatedReturnConnector @Inject()(httpClient: HttpClient,
                                           jsonFormatters: FeatureSwitchJsonFormatter,
                                           implicit val appConfig: AppConfig) extends DesBaseConnector with Logging {

  private[connectors] lazy val abbreviatedReturnUrl = s"${appConfig.desUrl}/organisations/interest-restrictions-return/abbreviated"

  def submitAbbreviatedReturn(abbreviatedReturnModel: AbbreviatedReturnModel)
                             (implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[_]): Future[SubmissionResponse] = {
    logger.debug(s"URL: $abbreviatedReturnUrl")
    val receivedSize = request.headers.get(HeaderNames.CONTENT_LENGTH)
    val jsonSize = Json.stringify(Json.toJson(abbreviatedReturnModel)(jsonFormatters.abbreviatedReturnWrites)).length
    logger.debug(s"Size of content received: $receivedSize sent: $jsonSize")

    httpClient.POST(abbreviatedReturnUrl, abbreviatedReturnModel)(jsonFormatters.abbreviatedReturnWrites, AbbreviatedReturnReads, desHc, ec)
  }
}

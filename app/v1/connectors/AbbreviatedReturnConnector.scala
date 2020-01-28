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

package v1.connectors

import config.AppConfig
import javax.inject.Inject
import play.api.Logger
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.httpParsers.AbbreviatedReturnHttpParser.AbbreviatedReturnReads
import v1.models.abbreviatedReturn.AbbreviatedReturnModel
import v1.models.requests.IdentifierRequest

import scala.concurrent.{ExecutionContext, Future}

class AbbreviatedReturnConnector @Inject()(httpClient: HttpClient,
                                           implicit val appConfig: AppConfig) extends DesBaseConnector {

  private[connectors] lazy val abbreviatedReturnUrl = s"${appConfig.desUrl}/interest-restriction/return/abbreviated"

  def submitAbbreviatedReturn(abbreviatedReturnModel: AbbreviatedReturnModel)
                             (implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[_]): Future[SubmissionResponse] = {

    Logger.debug(s"[AbbreviatedReturnConnector][submitAbbreviatedReturn] URL: $abbreviatedReturnUrl")
    Logger.debug(s"[AbbreviatedReturnConnector][submitAbbreviatedReturn] Headers: ${desHc.headers}")
    Logger.debug(s"[AbbreviatedReturnConnector][submitAbbreviatedReturn] Body: \n\n ${Json.toJson(abbreviatedReturnModel)}")

    httpClient.POST(abbreviatedReturnUrl, abbreviatedReturnModel)(AbbreviatedReturnModel.format, AbbreviatedReturnReads, desHc, ec)
  }

}
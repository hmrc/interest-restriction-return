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

package connectors

import config.AppConfig
import connectors.httpParsers.AbbreviatedReturnHttpParser.{AbbreviatedReturnReads, AbbreviatedReturnResponse}
import javax.inject.Inject
import models.abbreviatedReturn.AbbreviatedReturnModel
import models.requests.IdentifierRequest
import play.api.Logger
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class AbbreviatedReturnConnector @Inject()(httpClient: HttpClient,
                                           implicit val appConfig: AppConfig) extends DesBaseConnector {

  private[connectors] lazy val abbreviatedReturnUrl = s"${appConfig.desUrl}/interest-restriction/return/abbreviated"

  def submitAbbreviatedReturn(abbreviatedReturnModel: AbbreviatedReturnModel)
                             (implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[_]): Future[AbbreviatedReturnResponse] = {

    Logger.debug(s"[AbbreviatedReturnConnector][submitAbbreviatedReturn] URL: $abbreviatedReturnUrl")
    Logger.debug(s"[AbbreviatedReturnConnector][submitAbbreviatedReturn] Headers: ${desHc.headers}")
    Logger.debug(s"[AbbreviatedReturnConnector][submitAbbreviatedReturn] Body: \n\n ${Json.toJson(abbreviatedReturnModel)}")

    httpClient.POST(abbreviatedReturnUrl, abbreviatedReturnModel)(AbbreviatedReturnModel.format, AbbreviatedReturnReads, desHc, ec)
  }

}

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

package connectors

import config.AppConfig
import connectors.httpParsers.RevokeReportingCompanyHttpParser.{RevokeReportingCompanyReads, RevokeReportingCompanyResponse}
import javax.inject.Inject
import models.requests.IdentifierRequest
import models.revokeReportingCompany.RevokeReportingCompanyModel
import play.api.Logger
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class RevokeReportingCompanyConnector @Inject()(httpClient: HttpClient,
                                                implicit val appConfig: AppConfig) extends DesBaseConnector {

  private[connectors] lazy val revokeUrl = s"${appConfig.desUrl}/interest-restriction/reporting-company/revoke"

  def revoke(revokeReportingCompanyModel: RevokeReportingCompanyModel)
             (implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[_]): Future[RevokeReportingCompanyResponse] = {

    Logger.debug(s"[RevokeReportingCompanyConnector][revoke] URL: $revokeUrl")
    Logger.debug(s"[RevokeReportingCompanyConnector][revoke] Headers: ${desHc.headers}")
    Logger.debug(s"[RevokeReportingCompanyConnector][revoke] Body: \n\n ${Json.toJson(revokeReportingCompanyModel)}")

    httpClient.POST(revokeUrl, revokeReportingCompanyModel)(RevokeReportingCompanyModel.format, RevokeReportingCompanyReads, desHc, ec)
  }

}

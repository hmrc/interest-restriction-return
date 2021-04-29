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

import audit.{AuditEventTypes}
import config.AppConfig

import javax.inject.Inject
import play.api.Logging
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.HttpClient
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.httpParsers.RevokeReportingCompanyHttpParser.RevokeReportingCompanyReads
import v1.models.requests.IdentifierRequest
import v1.models.revokeReportingCompany.RevokeReportingCompanyModel

import scala.concurrent.{ExecutionContext, Future}

class RevokeReportingCompanyConnector @Inject()(httpClient: HttpClient,
                                                implicit val appConfig: AppConfig) extends DesBaseConnector with Logging with AuditEventTypes {

  private[connectors] lazy val revokeUrl = s"${appConfig.desUrl}/organisations/interest-restrictions-return/revoke"

  def revoke(revokeReportingCompanyModel: RevokeReportingCompanyModel)
             (implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[_]): Future[SubmissionResponse] = {

    logger.debug(s"[RevokeReportingCompanyConnector][submit] URL: $revokeUrl")
    logger.debug(s"[RevokeReportingCompanyConnector][submit] Headers: ${desHc.headers}")

    httpClient.POST(revokeUrl, revokeReportingCompanyModel)(RevokeReportingCompanyModel.format, RevokeReportingCompanyReads, desHc, ec)
  }

}

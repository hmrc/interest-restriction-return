/*
 * Copyright 2025 HM Revenue & Customs
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

import com.google.inject.Singleton
import config.AppConfig
import play.api.http.HeaderNames
import play.api.libs.json.Json
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.httpParsers.RevokeReportingCompanyHttpParser.RevokeReportingCompanyReads
import v1.models.requests.IdentifierRequest
import v1.models.revokeReportingCompany.RevokeReportingCompanyModel
import play.api.libs.ws.writeableOf_JsValue

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RevokeReportingCompanyConnector @Inject() (httpClient: HttpClientV2, implicit val appConfig: AppConfig)
    extends DesBaseConnector {

  private[connectors] lazy val revokeUrl = s"${appConfig.desUrl}/organisations/interest-restrictions-return/revoke"

  def revoke(
    revokeReportingCompanyModel: RevokeReportingCompanyModel
  )(implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[?]): Future[SubmissionResponse] = {
    logger.debug(s"[RevokeReportingCompanyConnector][revoke] URL: $revokeUrl")
    val receivedSize = request.headers.get(HeaderNames.CONTENT_LENGTH)
    val jsonSize     = Json.stringify(Json.toJson(revokeReportingCompanyModel)(RevokeReportingCompanyModel.format)).length
    logger.debug(s"[RevokeReportingCompanyConnector][revoke] Size of content received: $receivedSize sent: $jsonSize")

    httpClient
      .post(url"$revokeUrl")
      .setHeader(desHeaders()*)
      .withBody(Json.toJson(revokeReportingCompanyModel))
      .execute[SubmissionResponse]
  }

  def revokeHip(
    revokeReportingCompanyModel: RevokeReportingCompanyModel
  )(implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[?]): Future[SubmissionResponse] = {
    val fullUrl      = appConfig.hipRevokeReportingCompanyUrl
    logger.debug(s"[RevokeReportingCompanyConnector][revokeHip] URL: $fullUrl")
    val receivedSize = request.headers.get(HeaderNames.CONTENT_LENGTH)
    val jsonSize     = Json.stringify(Json.toJson(revokeReportingCompanyModel)(RevokeReportingCompanyModel.format)).length
    logger.debug(
      s"[RevokeReportingCompanyConnector][revokeHip] Size of content received: $receivedSize sent: $jsonSize"
    )

    httpClient
      .post(url"$fullUrl")
      .setHeader(hipHeaders(appConfig)*)
      .withBody(Json.toJson(revokeReportingCompanyModel))
      .execute[SubmissionResponse]
  }

}

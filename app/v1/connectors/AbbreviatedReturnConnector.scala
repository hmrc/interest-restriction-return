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
import play.api.libs.ws.writeableOf_JsValue
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import utils.JsonFormatters
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.httpParsers.AbbreviatedReturnHttpParser.AbbreviatedReturnReads
import v1.models.abbreviatedReturn.AbbreviatedReturnModel
import v1.models.requests.IdentifierRequest

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AbbreviatedReturnConnector @Inject() (httpClient: HttpClientV2, implicit val appConfig: AppConfig)
    extends BaseConnector
    with JsonFormatters {

  private[connectors] lazy val abbreviatedReturnUrl =
    s"${appConfig.desUrl}/organisations/interest-restrictions-return/abbreviated"

  def submitAbbreviatedReturn(
    abbreviatedReturnModel: AbbreviatedReturnModel
  )(implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[?]): Future[SubmissionResponse] = {
    logger.debug(s"[AbbreviatedReturnConnector][submitAbbreviatedReturn] URL: $abbreviatedReturnUrl")
    val receivedSize = request.headers.get(HeaderNames.CONTENT_LENGTH)
    val jsonSize     = Json.stringify(Json.toJson(abbreviatedReturnModel)).length
    logger.debug(
      s"[AbbreviatedReturnConnector][submitAbbreviatedReturn] Size of content received: $receivedSize sent: $jsonSize"
    )

    httpClient
      .post(url"$abbreviatedReturnUrl")
      .setHeader(desHeaders()*)
      .withBody(Json.toJson(abbreviatedReturnModel))
      .execute[SubmissionResponse]
  }

  def submitAbbreviatedReturnHip(
    abbreviatedReturnModel: AbbreviatedReturnModel
  )(implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[?]): Future[SubmissionResponse] = {
    val fullUrl      = appConfig.hipAbbreviatedReturnUrl
    logger.debug(s"[AbbreviatedReturnConnector][submitAbbreviatedReturnHip] URL: $fullUrl")
    val receivedSize = request.headers.get(HeaderNames.CONTENT_LENGTH)
    val jsonSize     = Json.stringify(Json.toJson(abbreviatedReturnModel)).length
    logger.debug(
      s"[AbbreviatedReturnConnector][submitAbbreviatedReturnHip] Size of content received: $receivedSize sent: $jsonSize"
    )

    httpClient
      .post(url"$fullUrl")
      .setHeader(hipHeaders(appConfig)*)
      .withBody(Json.toJson(abbreviatedReturnModel))
      .execute[SubmissionResponse]
  }

}

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

package v1.connectors

import config.AppConfig
import play.api.Logging
import play.api.libs.json.Json
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import v1.connectors.HttpHelper.NrsResponse
import v1.connectors.httpParsers.NrsResponseHttpParser.NrsResponseReads
import v1.models.nrs.*
import play.api.libs.ws.writeableOf_JsValue

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

trait NrsConnector {
  def send(nrsPayload: NrsPayload): Future[NrsResponse]
}

@Singleton
class NrsConnectorImpl @Inject() (httpClient: HttpClientV2, implicit val appConfig: AppConfig)(implicit
  ec: ExecutionContext
) extends NrsConnector
    with Logging {

  private val XApiKey = "X-API-Key"

  private def post(payload: NrsPayload, url: String, authToken: String): Future[NrsResponse] = {

    implicit val hc: HeaderCarrier = HeaderCarrier()

    val fullNrsUrl = url + "/submission"

    logger.info(s"[NrsConnectorImpl][post] Sending request to NRS service. Url: $fullNrsUrl")

    httpClient
      .post(url"$fullNrsUrl")
      .setHeader("Content-Type" -> "application/json", XApiKey -> authToken)
      .withBody(Json.toJson(payload))
      .execute[NrsResponse]
  }

  def send(nrsPayload: NrsPayload): Future[NrsResponse] =
    (appConfig.nrsUrl, appConfig.nrsAuthorisationToken) match {
      case (Some(url), Some(token)) =>
        post(nrsPayload, url, token)
      case _                        =>
        logger.error(
          s"[NrsConnectorImpl][send] NRS config failure: nrsUrl: ${appConfig.nrsUrl} and nrsAuthorisationToken: ${appConfig.nrsAuthorisationToken}"
        )
        Future(
          Left(
            NrsError(
              "[NrsConnectorImpl][send] NRS URL and token need to be configured in the application.conf or NRS is disabled"
            )
          )
        )
    }
}

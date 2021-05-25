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
import com.google.inject._
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.HttpClient
import play.api.Logging
import v1.models.nrs._
import scala.util.{Success, Failure}
import v1.connectors.HttpHelper.NrsResponse
import v1.connectors.httpParsers.NrsResponseHttpParser._

import scala.concurrent.{ExecutionContext, Future}

trait NrsConnector {
  def send[A](nrsPayload: NrsPayload): Future[NrsResponse]
}

@Singleton
class NrsConnectorImpl @Inject()(http: HttpClient, implicit val appConfig: AppConfig)(implicit ec: ExecutionContext) extends NrsConnector with Logging {

  private val XApiKey = "X-API-Key"

  def send[A](nrsPayload: NrsPayload): Future[NrsResponse] = 
    (appConfig.nrsUrl, appConfig.nrsAuthorisationToken) match {
      case (Some(url), Some(token)) => post(nrsPayload, url, token)
      case _ => Future.failed(new NrsConfigurationException)
    }

  private def post[A](payload: NrsPayload, url: String, authToken: String): Future[NrsResponse] = {

    implicit val hc: HeaderCarrier = HeaderCarrier()

    logger.debug(s"Sending request to NRS service. Url: $url Payload:\n${Json.prettyPrint(Json.toJson(payload))}")
    val result = http.POST[NrsPayload, NrsResponse](url, payload, Seq[(String, String)](("Content-Type", "application/json"), (XApiKey, authToken)))
    result.onComplete {
      case Success(response) => logger.info(s"Response received from NRS service: ${response}")
      case Failure(e) => logger.error(s"Call to NRS service failed url=$url, exception=$e", e)
    }
    result
  }
}

class NrsConfigurationException extends Exception("NRS URL and token needs to be configured in the application.conf", None.orNull)

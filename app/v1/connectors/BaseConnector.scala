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

import config.AppConfig
import play.api.Logging
import play.api.http.Status.{CREATED, INTERNAL_SERVER_ERROR, OK}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import v1.connectors.HttpHelper.SubmissionResponse
import v1.models.requests.IdentifierRequest

import java.util.UUID.randomUUID
import scala.util.matching.Regex

trait BaseConnector extends Logging with HttpConnectorHeaders {

  private val requestIdPattern: Regex = """.*([A-Za-z0-9]{8}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}).*""".r

  def desHeaders()(implicit
    hc: HeaderCarrier,
    appConfig: AppConfig,
    request: IdentifierRequest[?]
  ): Seq[(String, String)]   =
    Seq(
      appConfig.desEnvironment,
      PROVIDER_ID_HEADER   -> request.identifier,
      CORRELATION_HEADER   -> getCorrelationId(hc),
      AUTHORIZATION_HEADER -> appConfig.desAuthorisationToken
    )

  def hipHeaders(appConfig: AppConfig)(implicit hc: HeaderCarrier): Seq[(String, String)] =
    Seq(
      CORRELATION_HEADER   -> getCorrelationId(hc),
      AUTHORIZATION_HEADER -> s"Basic ${appConfig.hipAuthorizationToken}"
    )

  def generateNewUUID: String = randomUUID.toString

  def getCorrelationId(hc: HeaderCarrier): String =
    try {
      val candidateUUID = hc.requestId
        .map(_.value)
        .flatMap(rid => requestIdPattern.findFirstMatchIn(rid).map(_.group(1)))
        .map(prefix => s"$prefix-${generateNewUUID.takeRight(12)}")
        .getOrElse(generateNewUUID)

      // Validate the UUID by attempting to parse it
      // happened once that prefix had not uuid type format so generated cid was invalid
      java.util.UUID.fromString(candidateUUID)
      candidateUUID
    } catch {
      case _: Exception => generateNewUUID
    }

  def handleHttpResponse(
    response: HttpResponse,
    parserName: String,
    unexpectedErrorMessage: String
  ): SubmissionResponse =
    response.status match {
      case CREATED | OK =>
        logger.info(s"[DesBaseConnector][handleHttpResponse] Successfully created with response $response")
        logger.debug(s"[DesBaseConnector][handleHttpResponse] Json Response: ${response.json}")
        response.json
          .validate[DesSuccessResponse](DesSuccessResponse.fmt)
          .fold(
            invalid => {
              logger.error(s"[DesBaseConnector][handleHttpResponse] Invalid Success Response Json - $invalid")
              Left(InvalidSuccessResponse())
            },
            valid => Right(valid)
          )
      case status       =>
        logger.error(
          s"[DesBaseConnector][handleHttpResponse] Unexpected response, status $status returned with body ${response.body}"
        )
        Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, s"Status $INTERNAL_SERVER_ERROR $unexpectedErrorMessage"))
    }
}

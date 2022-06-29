/*
 * Copyright 2022 HM Revenue & Customs
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
import v1.connectors.HttpHelper.SubmissionResponse
import play.api.Logging
import play.api.http.Status.{CREATED, INTERNAL_SERVER_ERROR}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import v1.models.requests.IdentifierRequest


import java.util.UUID.randomUUID

trait DesBaseConnector extends Logging {

  val uuidIdBeginIndex: Int = 24

  def desHc(implicit hc: HeaderCarrier, appConfig: AppConfig, request: IdentifierRequest[_]): HeaderCarrier = {
    val correlationId = correlationIdGenerator(hc)
    logger.debug(s"Prepping message with correlationId header: $correlationId")
    hc.withExtraHeaders(appConfig.desEnvironmentHeader, "providerId" -> request.identifier,
      "correlationId" -> correlationId, "Authorization" -> appConfig.desAuthorisationToken)
  }

  def generateNewUUID : String = randomUUID.toString

  def correlationIdGenerator(hc: HeaderCarrier): String = {
    val CorrelationIdPattern = """.*([A-Za-z0-9]{8}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}).*""".r
    hc.requestId match {
      case Some(requestId) => requestId.value match {
        case CorrelationIdPattern(prefix) => prefix + "-" + generateNewUUID.substring(uuidIdBeginIndex)
        case _ => generateNewUUID
      }
      case _ => generateNewUUID
    }
  }

  def handleHttpResponse(response: HttpResponse, parserName: String, unexpectedErrorMessage: String): SubmissionResponse = {
    response.status match {
      case CREATED =>
        logger.info(s"Successfully created with response ${response}")
        logger.debug(s"Status CREATED")
        logger.debug(s"Json Response: ${response.json}")
        response.json.validate[DesSuccessResponse](DesSuccessResponse.fmt).fold(
          invalid => {
            logger.error(s"Invalid Success Response Json - $invalid")
            Left(InvalidSuccessResponse)
          },
          valid => Right(valid)
        )
      case status =>
        logger.error(s"Unexpected response, status $status returned with body ${response.body}")
        Left(UnexpectedFailure(INTERNAL_SERVER_ERROR,s"Status $INTERNAL_SERVER_ERROR $unexpectedErrorMessage"))
    }
  }
}

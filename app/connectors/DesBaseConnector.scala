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
import connectors.HttpHelper.SubmissionResponse
import models.requests.IdentifierRequest
import play.api.Logger
import play.api.http.Status.OK
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.http.logging.Authorization

trait DesBaseConnector {

  def desHc(implicit hc: HeaderCarrier, appConfig: AppConfig, request: IdentifierRequest[_]): HeaderCarrier =
    hc.withExtraHeaders(appConfig.desEnvironmentHeader, "providerId" -> request.identifier)
      .copy(authorization = Some(Authorization(appConfig.desAuthorisationToken)))

  def handleHttpResponse(response: HttpResponse, parserName: String, unexpectedErrorMessage: String): SubmissionResponse = {
    response.status match {
      case OK =>
        Logger.debug(s"[$parserName][read]: Status OK")
        Logger.debug(s"[$parserName][read]: Json Response: ${response.json}")
        response.json.validate[DesSuccessResponse](DesSuccessResponse.fmt).fold(
          invalid => {
            Logger.warn(s"[$parserName][read]: Invalid Success Response Json - $invalid")
            Left(InvalidSuccessResponse)
          },
          valid => Right(valid)
        )
      case status =>
        Logger.warn(s"[$parserName][read]: Unexpected response, status $status returned")
        Left(UnexpectedFailure(response.status,s"Status ${response.status} $unexpectedErrorMessage"))
    }
  }
}

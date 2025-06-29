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

package v1.services

import com.google.inject.Singleton
import config.AppConfig
import play.api.Logging

import javax.inject.Inject
import uk.gov.hmrc.http.HeaderCarrier
import v1.connectors.FullReturnConnector
import v1.connectors.HttpHelper.SubmissionResponse
import v1.models.fullReturn.FullReturnModel
import v1.models.requests.IdentifierRequest

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FullReturnService @Inject() (connector: FullReturnConnector, appConfig: AppConfig)
    extends Submission[FullReturnModel]
    with Logging {

  override def submit(
    fullReturn: FullReturnModel
  )(implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[?]): Future[SubmissionResponse] =
    if (appConfig.isHipEnabled) {
      logger.info("[FullReturnService][submit] Submitting to HIP")
      connector.submitHip(fullReturn).map { resp =>
        logger.info(
          "[FullReturnService][submit] Successfully sent a full return payload to HIP"
        )
        resp
      }
    } else {
      logger.info("[FullReturnService][submit] Submitting to DES")
      connector.submit(fullReturn).map { resp =>
        logger.info(
          "[FullReturnService][submit] Successfully sent a full return payload to DES"
        )
        resp
      }
    }

}

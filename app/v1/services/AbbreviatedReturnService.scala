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
import v1.connectors.AbbreviatedReturnConnector
import v1.connectors.HttpHelper.SubmissionResponse
import v1.models.abbreviatedReturn.AbbreviatedReturnModel
import v1.models.requests.IdentifierRequest

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AbbreviatedReturnService @Inject() (connector: AbbreviatedReturnConnector, appConfig: AppConfig)
    extends Submission[AbbreviatedReturnModel]
    with Logging {

  override def submit(
    abbreviatedReturn: AbbreviatedReturnModel
  )(implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[?]): Future[SubmissionResponse] =
    if (appConfig.isHipEnabled) {
      logger.info("[AbbreviatedReturnService][submit] Submitting to HIP")
      connector.submitAbbreviatedReturnHip(abbreviatedReturn).map { resp =>
        logger.info(
          "[AbbreviatedReturnService][submit] Successfully sent a abbreviated return payload to HIP"
        )
        resp
      }
    } else {
      logger.info("[AbbreviatedReturnService][submit] Submitting to DES")
      connector.submitAbbreviatedReturn(abbreviatedReturn).map { resp =>
        logger.info(
          "[AbbreviatedReturnService][submit] Successfully sent a abbreviated return payload to DES"
        )
        resp
      }
    }

}

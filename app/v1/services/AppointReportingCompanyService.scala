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
import uk.gov.hmrc.http.HeaderCarrier
import v1.connectors.AppointReportingCompanyConnector
import v1.connectors.HttpHelper.SubmissionResponse
import v1.models.appointReportingCompany.AppointReportingCompanyModel
import v1.models.requests.IdentifierRequest

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AppointReportingCompanyService @Inject() (
  connector: AppointReportingCompanyConnector,
  appConfig: AppConfig
) extends Submission[AppointReportingCompanyModel]
    with Logging {

  override def submit(
    appointReportingCompany: AppointReportingCompanyModel
  )(implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[?]): Future[SubmissionResponse] =
    if (appConfig.isHipEnabled) {
      logger.info("[AppointReportingCompanyService][submit] Submitting to HIP")
      connector.appointHip(appointReportingCompany).map { resp =>
        logger.info(
          "[AppointReportingCompanyService][submit] Successfully sent a appoint reporting company payload to HIP"
        )
        resp
      }
    } else {
      logger.info("[AppointReportingCompanyService][submit] Submitting to DES")
      connector.appoint(appointReportingCompany).map { resp =>
        logger.info(
          "[AppointReportingCompanyService][submit] Successfully sent a appoint reporting company payload to DES"
        )
        resp
      }
    }

}

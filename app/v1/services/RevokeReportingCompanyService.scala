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

import play.api.Logging

import javax.inject.Inject
import uk.gov.hmrc.http.HeaderCarrier
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.RevokeReportingCompanyConnector
import v1.models.requests.IdentifierRequest
import v1.models.revokeReportingCompany.RevokeReportingCompanyModel

import scala.concurrent.{ExecutionContext, Future}

class RevokeReportingCompanyService @Inject() (revokeReportingCompanyConnector: RevokeReportingCompanyConnector)
    extends Submission[RevokeReportingCompanyModel]
    with Logging {

  override def submit(
    revokeReportingCompany: RevokeReportingCompanyModel
  )(implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[?]): Future[SubmissionResponse] =
    revokeReportingCompanyConnector.revoke(revokeReportingCompany).map { resp =>
      logger.info("[RevokeReportingCompanyService][submit] Successfully sent a revoke reporting company payload")
      resp
    }

}

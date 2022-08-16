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

package v1.connectors.mocks

import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.http.HeaderCarrier
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.RevokeReportingCompanyConnector
import v1.models.requests.IdentifierRequest
import v1.models.revokeReportingCompany.RevokeReportingCompanyModel

import scala.concurrent.{ExecutionContext, Future}

trait MockRevokeReportingCompanyConnector extends MockFactory {

  lazy val mockRevokeReportingCompanyConnector: RevokeReportingCompanyConnector = mock[RevokeReportingCompanyConnector]

  def mockRevokeReportingCompany(model: RevokeReportingCompanyModel)(response: SubmissionResponse): Unit =
    (mockRevokeReportingCompanyConnector
      .revoke(_: RevokeReportingCompanyModel)(_: HeaderCarrier, _: ExecutionContext, _: IdentifierRequest[_]))
      .expects(model, *, *, *)
      .returns(Future.successful(response))
}

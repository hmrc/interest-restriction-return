/*
 * Copyright 2019 HM Revenue & Customs
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

package connectors.mocks

import connectors.RevokeReportingCompanyConnector
import connectors.httpParsers.RevokeReportingCompanyHttpParser.RevokeReportingCompanyResponse
import models.requests.IdentifierRequest
import models.revokeReportingCompany.RevokeReportingCompanyModel
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockRevokeReportingCompanyConnector extends MockFactory {

  lazy val mockRevokeReportingCompanyConnector: RevokeReportingCompanyConnector = mock[RevokeReportingCompanyConnector]

  def mockRevokeReportingCompany(model: RevokeReportingCompanyModel)(response: RevokeReportingCompanyResponse): Unit = {
    (mockRevokeReportingCompanyConnector.revoke(_: RevokeReportingCompanyModel)(_: HeaderCarrier, _: ExecutionContext, _: IdentifierRequest[_]))
      .expects(model, *, *, *)
      .returns(Future.successful(response))
  }
}

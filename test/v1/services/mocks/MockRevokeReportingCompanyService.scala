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

package v1.services.mocks

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.mockito.{ArgumentMatchers, Mockito}
import v1.connectors.HttpHelper.SubmissionResponse
import v1.models.revokeReportingCompany.RevokeReportingCompanyModel
import v1.services.RevokeReportingCompanyService

import scala.concurrent.Future

trait MockRevokeReportingCompanyService {

  lazy val mockRevokeReportingCompanyService: RevokeReportingCompanyService =
    Mockito.mock(classOf[RevokeReportingCompanyService])

  def mockRevokeReportingCompany(model: RevokeReportingCompanyModel)(response: SubmissionResponse): Unit =
    when(mockRevokeReportingCompanyService.submit(ArgumentMatchers.eq(model))(any(), any(), any()))
      .thenReturn(Future.successful(response))
}

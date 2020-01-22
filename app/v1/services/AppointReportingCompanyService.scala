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

package v1.services

import javax.inject.Inject
import uk.gov.hmrc.http.HeaderCarrier
import v1.connectors.AppointReportingCompanyConnector
import v1.connectors.HttpHelper.SubmissionResponse
import v1.models.appointReportingCompany.AppointReportingCompanyModel
import v1.models.requests.IdentifierRequest

import scala.concurrent.{ExecutionContext, Future}

class AppointReportingCompanyService @Inject()(appointReportingCompanyConnector: AppointReportingCompanyConnector)
  extends Submission[AppointReportingCompanyModel]{

  override def submit(appointReportingCompany: AppointReportingCompanyModel)
                     (implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[_]): Future[SubmissionResponse] =
    appointReportingCompanyConnector.appoint(appointReportingCompany)

}

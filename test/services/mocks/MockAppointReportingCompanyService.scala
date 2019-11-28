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

package services.mocks

import connectors.httpParsers.AppointReportingCompanyHttpParser.AppointReportingCompanyResponse
import models.appointReportingCompany.AppointReportingCompanyModel
import org.scalamock.scalatest.MockFactory
import services.AppointReportingCompanyService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockAppointReportingCompanyService extends MockFactory {

  lazy val mockAppointReportingCompanyService: AppointReportingCompanyService = mock[AppointReportingCompanyService]

  def mockAppointReportingCompany(model: AppointReportingCompanyModel)(response: AppointReportingCompanyResponse): Unit = {
    (mockAppointReportingCompanyService.appoint(_: AppointReportingCompanyModel)(_: HeaderCarrier, _: ExecutionContext))
      .expects(model, *, *)
      .returns(Future.successful(response))
  }
}
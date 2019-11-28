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

package services

import assets.appointReportingCompany.AppointReportingCompanyConstants._
import connectors.httpParsers.AppointReportingCompanyHttpParser.{AppointReportingCompanyResponse, SuccessResponse, UnexpectedFailure}
import connectors.mocks.MockAppointReportingCompanyConnector
import play.api.http.Status._
import utils.BaseSpec

class AppointReportingCompanyServiceSpec extends MockAppointReportingCompanyConnector with BaseSpec {

  "AppointReportingCompanyService.appoint" when {

    def setup(response: AppointReportingCompanyResponse): AppointReportingCompanyService = {
      mockAppointReportingCompany(appointReportingCompanyModel)(response)
      new AppointReportingCompanyService(mockAppointReportingCompanyConnector)
    }

    "appointment is successful" should {

      "return a Right(SuccessResponse)" in {

        val service = setup(Right(SuccessResponse("ackRef")))
        val result = service.appoint(appointReportingCompanyModel)

        await(result) shouldBe Right(SuccessResponse("ackRef"))
      }
    }

    "update is unsuccessful" should {

      "return a Left(UnexpectedFailure)" in {

        val service = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result = service.appoint(appointReportingCompanyModel)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }
}
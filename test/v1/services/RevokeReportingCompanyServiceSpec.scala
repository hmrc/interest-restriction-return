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

import data.revokeReportingCompany.RevokeReportingCompanyConstants.*
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.{DesSuccessResponse, UnexpectedFailure}
import v1.connectors.mocks.MockRevokeReportingCompanyConnector
import play.api.http.Status.*
import utils.BaseSpec

import scala.concurrent.Future

class RevokeReportingCompanyServiceSpec extends MockRevokeReportingCompanyConnector with BaseSpec {

  "RevokeReportingCompanyService.submit" when {
    def setup(response: SubmissionResponse): RevokeReportingCompanyService = {
      mockRevokeReportingCompany(revokeReportingCompanyModelMax)(response)
      new RevokeReportingCompanyService(mockRevokeReportingCompanyConnector, appConfig)
    }

    "submission is successful" should {
      "return a Right(SuccessResponse)" in {
        val service: RevokeReportingCompanyService = setup(Right(DesSuccessResponse("ackRef")))
        val result: Future[SubmissionResponse]     = service.submit(revokeReportingCompanyModelMax)

        await(result) shouldBe Right(DesSuccessResponse("ackRef"))
      }
    }

    "submission is unsuccessful" should {
      "return a Left(UnexpectedFailure)" in {
        val service: RevokeReportingCompanyService = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result: Future[SubmissionResponse]     = service.submit(revokeReportingCompanyModelMax)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }
}

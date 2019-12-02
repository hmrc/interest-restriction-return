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

package connectors

import assets.revokeReportingCompany.RevokeReportingCompanyConstants._
import connectors.httpParsers.RevokeReportingCompanyHttpParser.{RevokeReportingCompanyResponse, ErrorResponse, SuccessResponse, UnexpectedFailure}
import connectors.mocks.MockHttpClient
import models.revokeReportingCompany.RevokeReportingCompanyModel
import play.api.http.Status._
import utils.BaseSpec

class RevokeReportingCompanyConnectorSpec extends MockHttpClient with BaseSpec {

  "RevokeReportingCompanyConnector.revoke" when {

    def setup(response: RevokeReportingCompanyResponse): RevokeReportingCompanyConnector = {
      val desUrl = "http://localhost:9262/interest-restriction/reporting-company/revoke"
      mockHttpPost[RevokeReportingCompanyModel, Either[ErrorResponse, SuccessResponse]](desUrl, revokeReportingCompanyModelMax)(response)
      new RevokeReportingCompanyConnector(mockHttpClient, appConfig)
    }

    "revokement is successful" should {

      "return a Right(SuccessResponse)" in {

        val connector = setup(Right(SuccessResponse(ackRef)))
        val result = connector.revoke(revokeReportingCompanyModelMax)

        await(result) shouldBe Right(SuccessResponse(ackRef))
      }
    }

    "update is unsuccessful" should {

      "return a Left(UnexpectedFailure)" in {

        val connector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result = connector.revoke(revokeReportingCompanyModelMax)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }
}

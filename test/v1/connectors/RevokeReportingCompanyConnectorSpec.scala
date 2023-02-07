/*
 * Copyright 2023 HM Revenue & Customs
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

package v1.connectors

import assets.fullReturn.FullReturnConstants.ackRef
import assets.revokeReportingCompany.RevokeReportingCompanyConstants._
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.mocks.MockHttpClient
import play.api.http.Status._
import utils.BaseSpec
import v1.models.revokeReportingCompany.RevokeReportingCompanyModel

import scala.concurrent.Future

class RevokeReportingCompanyConnectorSpec extends MockHttpClient with BaseSpec {

  "RevokeReportingCompanyConnector.revoke" when {
    def setup(response: SubmissionResponse): RevokeReportingCompanyConnector = {
      val desUrl: String = "http://localhost:9262/organisations/interest-restrictions-return/revoke"
      mockHttpPost[RevokeReportingCompanyModel, Either[ErrorResponse, DesSuccessResponse]](
        desUrl,
        revokeReportingCompanyModelMax
      )(response)
      new RevokeReportingCompanyConnector(mockHttpClient, appConfig)
    }

    "revokement is successful" should {
      "return a Right(SuccessResponse)" in {
        val connector: RevokeReportingCompanyConnector = setup(Right(DesSuccessResponse(ackRef)))
        val result: Future[SubmissionResponse]         = connector.revoke(revokeReportingCompanyModelMax)

        await(result) shouldBe Right(DesSuccessResponse(ackRef))
      }
    }

    "revokement is unsuccessful" should {
      "return a Left(UnexpectedFailure)" in {
        val connector: RevokeReportingCompanyConnector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result: Future[SubmissionResponse]         = connector.revoke(revokeReportingCompanyModelMax)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }
}

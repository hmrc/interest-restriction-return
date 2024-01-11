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

import data.abbreviatedReturn.AbbreviatedReturnConstants._
import data.fullReturn.FullReturnConstants.ackRef
import play.api.http.Status._
import utils.BaseSpec
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.mocks.MockHttpClient
import v1.models.abbreviatedReturn.AbbreviatedReturnModel

import scala.concurrent.Future

class AbbreviatedReturnConnectorSpec extends MockHttpClient with BaseSpec {

  "AbbreviatedReturnConnector.submitAbbreviatedReturn" when {
    def setup(response: SubmissionResponse): AbbreviatedReturnConnector = {
      val desUrl: String = "http://localhost:9262/organisations/interest-restrictions-return/abbreviated"
      mockHttpPost[AbbreviatedReturnModel, Either[ErrorResponse, DesSuccessResponse]](
        desUrl,
        abbreviatedReturnUltimateParentModel
      )(response)
      new AbbreviatedReturnConnector(mockHttpClient, appConfig)
    }

    "submission is successful" should {
      "return a Right(SuccessResponse)" in {
        val connector: AbbreviatedReturnConnector = setup(Right(DesSuccessResponse(ackRef)))
        val result: Future[SubmissionResponse]    = connector.submitAbbreviatedReturn(abbreviatedReturnUltimateParentModel)

        await(result) shouldBe Right(DesSuccessResponse(ackRef))
      }
    }

    "submission is unsuccessful" should {
      "return a Left(UnexpectedFailure)" in {
        val connector: AbbreviatedReturnConnector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result: Future[SubmissionResponse]    = connector.submitAbbreviatedReturn(abbreviatedReturnUltimateParentModel)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }
}

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

package connectors

import assets.abbreviatedReturn.AbbreviatedReturnConstants._
import connectors.HttpHelper.SubmissionResponse
import connectors.mocks.MockHttpClient
import models.abbreviatedReturn.AbbreviatedReturnModel
import play.api.http.Status._
import utils.BaseSpec

class AbbreviatedReturnConnectorSpec extends MockHttpClient with BaseSpec {

  "AbbreviatedReturnConnector.submitAbbreviatedReturn" when {

    def setup(response: SubmissionResponse): AbbreviatedReturnConnector = {
      val desUrl = "http://localhost:9262/interest-restriction/return/abbreviated"
      mockHttpPost[AbbreviatedReturnModel, Either[ErrorResponse, SuccessResponse]](desUrl, abbreviatedReturnModelMax)(response)
      new AbbreviatedReturnConnector(mockHttpClient, appConfig)
    }

    "submission is successful" should {

      "return a Right(SuccessResponse)" in {

        val connector = setup(Right(SuccessResponse(ackRef)))
        val result = connector.submitAbbreviatedReturn(abbreviatedReturnModelMax)

        await(result) shouldBe Right(SuccessResponse(ackRef))
      }
    }

    "submission is unsuccessful" should {

      "return a Left(UnexpectedFailure)" in {

        val connector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result = connector.submitAbbreviatedReturn(abbreviatedReturnModelMax)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }
}

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

import assets.fullReturn.FullReturnConstants._
import connectors.httpParsers.FullReturnHttpParser.{ErrorResponse, SuccessResponse, UnexpectedFailure}
import connectors.mocks.MockHttpClient
import models.fullReturn.FullReturnModel
import play.api.http.Status._
import utils.BaseSpec

class FullReturnConnectorSpec extends MockHttpClient with BaseSpec {

  "FullReturnConnector.submit using fullReturnModelMax" when {

    def setup(response: Either[ErrorResponse, SuccessResponse]): FullReturnConnector = {
      val desUrl = "http://localhost:9262/interest-restriction/full-return/submit"
      mockHttpPost[FullReturnModel, Either[ErrorResponse, SuccessResponse]](desUrl, fullReturnModelMax)(response)
      new FullReturnConnector(mockHttpClient, appConfig)
    }

    "submission is successful" should {

      "return a Right(SuccessResponse)" in {

        val connector = setup(Right(SuccessResponse("ackRef")))
        val result = connector.submit(fullReturnModelMax)

        await(result) shouldBe Right(SuccessResponse("ackRef"))
      }
    }

    "update is unsuccessful" should {

      "return a Left(UnexpectedFailure)" in {

        val connector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result = connector.submit(fullReturnModelMax)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }

  "FullReturnConnector.submit using fullReturnModelMin" when {

    def setup(response: Either[ErrorResponse, SuccessResponse]): FullReturnConnector = {
      val desUrl = "http://localhost:9262/interest-restriction/full-return/submit"
      mockHttpPost[FullReturnModel, Either[ErrorResponse, SuccessResponse]](desUrl, fullReturnModelMin)(response)
      new FullReturnConnector(mockHttpClient, appConfig)
    }

    "submission is successful" should {

      "return a Right(SuccessResponse)" in {

        val connector = setup(Right(SuccessResponse("ackRef")))
        val result = connector.submit(fullReturnModelMin)

        await(result) shouldBe Right(SuccessResponse("ackRef"))
      }
    }

    "update is unsuccessful" should {

      "return a Left(UnexpectedFailure)" in {

        val connector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result = connector.submit(fullReturnModelMin)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }
}

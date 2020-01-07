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

package services

import assets.fullReturn.FullReturnConstants._
import connectors.httpParsers.FullReturnHttpParser.{FullReturnResponse, SuccessResponse, UnexpectedFailure}
import connectors.mocks.MockFullReturnConnector
import play.api.http.Status._
import utils.BaseSpec

class FullReturnServiceSpec extends MockFullReturnConnector with BaseSpec {

  "FullReturnService.submit using fullReturnModelMax" when {

    "fullReturnModelMax is used" when {

      def setup(response: FullReturnResponse): FullReturnService = {
        mockFullReturn(fullReturnModelMax)(response)
        new FullReturnService(mockFullReturnConnector)
      }

      "appointment is successful" should {

        "return a Right(SuccessResponse)" in {

          val service = setup(Right(SuccessResponse("ackRef")))
          val result = service.submit(fullReturnModelMax)

          await(result) shouldBe Right(SuccessResponse("ackRef"))
        }
      }

      "update is unsuccessful" should {

        "return a Left(UnexpectedFailure)" in {

          val service = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
          val result = service.submit(fullReturnModelMax)

          await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
        }
      }
    }

    "fullReturnModelMin is used" when {

      def setup(response: FullReturnResponse): FullReturnService = {
        mockFullReturn(fullReturnModelMin)(response)
        new FullReturnService(mockFullReturnConnector)
      }

      "appointment is successful" should {

        "return a Right(SuccessResponse)" in {

          val service = setup(Right(SuccessResponse("ackRef")))
          val result = service.submit(fullReturnModelMin)

          await(result) shouldBe Right(SuccessResponse("ackRef"))
        }
      }

      "update is unsuccessful" should {

        "return a Left(UnexpectedFailure)" in {

          val service = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
          val result = service.submit(fullReturnModelMin)

          await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
        }
      }
    }

  }
}

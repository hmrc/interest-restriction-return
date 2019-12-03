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

package controllers

import assets.fullReturn.FullReturnConstants._
import connectors.httpParsers.FullReturnHttpParser.{SuccessResponse, UnexpectedFailure}
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.{FakeRequest, Helpers}
import services.mocks.MockFullReturnService
import utils.BaseSpec

class FullReturnControllerSpec extends MockFullReturnService with BaseSpec {

  override lazy val fakeRequest = FakeRequest("POST", "/interest-restriction-return/full-return/submit")

  "FullReturnController.submit()" when {

    "the user is authenticated" when {

      object AuthorisedController extends FullReturnController(
        AuthorisedAction,
        mockFullReturnService,
        Helpers.stubControllerComponents()
      )

      "a valid payload is submitted" when {

        lazy val validJsonFakeRequest = fakeRequest
          .withBody(fullReturnJsonMax)
          .withHeaders("Content-Type" -> "application/json")

        "a success response is returned from the service" should {

          "return 200 (OK)" in {

            mockFullReturn(fullReturnModelMax)(Right(SuccessResponse(ackRef)))
            val result = AuthorisedController.submit()(validJsonFakeRequest)
            status(result) shouldBe Status.OK
          }
        }

        "an error response is returned from the service" should {

          "return the Error" in {

            mockFullReturn(fullReturnModelMax)(Left(UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, "err")))
            val result = AuthorisedController.submit()(validJsonFakeRequest)
            status(result) shouldBe Status.INTERNAL_SERVER_ERROR
          }
        }
      }

      "an invalid payload is submitted" when {

        lazy val invalidJsonFakeRequest = fakeRequest
          .withBody(Json.obj())
          .withHeaders("Content-Type" -> "application/json")

        "return a BAD_REQUEST JSON validation error" in {

          val result = AuthorisedController.submit()(invalidJsonFakeRequest)
          status(result) shouldBe Status.BAD_REQUEST
        }
      }
    }

    "the user is unauthenticated" should {

      "return 401 (Unauthorised)" in {

        object UnauthorisedController extends FullReturnController(
          authAction = UnauthorisedAction,
          fullReturnService = mockFullReturnService,
          controllerComponents = Helpers.stubControllerComponents()
        )

        val result = UnauthorisedController.submit()(fakeRequest.withBody(Json.obj()))
        status(result) shouldBe Status.UNAUTHORIZED
      }
    }
  }
}

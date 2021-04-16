/*
 * Copyright 2021 HM Revenue & Customs
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

package v1.controllers

import assets.fullReturn.FullReturnConstants._
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.{FakeRequest, Helpers}
import utils.BaseSpec
import v1.connectors.{DesSuccessResponse, UnexpectedFailure}
import v1.services.mocks.MockFullReturnService

class FullReturnControllerSpec extends MockFullReturnService with BaseSpec {

  override lazy val fakeRequest = FakeRequest("POST", "/interest-restriction-return/return/full")

  "FullReturnController.submit()" when {
    "the user is authenticated" when {

      object AuthorisedController extends FullReturnController(
        authAction = AuthorisedAction,
        fullReturnService = mockFullReturnService,
        auditWrapper = auditWrapper,
        controllerComponents = Helpers.stubControllerComponents()
      )

      "a valid payload is submitted" when {

        lazy val validJsonFakeRequest = fakeRequest
          .withBody(fullReturnUltimateParentJson)
          .withHeaders("Content-Type" -> "application/json")

        "a success response is returned from the companies house service with no v1.validation errors" when {

          "a success response is returned from the service full return service" should {

            "return 200 (OK)" in {
              mockFullReturn(fullReturnUltimateParentModel)(Right(DesSuccessResponse(ackRef)))

              val result = AuthorisedController.submit()(validJsonFakeRequest)
              status(result) shouldBe Status.OK
            }
          }

          "an error response is returned from the service" should {

            "return the Error" in {
              mockFullReturn(fullReturnUltimateParentModel)(Left(UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, "err")))

              val result = AuthorisedController.submit()(validJsonFakeRequest)
              status(result) shouldBe Status.INTERNAL_SERVER_ERROR
            }
          }
        }
      }

      "an invalid payload is submitted" when {

        lazy val invalidJsonFakeRequest = fakeRequest
          .withBody(Json.obj())
          .withHeaders("Content-Type" -> "application/json")

        "return a BAD_REQUEST JSON v1.validation error" in {
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
          auditWrapper = auditWrapper,
          controllerComponents = Helpers.stubControllerComponents()
        )

        val result = UnauthorisedController.submit()(fakeRequest.withBody(Json.obj()))
        status(result) shouldBe Status.UNAUTHORIZED
      }
    }
  }
}

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

import assets.abbreviatedReturn.AbbreviatedReturnConstants._
import v1.connectors.{DesSuccessResponse, UnexpectedFailure}
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.{FakeRequest, Helpers}
import v1.services.mocks.MockAbbreviatedReturnService
import utils.BaseSpec

class AbbreviatedReturnControllerSpec extends MockAbbreviatedReturnService with BaseSpec {

  override lazy val fakeRequest = FakeRequest("POST", "/interest-restriction-return/return/abbreviated")

  "AbbreviatedReturnController.submitAbbreviatedReturn()" when {

    "the user is authenticated" when {

      object AuthorisedController extends AbbreviatedReturnController(
        authAction = AuthorisedAction,
        abbreviatedReturnService = mockAbbreviatedReturnService,
        controllerComponents = Helpers.stubControllerComponents()
      )

      "a valid payload is submitted" when {

        lazy val validJsonFakeRequest = fakeRequest
          .withBody(abbreviatedReturnUltimateParentJson)
          .withHeaders("Content-Type" -> "application/json")

        "a success response is returned from the companies house service with no validation errors" when {

          "a success response is returned from the abbreviated return service" should {

            "return 200 (OK)" in {

              mockAbbreviatedReturn(abbreviatedReturnUltimateParentModel)(Right(DesSuccessResponse(ackRef)))
              val result = AuthorisedController.submitAbbreviatedReturn()(validJsonFakeRequest)
              status(result) shouldBe Status.OK
            }
          }

          "an error response is returned from the service" should {

            "return the Error" in {

              mockAbbreviatedReturn(abbreviatedReturnUltimateParentModel)(Left(UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, "err")))
              val result = AuthorisedController.submitAbbreviatedReturn()(validJsonFakeRequest)
              status(result) shouldBe Status.INTERNAL_SERVER_ERROR
            }
          }
        }
      }

      "an invalid payload is submitted" when {

        lazy val invalidJsonFakeRequest = fakeRequest
          .withBody(Json.obj())
          .withHeaders("Content-Type" -> "application/json")

        "return a BAD_REQUEST JSON validation error" in {

          val result = AuthorisedController.submitAbbreviatedReturn()(invalidJsonFakeRequest)
          status(result) shouldBe Status.BAD_REQUEST
        }
      }
    }

    "the user is unauthenticated" should {

      "return 401 (Unauthorised)" in {

        object UnauthorisedController extends AbbreviatedReturnController(
          authAction = UnauthorisedAction,
          abbreviatedReturnService = mockAbbreviatedReturnService,
          controllerComponents = Helpers.stubControllerComponents()
        )

        val result = UnauthorisedController.submitAbbreviatedReturn()(fakeRequest.withBody(Json.obj()))
        status(result) shouldBe Status.UNAUTHORIZED
      }
    }
  }
}

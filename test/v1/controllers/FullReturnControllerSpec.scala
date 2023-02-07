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

package v1.controllers

import assets.fullReturn.FullReturnConstants._
import play.api.http.Status._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.{FakeRequest, Helpers}
import utils.BaseSpec
import v1.connectors.{DesSuccessResponse, UnexpectedFailure}
import v1.controllers.actions.AuthAction
import v1.services.mocks.MockFullReturnService

import scala.concurrent.Future

class FullReturnControllerSpec extends MockFullReturnService with BaseSpec {

  override lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(
    method = "POST",
    path = "/interest-restriction-return/return/full"
  )

  private val fakeRequestForValidate: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(
    method = "POST",
    path = "/interest-restriction-return/return/full/validate"
  )

  private lazy val validJsonFakeRequest: FakeRequest[JsObject]              = fakeRequest
    .withBody(fullReturnUltimateParentJson)
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private lazy val validJsonFakeRequestForValidate: FakeRequest[JsObject]   = fakeRequestForValidate
    .withBody(fullReturnUltimateParentJson)
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private lazy val invalidJsonFakeRequest: FakeRequest[JsObject]            = fakeRequest
    .withBody(Json.obj())
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private lazy val invalidJsonFakeRequestForValidate: FakeRequest[JsObject] = fakeRequestForValidate
    .withBody(Json.obj())
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private class Test(authAction: AuthAction) {
    val fullReturnController: FullReturnController = new FullReturnController(
      authAction = authAction,
      fullReturnService = mockFullReturnService,
      controllerComponents = Helpers.stubControllerComponents(),
      nrsService = nrsService,
      appConfig = appConfig
    )
  }

  "FullReturnController" when {
    ".submit" when {
      "the user is authenticated" when {
        "a valid payload is submitted" when {
          "a success response is returned from the companies house service with no validation errors" when {
            "a success response is returned from the service full return service" should {
              "return 200 (OK)" in new Test(AuthorisedAction) {
                mockFullReturn(fullReturnUltimateParentModel)(Right(DesSuccessResponse(ackRef)))

                val result: Future[Result] = fullReturnController.submit()(validJsonFakeRequest)
                status(result) shouldBe OK
              }
            }

            "an error response is returned from the service" should {
              "return the Error" in new Test(AuthorisedAction) {
                mockFullReturn(fullReturnUltimateParentModel)(
                  Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "err"))
                )

                val result: Future[Result] = fullReturnController.submit()(validJsonFakeRequest)
                status(result) shouldBe INTERNAL_SERVER_ERROR
              }
            }
          }
        }

        "an invalid payload is submitted" should {
          "return a BAD_REQUEST JSON validation error" in new Test(AuthorisedAction) {
            val result: Future[Result] = fullReturnController.submit()(invalidJsonFakeRequest)
            status(result) shouldBe BAD_REQUEST
          }
        }
      }

      "the user is unauthenticated" should {
        "return 401 (Unauthorised)" in new Test(UnauthorisedAction) {
          val result: Future[Result] = fullReturnController.submit()(fakeRequest.withBody(Json.obj()))
          status(result) shouldBe UNAUTHORIZED
        }
      }
    }

    ".validate" when {
      "the user is authenticated" when {
        "a valid payload is submitted" should {
          "return 204 (NO CONTENT)" in new Test(AuthorisedAction) {
            val result: Future[Result] = fullReturnController.validate()(validJsonFakeRequestForValidate)
            status(result) shouldBe NO_CONTENT
          }
        }

        "an invalid payload is submitted" should {
          "return a BAD_REQUEST JSON validation error" in new Test(AuthorisedAction) {
            val result: Future[Result] = fullReturnController.validate()(invalidJsonFakeRequestForValidate)
            status(result) shouldBe BAD_REQUEST
          }
        }
      }

      "the user is unauthenticated" should {
        "return 401 (Unauthorised)" in new Test(UnauthorisedAction) {
          val result: Future[Result] = fullReturnController.validate()(fakeRequest.withBody(Json.obj()))
          status(result) shouldBe UNAUTHORIZED
        }
      }
    }
  }
}

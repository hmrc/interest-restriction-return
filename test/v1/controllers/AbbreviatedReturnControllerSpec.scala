/*
 * Copyright 2025 HM Revenue & Customs
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

import data.abbreviatedReturn.AbbreviatedReturnConstants.*
import play.api.http.Status.*
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.{FakeRequest, Helpers}
import utils.BaseSpec
import v1.connectors.{DesSuccessResponse, UnexpectedFailure}
import v1.controllers.actions.AuthAction
import v1.services.mocks.MockAbbreviatedReturnService

import scala.concurrent.Future

class AbbreviatedReturnControllerSpec extends MockAbbreviatedReturnService with BaseSpec {

  override lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(
    method = "POST",
    path = "/interest-restriction-return/return/abbreviated"
  )

  private val fakeRequestForValidate: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(
    method = "POST",
    path = "/interest-restriction-return/return/abbreviated/validate"
  )

  private lazy val validJsonFakeRequest: FakeRequest[JsObject] = fakeRequest
    .withBody(abbreviatedReturnUltimateParentJson)
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private lazy val validJsonFakeRequestForValidate: FakeRequest[JsObject] = fakeRequestForValidate
    .withBody(abbreviatedReturnUltimateParentJson)
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private lazy val invalidJsonFakeRequest: FakeRequest[JsObject] = fakeRequest
    .withBody(Json.obj())
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private lazy val invalidJsonFakeRequestForValidate: FakeRequest[JsObject] = fakeRequestForValidate
    .withBody(Json.obj())
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private class Test(authAction: AuthAction) {
    val abbreviatedReturnController: AbbreviatedReturnController = new AbbreviatedReturnController(
      authAction = authAction,
      abbreviatedReturnService = mockAbbreviatedReturnService,
      controllerComponents = Helpers.stubControllerComponents(),
      nrsService = nrsService,
      appConfig = appConfig
    )
  }

  "AbbreviatedReturnController" when {
    ".submitAbbreviatedReturn" when {
      "the user is authenticated" when {
        "a valid payload is submitted" when {
          "a success response is returned from the companies house service with no validation errors" when {
            "a success response is returned from the abbreviated return service" should {
              "return 200 (OK)" in new Test(AuthorisedAction) {
                mockAbbreviatedReturn(abbreviatedReturnUltimateParentModel)(Right(DesSuccessResponse(ackRef)))

                val result: Future[Result] = abbreviatedReturnController.submitAbbreviatedReturn()(validJsonFakeRequest)
                status(result) shouldBe OK
              }
            }

            "an error response is returned from the service" should {
              "return the Error" in new Test(AuthorisedAction) {
                mockAbbreviatedReturn(abbreviatedReturnUltimateParentModel)(
                  Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "err"))
                )

                val result: Future[Result] = abbreviatedReturnController.submitAbbreviatedReturn()(validJsonFakeRequest)
                status(result) shouldBe INTERNAL_SERVER_ERROR
              }
            }
          }
        }

        "an invalid payload is submitted" should {
          "return a BAD_REQUEST JSON validation error" in new Test(AuthorisedAction) {
            val result: Future[Result] = abbreviatedReturnController.submitAbbreviatedReturn()(invalidJsonFakeRequest)
            status(result) shouldBe BAD_REQUEST
          }
        }
      }

      "the user is unauthenticated" should {
        "return 401 (Unauthorised)" in new Test(UnauthorisedAction) {
          val result: Future[Result] =
            abbreviatedReturnController.submitAbbreviatedReturn()(fakeRequest.withBody(Json.obj()))
          status(result) shouldBe UNAUTHORIZED
        }
      }
    }

    ".validate" when {
      "the user is authenticated" when {
        "a valid payload is submitted" should {
          "return 204 (NO_CONTENT)" in new Test(AuthorisedAction) {
            val result: Future[Result] = abbreviatedReturnController.validate()(validJsonFakeRequestForValidate)
            status(result) shouldBe NO_CONTENT
          }
        }

        "an invalid payload is submitted" should {
          "return a BAD_REQUEST JSON validation error" in new Test(AuthorisedAction) {
            val result: Future[Result] = abbreviatedReturnController.validate()(invalidJsonFakeRequestForValidate)
            status(result) shouldBe BAD_REQUEST
          }
        }
      }

      "the user is unauthenticated" should {
        "return 401 (Unauthorised)" in new Test(UnauthorisedAction) {
          val result: Future[Result] =
            abbreviatedReturnController.submitAbbreviatedReturn()(fakeRequest.withBody(Json.obj()))
          status(result) shouldBe UNAUTHORIZED
        }
      }
    }
  }
}

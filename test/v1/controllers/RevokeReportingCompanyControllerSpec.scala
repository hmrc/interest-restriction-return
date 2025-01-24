/*
 * Copyright 2024 HM Revenue & Customs
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

import data.revokeReportingCompany.RevokeReportingCompanyConstants._
import v1.connectors.{DesSuccessResponse, UnexpectedFailure}
import play.api.http.Status._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.{FakeRequest, Helpers}
import v1.services.mocks.MockRevokeReportingCompanyService
import utils.BaseSpec
import v1.controllers.actions.AuthAction

import scala.concurrent.Future

class RevokeReportingCompanyControllerSpec extends MockRevokeReportingCompanyService with BaseSpec {

  override lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(
    method = "POST",
    path = "/interest-restriction-return/reporting-company/revoke"
  )

  private val fakeRequestForValidate: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(
    method = "POST",
    path = "/interest-restriction-return/reporting-company/revoke/validate"
  )

  private lazy val validJsonFakeRequest: FakeRequest[JsObject] = fakeRequest
    .withBody(revokeReportingCompanyJsonMax)
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private lazy val validJsonFakeRequestForValidate: FakeRequest[JsObject] = fakeRequestForValidate
    .withBody(revokeReportingCompanyJsonMax)
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private lazy val invalidJsonFakeRequest: FakeRequest[JsObject] = fakeRequest
    .withBody(Json.obj())
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private lazy val invalidJsonFakeRequestForValidate: FakeRequest[JsObject] = fakeRequestForValidate
    .withBody(Json.obj())
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private class Test(authAction: AuthAction) {
    val revokeReportingCompanyController: RevokeReportingCompanyController = new RevokeReportingCompanyController(
      authAction = authAction,
      revokeReportingCompanyService = mockRevokeReportingCompanyService,
      controllerComponents = Helpers.stubControllerComponents(),
      appConfig = appConfig
    )
  }

  "RevokeReportingCompanyController" when {
    ".revoke" when {
      "the user is authenticated" when {
        "a valid payload is submitted" when {
          "a success response is returned from the companies house service with no validation errors" when {
            "a success response is returned from the revoke reporting company service" should {
              "return 200 (OK)" in new Test(AuthorisedAction) {
                mockRevokeReportingCompany(revokeReportingCompanyModelMax)(Right(DesSuccessResponse(ackRef)))

                val result: Future[Result] = revokeReportingCompanyController.revoke()(validJsonFakeRequest)
                status(result) shouldBe OK
              }
            }

            "an error response is returned from the revoke reporting company service" should {
              "return the Error" in new Test(AuthorisedAction) {
                mockRevokeReportingCompany(revokeReportingCompanyModelMax)(
                  Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "err"))
                )

                val result: Future[Result] = revokeReportingCompanyController.revoke()(validJsonFakeRequest)
                status(result) shouldBe INTERNAL_SERVER_ERROR
              }
            }
          }
        }

        "an invalid payload is submitted" should {
          "return a BAD_REQUEST JSON validation error" in new Test(AuthorisedAction) {
            val result: Future[Result] = revokeReportingCompanyController.revoke()(invalidJsonFakeRequest)
            status(result) shouldBe BAD_REQUEST
          }
        }
      }

      "the user is unauthenticated" should {
        "return 401 (Unauthorised)" in new Test(UnauthorisedAction) {
          val result: Future[Result] = revokeReportingCompanyController.revoke()(fakeRequest.withBody(Json.obj()))
          status(result) shouldBe UNAUTHORIZED
        }
      }
    }

    ".validate" when {
      "the user is authenticated" when {
        "a valid payload is submitted" should {
          "return 204 (NO CONTENT)" in new Test(AuthorisedAction) {
            val result: Future[Result] = revokeReportingCompanyController.validate()(validJsonFakeRequestForValidate)
            status(result) shouldBe NO_CONTENT
          }
        }

        "an invalid payload is submitted" should {
          "return a BAD_REQUEST JSON validation error" in new Test(AuthorisedAction) {
            val result: Future[Result] = revokeReportingCompanyController.validate()(invalidJsonFakeRequestForValidate)
            status(result) shouldBe BAD_REQUEST
          }
        }
      }

      "the user is unauthenticated" should {
        "return 401 (Unauthorised)" in new Test(UnauthorisedAction) {
          val result: Future[Result] = revokeReportingCompanyController.validate()(fakeRequest.withBody(Json.obj()))
          status(result) shouldBe UNAUTHORIZED
        }
      }
    }
  }
}

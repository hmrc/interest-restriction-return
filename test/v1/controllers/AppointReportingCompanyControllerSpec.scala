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

import data.appointReportingCompany.AppointReportingCompanyConstants._
import v1.connectors.{DesSuccessResponse, UnexpectedFailure}
import play.api.http.Status._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.{FakeRequest, Helpers}
import v1.services.mocks.MockAppointReportingCompanyService
import utils.BaseSpec
import v1.controllers.actions.AuthAction

import scala.concurrent.Future

class AppointReportingCompanyControllerSpec extends MockAppointReportingCompanyService with BaseSpec {

  override lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(
    method = "POST",
    path = "/interest-restriction-return/reporting-company/appoint"
  )

  private val fakeRequestForValidate: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(
    method = "POST",
    path = "/interest-restriction-return/reporting-company/appoint/validate"
  )

  private lazy val validJsonFakeRequest: FakeRequest[JsObject] = fakeRequest
    .withBody(appointReportingCompanyJsonMax)
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private lazy val validJsonFakeRequestForValidate: FakeRequest[JsObject] = fakeRequestForValidate
    .withBody(appointReportingCompanyJsonMax)
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private lazy val invalidJsonFakeRequest: FakeRequest[JsObject] = fakeRequest
    .withBody(Json.obj())
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private lazy val invalidJsonFakeRequestForValidate: FakeRequest[JsObject] = fakeRequestForValidate
    .withBody(Json.obj())
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

  private class Test(authAction: AuthAction) {
    val appointReportingCompanyController: AppointReportingCompanyController = new AppointReportingCompanyController(
      authAction = authAction,
      appointReportingCompanyService = mockAppointReportingCompanyService,
      controllerComponents = Helpers.stubControllerComponents(),
      appConfig = appConfig
    )
  }

  "AppointReportingCompanyController" when {
    ".appoint" when {
      "the user is authenticated" when {
        "a valid payload is submitted" when {
          "a success response is returned from the companies house service with no validation errors" when {
            "a success response is returned from the appoint reporting company service" should {
              "return 200 (OK)" in new Test(AuthorisedAction) {
                mockAppointReportingCompany(appointReportingCompanyModelMax)(Right(DesSuccessResponse(ackRef)))

                val result: Future[Result] = appointReportingCompanyController.appoint()(validJsonFakeRequest)
                status(result) shouldBe OK
              }
            }

            "an error response is returned from the appoint reporting company service" should {
              "return the Error" in new Test(AuthorisedAction) {
                mockAppointReportingCompany(appointReportingCompanyModelMax)(
                  Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "err"))
                )

                val result: Future[Result] = appointReportingCompanyController.appoint()(validJsonFakeRequest)
                status(result) shouldBe INTERNAL_SERVER_ERROR
              }
            }
          }
        }

        "an invalid payload is submitted" should {
          "return a BAD_REQUEST JSON validation error" in new Test(AuthorisedAction) {
            val result: Future[Result] = appointReportingCompanyController.appoint()(invalidJsonFakeRequest)
            status(result) shouldBe BAD_REQUEST
          }
        }
      }

      "the user is unauthenticated" should {
        "return 401 (Unauthorised)" in new Test(UnauthorisedAction) {
          val result: Future[Result] = appointReportingCompanyController.appoint()(fakeRequest.withBody(Json.obj()))
          status(result) shouldBe UNAUTHORIZED
        }
      }
    }

    ".validate" when {
      "the user is authenticated" when {
        "a valid payload is submitted" should {
          "return 204 (NO CONTENT)" in new Test(AuthorisedAction) {
            val result: Future[Result] = appointReportingCompanyController.validate()(validJsonFakeRequestForValidate)
            status(result) shouldBe NO_CONTENT
          }
        }

        "an invalid payload is submitted" should {
          "return a BAD_REQUEST JSON validation error" in new Test(AuthorisedAction) {
            val result: Future[Result] = appointReportingCompanyController.validate()(invalidJsonFakeRequestForValidate)
            status(result) shouldBe BAD_REQUEST
          }
        }
      }

      "the user is unauthenticated" should {
        "return 401 (Unauthorised)" in new Test(UnauthorisedAction) {
          val result: Future[Result] = appointReportingCompanyController.validate()(fakeRequest.withBody(Json.obj()))
          status(result) shouldBe UNAUTHORIZED
        }
      }
    }
  }
}

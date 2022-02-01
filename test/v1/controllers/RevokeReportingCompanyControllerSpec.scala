/*
 * Copyright 2022 HM Revenue & Customs
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

import assets.revokeReportingCompany.RevokeReportingCompanyConstants._
import v1.connectors.{DesSuccessResponse, UnexpectedFailure}
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.{FakeRequest, Helpers}
import v1.services.mocks.MockRevokeReportingCompanyService
import utils.BaseSpec

class RevokeReportingCompanyControllerSpec extends MockRevokeReportingCompanyService with BaseSpec {

  override lazy val fakeRequest = FakeRequest("POST", "/interest-restriction-return/reporting-company/revoke")

  "RevokeReportingCompanyController.revoke()" when {

    "the user is authenticated" when {

      object AuthorisedController extends RevokeReportingCompanyController(
        authAction = AuthorisedAction,
        revokeReportingCompanyService = mockRevokeReportingCompanyService,
        controllerComponents = Helpers.stubControllerComponents(),
        appConfig = appConfig
      )

      "a valid payload is submitted" when {

        lazy val validJsonFakeRequest = fakeRequest
          .withBody(revokeReportingCompanyJsonMax)
          .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

        "a success response is returned from the companies house service with no validation errors" when {

          "a success response is returned from the revoke reporting company service" should {

            "return 200 (OK)" in {
              mockRevokeReportingCompany(revokeReportingCompanyModelMax)(Right(DesSuccessResponse(ackRef)))

              val result = AuthorisedController.revoke()(validJsonFakeRequest)
              status(result) shouldBe Status.OK
            }
          }

          "an error response is returned from the revoke reporting company service" should {

            "return the Error" in {
              mockRevokeReportingCompany(revokeReportingCompanyModelMax)(Left(
                UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, "err"))
              )

              val result = AuthorisedController.revoke()(validJsonFakeRequest)
              status(result) shouldBe Status.INTERNAL_SERVER_ERROR
            }
          }
        }
      }

      "an invalid payload is submitted" when {

        lazy val invalidJsonFakeRequest = fakeRequest
          .withBody(Json.obj())
          .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

        "return a BAD_REQUEST JSON validation error" in {
          val result = AuthorisedController.revoke()(invalidJsonFakeRequest)
          status(result) shouldBe Status.BAD_REQUEST
        }
      }
    }

    "the user is unauthenticated" should {

      "return 401 (Unauthorised)" in {

        object UnauthorisedController extends RevokeReportingCompanyController(
          authAction = UnauthorisedAction,
          revokeReportingCompanyService = mockRevokeReportingCompanyService,
          controllerComponents = Helpers.stubControllerComponents(),
          appConfig = appConfig
        )

        val result = UnauthorisedController.revoke()(fakeRequest.withBody(Json.obj()))
        status(result) shouldBe Status.UNAUTHORIZED
      }
    }
  }


  "RevokeReportingCompanyController.validate()" when {
    val validateFakeRequest = FakeRequest("POST", "/interest-restriction-return/reporting-company/appoint/validate")
    "the user is authenticated" when {

      object AuthorisedController extends RevokeReportingCompanyController(
        authAction = AuthorisedAction,
        revokeReportingCompanyService = mockRevokeReportingCompanyService,
        controllerComponents = Helpers.stubControllerComponents(),
        appConfig = appConfig
      )

      "a valid payload is submitted" when {

        lazy val validJsonFakeRequest = validateFakeRequest
          .withBody(revokeReportingCompanyJsonMax)
          .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

        "return 204 (NO CONTENT)" in {
          val result = AuthorisedController.validate()(validJsonFakeRequest)
          status(result) shouldBe Status.NO_CONTENT
        }
      }

      "an invalid payload is submitted" when {

        lazy val invalidJsonFakeRequest = validateFakeRequest
          .withBody(Json.obj())
          .withHeaders("Content-Type" -> "application/json", "Authorization" -> "test")

        "return a BAD_REQUEST JSON v1.validation error" in {
          val result = AuthorisedController.validate()(invalidJsonFakeRequest)
          status(result) shouldBe Status.BAD_REQUEST
        }
      }
    }

    "the user is unauthenticated" should {

      "return 401 (Unauthorised)" in {

        object UnauthorisedController extends RevokeReportingCompanyController(
          authAction = UnauthorisedAction,
          revokeReportingCompanyService = mockRevokeReportingCompanyService,
          controllerComponents = Helpers.stubControllerComponents(),
          appConfig = appConfig
        )

        val result = UnauthorisedController.validate()(fakeRequest.withBody(Json.obj()))
        status(result) shouldBe Status.UNAUTHORIZED
      }
    }
  }
}

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

package controllers

import data.AppointReportingCompanyITConstants._
import play.api.http.Status._
import stubs.{AuthStub, DESStub}
import utils.IntegrationSpecBase

class AppointReportingCompanyControllerISpec extends IntegrationSpecBase {

  "POST /reporting-company/appoint" when {
    "user is authenticated" when {
      "request is successfully processed by DES" should {
        "return OK (200) with the correct body" in {
          AuthStub.authorised()
          DESStub.appointReportingCompanySuccess(appointReportingCompanyDesSuccessJson)

          val result = postRequest("/reporting-company/appoint", appointReportingCompanyJson)

          result should have(
            httpStatus(OK),
            jsonBodyAs(appointReportingCompanyDesSuccessJson)
          )
        }
      }

      "error is returned from DES" should {
        "return the error" in {
          AuthStub.authorised()
          DESStub.appointReportingCompanyError

          val result = postRequest("/reporting-company/appoint", appointReportingCompanyJson)

          result should have(
            httpStatus(INTERNAL_SERVER_ERROR)
          )
        }
      }
    }

    "user is unauthenticated" should {
      "return UNAUTHORISED (401)" in {
        AuthStub.unauthorised()

        val result = postRequest("/reporting-company/appoint", appointReportingCompanyJson)

        result should have(
          httpStatus(UNAUTHORIZED)
        )
      }
    }
  }

  "POST /reporting-company/appoint/validate" when {
    "user is authenticated" should {
      "return NO_CONTENT (204)" in {
        AuthStub.authorised()

        val result = postRequest("/reporting-company/appoint/validate", appointReportingCompanyJson)

        result should have(
          httpStatus(NO_CONTENT)
        )
        verifyNoCall("/submission")

      }
    }

    "user is unauthenticated" should {
      "return UNAUTHORISED (401)" in {
        AuthStub.unauthorised()

        val result = postRequest("/reporting-company/appoint/validate", appointReportingCompanyJson)

        result should have(
          httpStatus(UNAUTHORIZED)
        )
      }
    }
  }
}

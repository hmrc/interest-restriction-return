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

import data.RevokeReportingCompanyITConstants.*
import play.api.http.Status.*
import stubs.{AuthStub, HIPStub}
import utils.IntegrationSpecBase

class RevokeReportingCompanyControllerISpec extends IntegrationSpecBase {

  "POST /reporting-company/revoke" when {
    "user is authenticated" when {
      "request is successfully processed by HIP" should {
        "return OK (200) with the correct body" in {
          AuthStub.authorised()
          HIPStub.revokeReportingCompanySuccess(revokeReportingCompanySuccessJson)

          val result = postRequest("/reporting-company/revoke", revokeReportingCompanyJson)

          result should have(
            httpStatus(OK),
            jsonBodyAs(revokeReportingCompanySuccessJson)
          )
        }
      }

      "error is returned from HIP" should {
        "return the error" in {
          AuthStub.authorised()
          HIPStub.revokeReportingCompanyError

          val result = postRequest("/reporting-company/revoke", revokeReportingCompanyJson)

          result should have(
            httpStatus(INTERNAL_SERVER_ERROR)
          )
        }
      }
    }

    "user is unauthenticated" should {
      "return UNAUTHORISED (401)" in {
        AuthStub.unauthorised()

        val result = postRequest("/reporting-company/revoke", revokeReportingCompanyJson)

        result should have(
          httpStatus(UNAUTHORIZED)
        )
      }
    }
  }

  "POST /reporting-company/revoke/validate" when {
    "user is authenticated" should {
      "return NO_CONTENT (204)" in {
        AuthStub.authorised()

        val result = postRequest("/reporting-company/revoke/validate", revokeReportingCompanyJson)

        verifyNoCall("/submission")
        result should have(
          httpStatus(NO_CONTENT)
        )
      }
    }

    "user is unauthenticated" should {
      "return UNAUTHORISED (401)" in {
        AuthStub.unauthorised()

        val result = postRequest("/reporting-company/revoke/validate", revokeReportingCompanyJson)

        result should have(
          httpStatus(UNAUTHORIZED)
        )
      }
    }
  }
}

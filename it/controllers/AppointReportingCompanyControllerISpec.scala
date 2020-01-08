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
import assets.AppointReportingCompanyITConstants._
import play.api.http.Status._
import stubs.{AuthStub, CompaniesHouseStub, DESStub}
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}


class AppointReportingCompanyControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers {

  "POST /reporting-company/appoint" when {

    "user is authenticated" when {

      "companies house validates the CRN" should {

        "request is successfully processed by DES" should {

          "should return OK (200) with the correct body" in {

            AuthStub.authorised()
            CompaniesHouseStub.checkCrn(OK)
            DESStub.appointReportingCompanySuccess(appointReportingCompanyDesSuccessJson)

            val res = postRequest("/reporting-company/appoint", appointReportingCompanyJson)

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                jsonBodyAs(appointReportingCompanyDesSuccessJson)
              )
            }
          }
        }

        "error is returned from DES" should {

          "should return the error" in {

            AuthStub.authorised()
            CompaniesHouseStub.checkCrn(OK)
            DESStub.appointReportingCompanyError

            val res = postRequest("/reporting-company/appoint", appointReportingCompanyJson)

            whenReady(res) { result =>
              result should have(
                httpStatus(INTERNAL_SERVER_ERROR)
              )
            }
          }
        }
      }

      "companies house returns invalid for the CRN" should {

        "should return BAD_REQUEST (400) with the correct body" in {

          AuthStub.authorised()
          CompaniesHouseStub.checkCrn(BAD_REQUEST)

          val res = postRequest("/reporting-company/appoint", appointReportingCompanyJson)

          whenReady(res) { result =>
            result should have(
              httpStatus(BAD_REQUEST)
            )
          }
        }
      }

      "request is successfully processed by Companies House" should {

        "should return INTERNAL_SERVER_ERROR (500) with the correct body" in {

          AuthStub.authorised()
          CompaniesHouseStub.checkCrn(INTERNAL_SERVER_ERROR)

          val res = postRequest("/reporting-company/appoint", appointReportingCompanyJson)

          whenReady(res) { result =>
            result should have(
              httpStatus(INTERNAL_SERVER_ERROR)
            )
          }
        }
      }
    }

    "user is unauthenticated" when {

      "should return UNAUTHORISED (401)" in {

        AuthStub.unauthorised()

        val res = postRequest("/reporting-company/appoint", appointReportingCompanyJson)

        whenReady(res) { result =>
          result should have(
            httpStatus(UNAUTHORIZED)
          )
        }
      }
    }
  }
}

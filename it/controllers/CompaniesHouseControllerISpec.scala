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

import assets.BaseITConstants
import play.api.http.Status._
import stubs.{AuthStub, CompaniesHouseStub}
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}


class CompaniesHouseControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "POST /validate-crn" when {

    "user is authenticated" when {

      "companies house validates the CRN" should {

        "return 204 NO_CONTENT" in {

          AuthStub.authorised()
          CompaniesHouseStub.checkCrn(OK)

          val res = getRequest(s"/validate-crn/$crn")

          whenReady(res) { result =>
            result should have(
              httpStatus(NO_CONTENT)
            )
          }
        }
      }

      "error is returned from companies house" should {

        "return a 500 INTERNAL_SERVER_ERROR" in {

          AuthStub.authorised()
          CompaniesHouseStub.checkCrn(INTERNAL_SERVER_ERROR)

          val res = getRequest(s"/validate-crn/$crn")

          whenReady(res) { result =>
            result should have(
              httpStatus(INTERNAL_SERVER_ERROR)
            )
          }
        }
      }

      "unexpected response" should {

        "return a 500 INTERNAL_SERVER_ERROR" in {

          AuthStub.authorised()
          CompaniesHouseStub.checkCrn(IM_A_TEAPOT)

          val res = getRequest(s"/validate-crn/$crn")

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

        val res = getRequest(s"/validate-crn/$crn")

        whenReady(res) { result =>
          result should have(
            httpStatus(UNAUTHORIZED)
          )
        }
      }
    }
  }
}

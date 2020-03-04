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

import assets.AbbreviatedReturnITConstants._
import play.api.http.Status._
import stubs.{AuthStub, CompaniesHouseStub, DESStub}
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}


class AbbreviatedReturnControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers {

  "POST /return/abbreviated" when {

    "user is authenticated" when {

      "request is successfully processed by DES" should {

        "return OK (200) with the correct body" in {

          AuthStub.authorised()
          DESStub.abbreviatedReturnSuccess(abbreviatedReturnDesSuccessJson)

          val res = postRequest("/return/abbreviated", abbreviatedReturnJson)

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              jsonBodyAs(abbreviatedReturnDesSuccessJson)
            )
          }
        }
      }

      "error is returned from DES" should {

        "should return the error" in {

          AuthStub.authorised()
          DESStub.abbreviatedReturnError

          val res = postRequest("/return/abbreviated", abbreviatedReturnJson)

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

        val res = postRequest("/return/abbreviated", abbreviatedReturnJson)

        whenReady(res) { result =>
          result should have(
            httpStatus(UNAUTHORIZED)
          )
        }
      }
    }
  }
}

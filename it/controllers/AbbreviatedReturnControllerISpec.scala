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

package controllers

import assets.AbbreviatedReturnITConstants._
import assets.IntegrationNrsConstants._
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import stubs.{AuthStub, DESStub, NRSStub}
import utils.IntegrationSpecBase

import scala.concurrent.Future

class AbbreviatedReturnControllerISpec extends IntegrationSpecBase {

  "POST /return/abbreviated" when {
    "user is authenticated" when {
      "request is successfully processed by DES" when {
        "nrs is successful" should {
          "return OK (200) with the correct body" in {
            AuthStub.authorised()
            DESStub.abbreviatedReturnSuccess(abbreviatedReturnDesSuccessJson)
            NRSStub.success(responsePayload)

            val res: Future[WSResponse] = postRequest("/return/abbreviated", abbreviatedReturnJson)

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                jsonBodyAs(abbreviatedReturnDesSuccessJson)
              )
            }
          }
        }

        "nrs errors" should {
          "return OK (200) with the correct body" in {
            AuthStub.authorised()
            DESStub.abbreviatedReturnSuccess(abbreviatedReturnDesSuccessJson)
            NRSStub.error

            val res: Future[WSResponse] = postRequest("/return/abbreviated", abbreviatedReturnJson)

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                jsonBodyAs(abbreviatedReturnDesSuccessJson)
              )
            }
          }
        }
      }

      "error is returned from DES" should {
        "return the error" in {
          AuthStub.authorised()
          DESStub.abbreviatedReturnError

          val res: Future[WSResponse] = postRequest("/return/abbreviated", abbreviatedReturnJson)

          whenReady(res) { result =>
            result should have(
              httpStatus(INTERNAL_SERVER_ERROR)
            )
          }
        }
      }
    }

    "user is unauthenticated" should {
      "return UNAUTHORISED (401)" in {
        AuthStub.unauthorised()

        val res: Future[WSResponse] = postRequest("/return/abbreviated", abbreviatedReturnJson)

        whenReady(res) { result =>
          result should have(
            httpStatus(UNAUTHORIZED)
          )
        }
      }
    }
  }

  "POST /return/abbreviated/validate" when {
    "user is authenticated" should {
      "return NO_CONTENT (204)" in {
        AuthStub.authorised()
        DESStub.abbreviatedReturnSuccess(abbreviatedReturnDesSuccessJson)
        NRSStub.success(responsePayload)

        val res: Future[WSResponse] = postRequest("/return/abbreviated/validate", abbreviatedReturnJson)

        whenReady(res) { result =>
          verifyNoCall("/submission")
          result should have(
            httpStatus(NO_CONTENT)
          )
        }
      }
    }

    "user is unauthenticated" should {
      "return UNAUTHORISED (401)" in {
        AuthStub.unauthorised()

        val res: Future[WSResponse] = postRequest("/return/abbreviated/validate", abbreviatedReturnJson)

        whenReady(res) { result =>
          result should have(
            httpStatus(UNAUTHORIZED)
          )
        }
      }
    }
  }
}

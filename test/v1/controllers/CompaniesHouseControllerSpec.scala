/*
 * Copyright 2020 HM Revenue & Customs
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

import v1.connectors.{InvalidCRN, UnexpectedFailure, ValidCRN}
import v1.connectors.mocks.MockCompaniesHouseConnector
import play.api.http.Status
import play.api.test.{FakeRequest, Helpers}
import v1.services.mocks.MockFullReturnService
import utils.BaseSpec

class CompaniesHouseControllerSpec extends MockFullReturnService with MockCompaniesHouseConnector with BaseSpec {

  override lazy val fakeRequest = FakeRequest("POST", "/interest-restriction-return/validate-crn")
  
  "CompaniesHouseController.validateCRN(crn.crn)(fakeRequest)" when {

    "the user is authenticated" when {

      object CompaniesHouseAuthorised extends CompaniesHouseController(
        authAction = AuthorisedAction,
        companiesHouseConnector = mockCompaniesHouseConnector,
        controllerComponents = Helpers.stubControllerComponents()
      )

      "a valid crn is submitted" when {

        "a success response is returned from the companies house service with no errors" when {

          "a success response is returned from the companies house connector" should {

            "return 200 (OK)" in {
              
              mockValidateCRN(crn)(Right(ValidCRN))

              val result = CompaniesHouseAuthorised.validateCRN(crn.crn)(fakeRequest)
              status(result) shouldBe Status.NO_CONTENT
            }
          }

          "an invalid response is returned from the companies house connector" should {

            "return 400 bad request" in {
              
              mockValidateCRN(crn)(Left(InvalidCRN))

              val result = CompaniesHouseAuthorised.validateCRN(crn.crn)(fakeRequest)
              status(result) shouldBe Status.BAD_REQUEST
            }
          }

          "a server error response is returned from the companies house connector" should {

            "return 500 server error" in {

              mockValidateCRN(crn)(Left(UnexpectedFailure(409,"bad")))

              val result = CompaniesHouseAuthorised.validateCRN(crn.crn)(fakeRequest)
              status(result) shouldBe Status.INTERNAL_SERVER_ERROR
            }
          }
        }
      }

    }

    "the user is unauthenticated" should {

      "return 401 (Unauthorised)" in {

        object CompaniesHouseUnauthorised extends CompaniesHouseController(
          authAction = AuthorisedAction,
          companiesHouseConnector = mockCompaniesHouseConnector,
          controllerComponents = Helpers.stubControllerComponents()
        )

        val result = CompaniesHouseUnauthorised.validateCRN(crn.crn)(fakeRequest)
        status(result) shouldBe Status.UNAUTHORIZED
      }
    }
  }
}

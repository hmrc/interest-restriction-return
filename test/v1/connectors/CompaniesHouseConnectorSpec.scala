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

package v1.connectors

import v1.connectors.HttpHelper.CompaniesHouseResponse
import v1.connectors.mocks.MockHttpClient
import play.api.http.Status._
import utils.BaseSpec

class CompaniesHouseConnectorSpec extends MockHttpClient with BaseSpec {

  "CompaniesHouseConnector.appoint" when {

    def setup(response: CompaniesHouseResponse): CompaniesHouseConnector = {
      val url = s"http://localhost:9262/companies-house-api-proxy/company/${crn.crn}"
      mockHttpGet[CompaniesHouseResponse](url)(response)
      new CompaniesHouseConnector(mockHttpClient, appConfig)
    }

    "Success response from Companies House" should {

      "return a Right(ValidCRN)" in {

        val connector = setup(Right(ValidCRN))
        val result = connector.validateCRN(crn)

        await(result) shouldBe Right(ValidCRN)
      }
    }

    "Not Found response from Companies House" should {

      "return a Left(InvalidCRN)" in {

        val connector = setup(Left(InvalidCRN))
        val result = connector.validateCRN(crn)

        await(result) shouldBe Left(InvalidCRN)
      }
    }

    "Other Error response from Companies House" should {

      "return a Left(UnexpectedFailure)" in {

        val connector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result = connector.validateCRN(crn)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }
}

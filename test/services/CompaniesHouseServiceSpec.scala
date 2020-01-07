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

package services

import connectors.HttpHelper.CompaniesHouseResponse
import connectors.mocks.MockCompaniesHouseConnector
import connectors.{InvalidCRN, UnexpectedFailure, ValidCRN}
import play.api.http.Status._
import utils.BaseSpec

class CompaniesHouseServiceSpec extends MockCompaniesHouseConnector with BaseSpec {

  "CompaniesHouseService.appoint" when {

    def setup(response: CompaniesHouseResponse): CompaniesHouseService = {
      mockValidateCRN(crn)(response)
      new CompaniesHouseService(mockCompaniesHouseConnector)
    }

    "CRN is valid" should {

      "return a Right(ValidCRN)" in {

        val service = setup(Right(ValidCRN))
        val result = service.invalidCRNs(Seq(crn))

        await(result) shouldBe Right(ValidCRN)
      }
    }

    "CRN is invalid" should {

      "return a Right(ValidCRN)" in {

        val service = setup(Left(InvalidCRN))
        val result = service.invalidCRNs(Seq(crn))

        await(result) shouldBe Left(InvalidCRN)
      }
    }

    "update is unsuccessful" should {

      "return a Left(UnexpectedFailure)" in {

        val service = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result = service.invalidCRNs(Seq(crn))

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }
}

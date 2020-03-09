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

package v1.services

import play.api.http.Status._
import play.api.libs.json.{JsPath, Json}
import utils.BaseSpec
import v1.connectors.mocks.MockCompaniesHouseConnector
import v1.connectors.{InvalidCRN, UnexpectedFailure, ValidCRN}
import v1.models.CRNModel
import v1.models.errors.ValidationErrorResponseModel
import v1.validation.INVALID_CRN

class CompaniesHouseServiceSpec extends MockCompaniesHouseConnector with BaseSpec {

  "CompaniesHouseService.appoint" when {

    object TestCompaniesHouseService extends CompaniesHouseService(mockCompaniesHouseConnector)

    val jsPath = JsPath  \ "crn"

    "CRN is valid" should {

      "return a Right(ValidCRN)" in {

        mockValidateCRN(crn)(Right(ValidCRN))

        val actualResult = TestCompaniesHouseService.invalidCRNs(Seq(jsPath -> crn))
        val expectedResult = Right(Seq.empty)

        await(actualResult) shouldBe expectedResult
      }
    }

    "Multiple CRNs are given and valid" should {

      "return a Right(ValidCRN)" in {

        val testCrns: Seq[(JsPath, CRNModel)] = for(_ <- 1 to 1000) yield {
          mockValidateCRN(crn)(Right(ValidCRN))
          jsPath -> crn
        }
        
        val actualResult = TestCompaniesHouseService.invalidCRNs(testCrns)
        val expectedResult = Right(Seq.empty)

        await(actualResult) shouldBe expectedResult
      }
    }

    "CRN is invalid" should {

      "return a Right(ValidCRN)" in {

        mockValidateCRN(crn)(Left(InvalidCRN))

        val actualResult = TestCompaniesHouseService.invalidCRNs(Seq(JsPath \ "crn" -> crn))
        val expectedResult = Right(Seq(ValidationErrorResponseModel(INVALID_CRN, jsPath, Seq(InvalidCRN.body))))

        await(actualResult) shouldBe expectedResult
      }
    }

    "update is unsuccessful" should {

      "return a Left(UnexpectedFailure)" in {

        mockValidateCRN(crn)(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))

        val actualResult = TestCompaniesHouseService.invalidCRNs(Seq(JsPath \ "crn" -> crn))
        val expectedResult = Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))

        await(actualResult) shouldBe expectedResult
      }
    }
  }
}

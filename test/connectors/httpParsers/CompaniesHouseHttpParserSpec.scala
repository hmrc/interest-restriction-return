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

package connectors.httpParsers

import connectors.UnexpectedFailure
import connectors.httpParsers.CompaniesHouseHttpParser.{CompaniesHouseReads, InvalidCRN}
import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status
import uk.gov.hmrc.http.HttpResponse

class CompaniesHouseHttpParserSpec extends WordSpec with Matchers with GuiceOneAppPerSuite  {

  "CompaniesHouseHttpParser.CompaniesHouseReads" when {

    "given an (200)" should {

      "return a Right(ValidCRN)" in {

        val expectedResult = Right(true)
        val actualResult = CompaniesHouseReads.read("", "", HttpResponse(Status.OK))

        actualResult shouldBe expectedResult
      }
    }

    "given an (404) Not Found" should {

      "return a Left(InvalidCRN)" in {

        val expectedResult = Left(InvalidCRN)
        val actualResult = CompaniesHouseReads.read("", "", HttpResponse(Status.NOT_FOUND))

        actualResult shouldBe expectedResult
      }
    }


    "given any other status" should {

      "return a Left(UnexpectedFailure)" in {

        val expectedResult = Left(UnexpectedFailure(
          Status.INTERNAL_SERVER_ERROR,
          s"Status ${Status.INTERNAL_SERVER_ERROR} Error returned when calling Companies House"
        ))
        val actualResult = CompaniesHouseReads.read("", "", HttpResponse(Status.INTERNAL_SERVER_ERROR))

        actualResult shouldBe expectedResult
      }
    }
  }
}

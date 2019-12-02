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

package connectors.httpParsers

import connectors.httpParsers.FullReturnHttpParser.{FullReturnReads, InvalidSuccessResponse, SuccessResponse, UnexpectedFailure}
import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status
import play.api.libs.json.Json
import uk.gov.hmrc.http.HttpResponse

class FullReturnHttpParserSpec extends WordSpec with Matchers with GuiceOneAppPerSuite  {

  val ackRef = "ackRef"
  val ackRefResponse = Json.obj("acknowledgementReference" -> ackRef)

  "FullReturnHttpParser.FullReturnReads" when {

    "given an (200) with a valid ackRef response" should {

      "return a Right containing an acknowledgementReference" in {

        val expectedResult = Right(SuccessResponse(ackRef))
        val actualResult = FullReturnReads.read("", "", HttpResponse(Status.OK, Some(ackRefResponse)))

        actualResult shouldBe expectedResult
      }
    }

    "given an (200) with an invalid ackRef response" should {

      "return a Left(InvalidSuccessResponse)" in {

        val expectedResult = Left(InvalidSuccessResponse)
        val actualResult = FullReturnReads.read("", "", HttpResponse(Status.OK, Some(Json.obj())))

        actualResult shouldBe expectedResult
      }
    }

    "given any other status" should {

      "return a Left(UnexpectedFailure)" in {

        val expectedResult = Left(UnexpectedFailure(
          Status.INTERNAL_SERVER_ERROR,
          s"Status ${Status.INTERNAL_SERVER_ERROR} Error returned when trying to submit a full return"
        ))
        val actualResult = FullReturnReads.read("", "", HttpResponse(Status.INTERNAL_SERVER_ERROR))

        actualResult shouldBe expectedResult
      }
    }
  }
}
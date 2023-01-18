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

package v1.connectors.httpParsers

import assets.fullReturn.FullReturnConstants._
import v1.connectors.{DesSuccessResponse, InvalidSuccessResponse, UnexpectedFailure}
import v1.connectors.httpParsers.FullReturnHttpParser.FullReturnReads
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status
import play.api.libs.json.Json
import uk.gov.hmrc.http.HttpResponse
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FullReturnHttpParserSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {

  val ackRefResponse = Json.obj("acknowledgementReference" -> ackRef)

  "FullReturnHttpParser.FullReturnReads" when {

    "given an (201) with a valid ackRef response" should {

      "return a Right containing an acknowledgementReference" in {

        val expectedResult = Right(DesSuccessResponse(ackRef))
        val actualResult   =
          FullReturnReads.read("", "", HttpResponse(Status.CREATED, ackRefResponse, Map.empty[String, Seq[String]]))

        actualResult shouldBe expectedResult
      }
    }

    "given an (201) with an invalid ackRef response" should {

      "return a Left(InvalidSuccessResponse)" in {

        val expectedResult = Left(InvalidSuccessResponse)
        val actualResult   =
          FullReturnReads.read("", "", HttpResponse(Status.CREATED, Json.obj(), Map.empty[String, Seq[String]]))

        actualResult shouldBe expectedResult
      }
    }
    "given any other status" should {

      val expectedResult = Left(
        UnexpectedFailure(
          Status.INTERNAL_SERVER_ERROR,
          s"Status ${Status.INTERNAL_SERVER_ERROR} Error returned when trying to submit a full return"
        )
      )

      "return a Left(UnexpectedFailure) for a 500" in {

        val actualResult = FullReturnReads.read(
          "",
          "",
          HttpResponse(Status.INTERNAL_SERVER_ERROR, Json.obj(), Map.empty[String, Seq[String]])
        )

        actualResult shouldBe expectedResult
      }

      "return a Left(UnexpectedFailure) for a 200" in {

        val actualResult =
          FullReturnReads.read("", "", HttpResponse(Status.OK, ackRefResponse, Map.empty[String, Seq[String]]))

        actualResult shouldBe expectedResult
      }
    }
  }
}

/*
 * Copyright 2025 HM Revenue & Customs
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

import data.fullReturn.FullReturnConstants.*
import v1.connectors.{DesSuccessResponse, InvalidSuccessResponse, UnexpectedFailure}
import v1.connectors.httpParsers.FullReturnHttpParser.FullReturnReads
import play.api.http.Status.*
import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.http.HttpResponse
import utils.BaseSpec
import v1.connectors.HttpHelper.SubmissionResponse

class FullReturnHttpParserSpec extends BaseSpec {

  private val ackRefResponse: JsObject = Json.obj("acknowledgementReference" -> ackRef)

  "FullReturnHttpParser" when {
    "FullReturnReads.read" should {
      "return a Right containing an acknowledgementReference" when {
        "receiving an 201 CREATED with a valid ackRef response" in {
          val expectedResult: SubmissionResponse = Right(DesSuccessResponse(ackRef))
          val actualResult: SubmissionResponse   =
            FullReturnReads.read("", "", HttpResponse(CREATED, ackRefResponse, Map.empty[String, Seq[String]]))

          actualResult shouldBe expectedResult
        }
        "receiving an 200 OK with a valid ackRef response" in {
          val expectedResult: SubmissionResponse = Right(DesSuccessResponse(ackRef))
          val actualResult: SubmissionResponse   =
            FullReturnReads.read("", "", HttpResponse(CREATED, ackRefResponse, Map.empty[String, Seq[String]]))

          actualResult shouldBe expectedResult
        }
      }

      "return a Left InvalidSuccessResponse" when {
        "receiving an 201 CREATED with an invalid ackRef response" in {
          val expectedResult: SubmissionResponse = Left(InvalidSuccessResponse())
          val actualResult: SubmissionResponse   =
            FullReturnReads.read("", "", HttpResponse(CREATED, Json.obj(), Map.empty[String, Seq[String]]))

          actualResult shouldBe expectedResult
        }
      }

      "return a Left UnexpectedFailure" when {
        val expectedResult: SubmissionResponse = Left(
          UnexpectedFailure(
            INTERNAL_SERVER_ERROR,
            s"Status $INTERNAL_SERVER_ERROR Error returned when trying to submit a full return"
          )
        )
        "receiving a 500 INTERNAL_SERVER_ERROR with an invalid ackRef response" in {
          val actualResult: SubmissionResponse = FullReturnReads.read(
            "",
            "",
            HttpResponse(INTERNAL_SERVER_ERROR, Json.obj(), Map.empty[String, Seq[String]])
          )

          actualResult shouldBe expectedResult
        }
      }
    }
  }
}

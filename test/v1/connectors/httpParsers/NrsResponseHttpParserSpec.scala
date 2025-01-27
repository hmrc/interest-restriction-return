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

import java.util.UUID

import play.api.http.Status._
import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.http.HttpResponse
import utils.BaseSpec
import v1.connectors.HttpHelper.NrsResponse
import v1.connectors.httpParsers.NrsResponseHttpParser.NrsResponseReads
import v1.connectors.{InvalidSuccessResponse, UnexpectedFailure}
import v1.models.nrs.NrSubmissionId

class NrsResponseHttpParserSpec extends BaseSpec {

  private val nrSubmissionId: JsObject = Json.obj("nrSubmissionId" -> "5f4bdbef-0417-47a6-ac9e-ffc5a4905f7f")

  "NrsResponseHttpParser" when {
    "NrsResponseReads.read" should {
      "return a Right NrsResponse containing a nrSubmissionId" when {
        "receiving a 202 ACCEPTED with a valid nrSubmissionId response" in {
          val expectedResult: NrsResponse =
            Right(NrSubmissionId(UUID.fromString("5f4bdbef-0417-47a6-ac9e-ffc5a4905f7f")))
          val actualResult: NrsResponse   =
            NrsResponseReads.read("", "", HttpResponse(ACCEPTED, nrSubmissionId, Map("" -> Seq(""))))

          actualResult shouldBe expectedResult
        }
      }

      "return a Left InvalidSuccessResponse" when {
        "receiving a 202 ACCEPTED with an invalid nrSubmissionId response" in {
          val expectedResult: NrsResponse = Left(InvalidSuccessResponse())
          val actualResult: NrsResponse   =
            NrsResponseReads.read("", "", HttpResponse(ACCEPTED, JsObject.empty, Map("" -> Seq(""))))

          actualResult shouldBe expectedResult
        }
      }

      "return a Left UnexpectedFailure" when {
        "receiving a 500 INTERNAL_SERVER_ERROR with an invalid nrSubmissionId response" in {
          val expectedResult: NrsResponse = Left(
            UnexpectedFailure(
              status = INTERNAL_SERVER_ERROR,
              body = s"Status $INTERNAL_SERVER_ERROR Error returned when trying to submit Non Repudiation Submission"
            )
          )
          val actualResult                = NrsResponseReads.read(
            "",
            "",
            HttpResponse(INTERNAL_SERVER_ERROR, JsObject.empty, Map("" -> Seq("")))
          )

          actualResult shouldBe expectedResult
        }
      }
    }
  }
}

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

package v1.models

import play.api.libs.json.{JsError, Json}
import utils.BaseSpec
import v1.models.nrs.NrSubmissionId

import java.util.UUID

class NrSubmissionIdModelSpec extends BaseSpec {

  "NrSubmissionIdModel" must {

    ".toString" should {

      "return the UUID as a String" in {

        val randomId = UUID.randomUUID()

        val actual = NrSubmissionId(randomId).toString

        val expected = randomId.toString

        actual shouldBe expected
      }
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[NrSubmissionId] shouldBe JsError("Expected a JSON object")
      }
      "empty json" in {
        Json.obj().validate[NrSubmissionId] shouldBe a[JsError]
      }
      "there is an invalid UUID" in {
        Json.obj("nrSubmissionId" -> "b").validate[NrSubmissionId] shouldBe JsError("Invalid UUID string")
      }
    }
  }
}

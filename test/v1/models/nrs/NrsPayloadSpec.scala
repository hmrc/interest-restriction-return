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

package v1.models.nrs

import data.UnitNrsConstants.payload
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.libs.json.{JsSuccess, Json}

class NrsPayloadSpec extends AnyWordSpecLike with Matchers {
  "NrsPayload" when {
    "support round-trip serialization/deserialization" in {
      val value = payload(None)
      Json.toJson(value).validate[NrsPayload] shouldBe JsSuccess(value)
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[NrsPayload] isError
      }
      "empty json" in {
        Json.obj().validate[NrsPayload] isError
      }
    }
  }
}

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

package v1.models.authAction

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.libs.json.{JsSuccess, Json}

class ForbiddenIndividualsErrorSpec extends AnyWordSpecLike with Matchers {
  private val message =
    "You are unable to access this service as an individual. This service is only available to individual companies or groups of companies."
  "ForbiddenIndividualsError" should {
    "support round-trip serialization/deserialization" in {
      Json.toJson(new ForbiddenIndividualsError).validate[ForbiddenIndividualsError] shouldBe JsSuccess(
        new ForbiddenIndividualsError()
      )
    }
  }
  "deserialization" when {
    "the object is empty" in {
      val json = Json.obj()
      json.validate[ForbiddenIndividualsError] shouldBe JsSuccess(new ForbiddenIndividualsError)
    }
    "invalid code" in {
      val json = Json.obj(
        "code"    -> message,
        "message" -> message
      )
      json.validate[ForbiddenIndividualsError] isError
    }
  }
}

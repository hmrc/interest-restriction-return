/*
 * Copyright 2022 HM Revenue & Customs
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

import org.scalatest.TryValues._
import play.api.libs.json.{JsonValidationError, JsPath, JsResultException, JsString}

import scala.util.Try
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ElectionDecisionSpec extends AnyWordSpec with Matchers {
  "ElectionDecision" should {
    "valid" when {
      "elect is entered" in {
        val json = JsString("elect")
        val result = Try(json.as[ElectionDecision])

        result.isSuccess shouldEqual true
      }

      "revoke is entered" in {
        val json = JsString("revoke")
        val result = Try(json.as[ElectionDecision])

        result.isSuccess shouldEqual true
      }
    }

    "invalid" when {
      "an incorrect value is entered" in {
        val json = JsString("Something incorrect")
        val result = Try(json.as[ElectionDecision])

        result.failure.exception shouldEqual JsResultException(Seq((JsPath, Seq(JsonValidationError(s"Unknown ElectionDecision")))))
      }
    }
  }
}
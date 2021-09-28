/*
 * Copyright 2021 HM Revenue & Customs
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
import play.api.libs.json.{JsPath, JsString, JsResultException, JsonValidationError}
import scala.util.Try
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SubmissionTypeSpec extends AnyWordSpec with Matchers {
  "SubmissionType" should {
    "error when an incorrect value is entered" in {
      val json = JsString("Something incorrect")
      val result = Try(json.as[SubmissionType])

      result.failure.exception shouldEqual JsResultException(Seq((JsPath, Seq(JsonValidationError(s"Valid submission types are ${Original.toString} and ${Revised.toString}")))))
    }
  }
}
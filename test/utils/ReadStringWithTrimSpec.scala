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

package utils

import play.api.libs.json.{Format, JsString, Json}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ReadStringWithTrimSpec extends AnyWordSpec with Matchers {

  case class TestModelOurReads(someString: String, someInt: Int)
  object TestModelOurReads {
    import utils.ReadStringWithTrim.stringReads
    implicit val format: Format[TestModelOurReads] =
      Format[TestModelOurReads](Json.reads[TestModelOurReads], Json.writes[TestModelOurReads])
  }

  case class TestModelDefaultReads(someString: String, someInt: Int)
  object TestModelDefaultReads {
    implicit val format: Format[TestModelDefaultReads] =
      Format[TestModelDefaultReads](Json.reads[TestModelDefaultReads], Json.writes[TestModelDefaultReads])
  }

  "stringReads" should {
    "trim a top level string where it is imported" in {
      val testJson = JsString(" Some string ")
      testJson.as[String] shouldBe " Some string "

      import utils.ReadStringWithTrim.stringReads
      testJson.as[String] shouldBe "Some string"
    }

    "trim a nested string where it is imported" in {
      val testJson = Json.obj(
        "someString" -> " Some string ",
        "someInt"    -> 1
      )
      testJson.as[TestModelDefaultReads] shouldBe TestModelDefaultReads(" Some string ", 1)
      testJson.as[TestModelOurReads] shouldBe TestModelOurReads("Some string", 1)
    }

  }

}

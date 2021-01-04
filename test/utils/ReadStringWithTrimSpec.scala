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

package utils

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.{JsString, Json, Format}

class ReadStringWithTrimSpec extends WordSpec with Matchers {

  case class TestModelOurReads(someString: String, someInt: Int)
  object TestModelOurReads {
    import utils.ReadStringWithTrim.stringReads
    implicit val format: Format[TestModelOurReads] = Format[TestModelOurReads](Json.reads[TestModelOurReads], Json.writes[TestModelOurReads])
  }

  case class TestModelDefaultReads(someString: String, someInt: Int)
  object TestModelDefaultReads {
    implicit val format: Format[TestModelDefaultReads] = Format[TestModelDefaultReads](Json.reads[TestModelDefaultReads], Json.writes[TestModelDefaultReads])
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
        "someInt" -> 123
      )
      testJson.as[TestModelDefaultReads] shouldBe TestModelDefaultReads(" Some string ", 123)
      testJson.as[TestModelOurReads] shouldBe TestModelOurReads("Some string", 123)
    }

  }

}

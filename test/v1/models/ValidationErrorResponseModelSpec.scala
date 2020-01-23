/*
 * Copyright 2020 HM Revenue & Customs
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

package models

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json._
import v1.models.errors.ValidationErrorResponseModel

class ValidationErrorResponseModelSpec extends WordSpec with Matchers {

  "Validation Error Response" should {

    val errors: Seq[(JsPath, Seq[JsonValidationError])] = Seq(JsPath \ "FOO" -> Seq(JsonValidationError(Seq("BAR", "Snakes")), JsonValidationError(Seq("bye", "hello"))))

    "be successfully constructed given a sequence of Json validation errors" in {

      val expected = Seq(ValidationErrorResponseModel(field = "/FOO", errors = Seq("BAR", "Snakes", "bye", "hello")))

      ValidationErrorResponseModel(errors) shouldBe expected
    }

    "serialise to Json correctly" in {

      val expected = Json.arr(Json.obj(
        "field" -> "/FOO",
        "errors" -> Json.arr("BAR", "Snakes", "bye", "hello")
      ))

      Json.toJson(ValidationErrorResponseModel(errors)) shouldBe expected
    }
  }
}

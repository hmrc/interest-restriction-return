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

package models

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json._
import v1.models.errors.{ValidationErrorResponseModel, ErrorResponseModel}
import v1.validation.fullReturn._
import cats.data.{NonEmptyChain, Chain}

class ValidationErrorResponseModelSpec extends WordSpec with Matchers {

  "Validation Error Response" should {

    val errors: Seq[(JsPath, Seq[JsonValidationError])] = Seq(
      JsPath \ "FOO" -> Seq(JsonValidationError(Seq("BAR", "Snakes")), JsonValidationError(Seq("bye", "hello"))))

    "be successfully constructed given a sequence of Json validation errors" in {

      val expectedErrors = Seq(
        ErrorResponseModel(code = "JSON_VALIDATION_ERROR", message = "BAR", path = Some("/FOO")),
        ErrorResponseModel(code = "JSON_VALIDATION_ERROR", message = "Snakes", path = Some("/FOO")),
        ErrorResponseModel(code = "JSON_VALIDATION_ERROR", message = "bye", path = Some("/FOO")),
        ErrorResponseModel(code = "JSON_VALIDATION_ERROR", message = "hello", path = Some("/FOO"))
      )

      val expected = ValidationErrorResponseModel(
        code = "BAD_REQUEST", 
        message = "Request contains validation errors",
        errors = expectedErrors
      )

      ValidationErrorResponseModel(errors) shouldBe expected
    }

    "serialise Json Validation Errors to Json correctly" in {

      val expected = Json.obj(
        "code" -> "BAD_REQUEST",
        "message" -> "Request contains validation errors",
        "errors" -> Json.arr(
          Json.obj(
            "code" -> "JSON_VALIDATION_ERROR",
            "message" -> "BAR",
            "path" -> "/FOO"
          ), 
          Json.obj(
            "code" -> "JSON_VALIDATION_ERROR",
            "message" -> "Snakes",
            "path" -> "/FOO"
          ), 
          Json.obj(
            "code" -> "JSON_VALIDATION_ERROR",
            "message" -> "bye",
            "path" -> "/FOO"
          ), 
          Json.obj(
            "code" -> "JSON_VALIDATION_ERROR",
            "message" -> "hello",
            "path" -> "/FOO"
          )
        )
      )

      Json.toJson(ValidationErrorResponseModel(errors)) shouldBe expected
    }

    val apiErrors = Seq(NegativeAngieError(-123), TotalRestrictionsDecimalError(33.22222))
    val apiErrorChain = NonEmptyChain.fromChainUnsafe(
      Chain.fromSeq(apiErrors)
    )

    "be successfully constructed given a sequence of our Validation Errors" in {

      val expectedErrors = Seq(
        ErrorResponseModel(code = "NEGATIVE_ANGIE", message = "ANGIE cannot be negative", path = Some("/angie"), value = Some(Json.toJson(-123))),
        ErrorResponseModel(code = "TOTAL_RESTRICTIONS_DECIMAL", message = "totalRestrictions has greater than the allowed 2 decimal places.", path = Some("/totalRestrictions"), value = Some(Json.toJson(33.22222)))
      )

      val expected = ValidationErrorResponseModel(
        code = "BAD_REQUEST", 
        message = "Request contains validation errors",
        errors = expectedErrors
      )

      ValidationErrorResponseModel(apiErrorChain) shouldBe expected
    }

    "serialise our Validation Errors to Json correctly" in {

      val expected = Json.obj(
        "code" -> "BAD_REQUEST",
        "message" -> "Request contains validation errors",
        "errors" -> Json.arr(
          Json.obj(
            "code" -> "NEGATIVE_ANGIE",
            "message" -> "ANGIE cannot be negative",
            "path" -> "/angie",
            "value" -> -123
          ), 
          Json.obj(
            "code" -> "TOTAL_RESTRICTIONS_DECIMAL",
            "message" -> "totalRestrictions has greater than the allowed 2 decimal places.",
            "path" -> "/totalRestrictions",
            "value" -> 33.22222
          )
        )
      )

      Json.toJson(ValidationErrorResponseModel(apiErrorChain)) shouldBe expected
    }
  }
}

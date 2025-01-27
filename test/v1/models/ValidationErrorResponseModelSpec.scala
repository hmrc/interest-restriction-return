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

import play.api.libs.json._
import v1.models.errors.{ErrorResponseModel, ValidationErrorResponseModel}
import v1.validation.fullReturn._
import cats.data.{Chain, NonEmptyChain}
import utils.BaseSpec

class ValidationErrorResponseModelSpec extends BaseSpec {

  "ValidationErrorResponseModel" should {

    val errors: Seq[(JsPath, Seq[JsonValidationError])] =
      Seq(JsPath \ "FOO" -> Seq(JsonValidationError(Seq("BAR", "Snakes")), JsonValidationError(Seq("bye", "hello"))))

    "be successfully constructed given a sequence of Json validation errors" in {

      val expectedErrors = Seq(
        ErrorResponseModel(code = "JSON_VALIDATION_ERROR", message = "BAR", path = Some("/FOO")),
        ErrorResponseModel(code = "JSON_VALIDATION_ERROR", message = "Snakes", path = Some("/FOO")),
        ErrorResponseModel(code = "JSON_VALIDATION_ERROR", message = "bye", path = Some("/FOO")),
        ErrorResponseModel(code = "JSON_VALIDATION_ERROR", message = "hello", path = Some("/FOO"))
      )

      val expected = ValidationErrorResponseModel(
        code = "INVALID_REQUEST",
        message = "Request contains validation errors",
        errors = Some(expectedErrors),
        path = None,
        value = None
      )

      ValidationErrorResponseModel(errors) shouldBe expected
    }

    "serialise Json Validation Errors to Json correctly" in {

      val expected = Json.obj(
        "code"    -> "INVALID_REQUEST",
        "message" -> "Request contains validation errors",
        "errors"  -> Json.arr(
          Json.obj(
            "code"    -> "JSON_VALIDATION_ERROR",
            "message" -> "BAR",
            "path"    -> "/FOO"
          ),
          Json.obj(
            "code"    -> "JSON_VALIDATION_ERROR",
            "message" -> "Snakes",
            "path"    -> "/FOO"
          ),
          Json.obj(
            "code"    -> "JSON_VALIDATION_ERROR",
            "message" -> "bye",
            "path"    -> "/FOO"
          ),
          Json.obj(
            "code"    -> "JSON_VALIDATION_ERROR",
            "message" -> "hello",
            "path"    -> "/FOO"
          )
        )
      )

      Json.toJson(ValidationErrorResponseModel(errors)) shouldBe expected
    }

    val apiErrors     = Seq(NegativeAngieError(-1), TotalRestrictionsDecimalError(33.22222))
    val apiErrorChain = NonEmptyChain.fromChainUnsafe(
      Chain.fromSeq(apiErrors)
    )

    "be successfully constructed given a sequence of our Validation Errors" in {

      val expectedErrors = Seq(
        ErrorResponseModel(
          code = "NEGATIVE_ANGIE",
          message = "ANGIE must be a positive number",
          path = Some("/angie"),
          value = Some(Json.toJson(-1))
        ),
        ErrorResponseModel(
          code = "TOTAL_RESTRICTIONS_DECIMAL",
          message = "Total restrictions must be to 2 decimal places or less",
          path = Some("/totalRestrictions"),
          value = Some(Json.toJson(33.22222))
        )
      )

      val expected = ValidationErrorResponseModel(
        code = "INVALID_REQUEST",
        message = "Request contains validation errors",
        errors = Some(expectedErrors),
        path = None,
        value = None
      )

      ValidationErrorResponseModel(apiErrorChain) shouldBe expected
    }

    "serialise our Validation Errors to Json correctly" in {

      val expected = Json.obj(
        "code"    -> "INVALID_REQUEST",
        "message" -> "Request contains validation errors",
        "errors"  -> Json.arr(
          Json.obj(
            "code"    -> "NEGATIVE_ANGIE",
            "message" -> "ANGIE must be a positive number",
            "path"    -> "/angie",
            "value"   -> -1
          ),
          Json.obj(
            "code"    -> "TOTAL_RESTRICTIONS_DECIMAL",
            "message" -> "Total restrictions must be to 2 decimal places or less",
            "path"    -> "/totalRestrictions",
            "value"   -> 33.22222
          )
        )
      )

      Json.toJson(ValidationErrorResponseModel(apiErrorChain)) shouldBe expected
    }

    val singleApiError      = Seq(TotalRestrictionsDecimalError(33.22222))
    val singleApiErrorChain = NonEmptyChain.fromChainUnsafe(
      Chain.fromSeq(singleApiError)
    )

    "be successfully constructed given a single Validation Errors" in {
      val expected = ValidationErrorResponseModel(
        code = "TOTAL_RESTRICTIONS_DECIMAL",
        message = "Total restrictions must be to 2 decimal places or less",
        errors = None,
        path = Some("/totalRestrictions"),
        value = Some(Json.toJson(33.22222))
      )

      ValidationErrorResponseModel(singleApiErrorChain) shouldBe expected
    }

    "serialise a single validation error to Json correctly" in {

      val expected = Json.obj(
        "code"    -> "TOTAL_RESTRICTIONS_DECIMAL",
        "message" -> "Total restrictions must be to 2 decimal places or less",
        "path"    -> "/totalRestrictions",
        "value"   -> 33.22222
      )

      Json.toJson(ValidationErrorResponseModel(singleApiErrorChain)) shouldBe expected
    }
  }
}

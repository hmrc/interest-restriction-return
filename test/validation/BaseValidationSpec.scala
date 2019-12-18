/*
 * Copyright 2019 HM Revenue & Customs
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

package validation

import cats.data.{NonEmptyChain, Validated, ValidatedNec}
import cats.data.Validated.{Invalid, Valid}
import cats.implicits._
import cats.kernel.Semigroup
import models.Validation
import models.Validation.ValidationResult
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.{JsPath, JsString}

class BaseValidationSpec extends WordSpec with Matchers{

  implicit val topPath = JsPath \ "path"

  case class TestError(implicit topPath: JsPath) extends Validation {
    val errorMessage: String = "error"
    val path = topPath \ "path"
    val value = JsString("bad")
  }

  case class TestError1(implicit topPath: JsPath) extends Validation {
    val errorMessage: String = "error1"
    val path = topPath \ "path1"
    val value = JsString("bad1")
  }

  val baseValidation = new BaseValidation {}

  "combineValidationsForField" should {
    "return a single validation if there are no errors" in {

      val result = baseValidation.combineValidationsForField(Valid("ok"))

      result.getOrElse("") shouldBe "ok"
    }

    "return 2 invalids as a single message if there are 2 errors" in {

      val result = baseValidation.combineValidationsForField(TestError().invalidNec,TestError1().invalidNec)

      result.toEither.left.get.head.errorMessage shouldBe "error|error1"
    }
  }

  "combineValidations" should {
    "return a single validation if there are no errors" in {

      val result = baseValidation.combineValidations(Valid("ok"))

      result.getOrElse("") shouldBe "ok"
    }

    "return 2 invalids if there are 2 errors" in {

      val result = baseValidation.combineValidations(TestError().invalidNec,TestError1().invalidNec)

      result.toEither.left.get.head.errorMessage shouldBe "error"
      result.toEither.left.get.toList(1).errorMessage shouldBe "error1"
    }
  }

  "optionValidations" should {
    "return a single validation if there are no errors" in {

      val result = baseValidation.optionValidations(Some(Valid("ok")))

      result.getOrElse("") shouldBe Some("ok")
    }

    "return none if the data was empty" in {

      val result = baseValidation.optionValidations(None)

      result.getOrElse("") shouldBe None
    }

    "return 2 invalids if there are 2 errors" in {

      val result = baseValidation.optionValidations(Some(TestError().invalidNec),Some(TestError1().invalidNec))

      result.toEither.left.get.head.errorMessage shouldBe "error"
      result.toEither.left.get.toList(1).errorMessage shouldBe "error1"
    }
  }

}

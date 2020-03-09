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

package v1.validation

import cats.data.Validated.Valid
import cats.implicits._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.{JsPath, JsString}
import utils.BaseSpec
import v1.models.Validation

class BaseValidationSpec extends WordSpec with Matchers with BaseSpec {

  implicit val topPath = JsPath \ "path"

  case class TestError(implicit topPath: JsPath) extends Validation {
    val code = MISSING_FIELD
    val message: String = "error"
    val path = topPath \ "path"
    }

  case class TestError1(implicit topPath: JsPath) extends Validation {
    val code = MISSING_FIELD
    val message: String = "error1"
    val path = topPath \ "path1"
    }

  val baseValidation = new BaseValidation {}

  "combineValidationsForField" should {
    "return a single v1.validation if there are no errors" in {

      val result = baseValidation.combineValidationsForField(Valid("ok"))

      rightSide(result) shouldBe "ok"
    }

    "return 2 invalids as a single message if there are 2 errors" in {

      val result = baseValidation.combineValidationsForField(TestError().invalidNec,TestError1().invalidNec)

      leftSideError(result).message shouldBe "error|error1"
    }
  }

  "combineValidations" should {
    "return a single v1.validation if there are no errors" in {

      val result = baseValidation.combineValidations(Valid("ok"))

     rightSide(result) shouldBe "ok"
    }

    "return 2 invalids if there are 2 errors" in {

      val result = baseValidation.combineValidations(TestError().invalidNec,TestError1().invalidNec)

      leftSideError(result).message shouldBe "error"
      leftSideError(result,1).message shouldBe "error1"
    }
  }

  "optionValidations" should {
    "return a single v1.validation if there are no errors" in {

      val result = baseValidation.optionValidations(Some(Valid("ok")))

      rightSide(result) shouldBe Some("ok")
    }

    "return none if the data was empty" in {

      val result = baseValidation.optionValidations(None)

      rightSide(result) shouldBe None
    }

    "return 3 invalids if there are 3 errors" in {

      val result = baseValidation.optionValidations(Some(TestError().invalidNec),Some(TestError1().invalidNec),Some(TestError1().invalidNec))

      leftSideError(result).message shouldBe "error"
      leftSideError(result,1).message shouldBe "error1"
      leftSideError(result,2).message shouldBe "error1"
    }

    "return Right(None) if there are no valids or invalids" in {

      val result1 = baseValidation.optionValidations()

      rightSide(result1) shouldBe None
    }
  }
}

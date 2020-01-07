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

package validation
import cats.data.NonEmptyChain
import cats.data.Validated.{Invalid, Valid}
import models.Validation
import models.Validation.ValidationResult
import play.api.Logger
import play.api.libs.json.{JsPath, JsValue}

import scala.annotation.tailrec

trait BaseValidation {
  import cats.implicits._

  def combineValidationsForField[T](validations: ValidationResult[T]*): ValidationResult[T] = {
    val invalids = validations.collect { case Invalid(invalid) => invalid}
    invalids match {
      case seq if seq.isEmpty => validations.head
      case errors => {
        val validations = errors.flatMap(_.toList)
        new Validation {
          override val errorMessage: String = validations.map(_.errorMessage).mkString("|")
          override val path: JsPath = validations.head.path
          override val value: JsValue = validations.head.value
        }.invalidNec
      }
    }
  }

  def combineValidations[T](validations: ValidationResult[T]*): ValidationResult[T] = {
    val invalids = validations.collect { case Invalid(invalid) => invalid}
    invalids match {
      case seq if seq.isEmpty => validations.head
      case errors => Invalid(combineInvalids(errors.tail,errors.head))
    }
  }

  def optionValidations[T](validations: Option[ValidationResult[T]]*): ValidationResult[Option[T]] = {
    val invalids = validations.collect { case Some(Invalid(invalid)) => invalid}
    val valid = validations.collect { case Some(Valid(v)) => v}
    invalids match {
      case seq if seq.isEmpty => if(valid.nonEmpty) Some(valid.head).validNec else None.validNec
      case errors => Invalid(combineInvalids(errors.tail,errors.head))
    }
  }

  @tailrec
  private def combineInvalids(errors: Seq[NonEmptyChain[Validation]], combined: NonEmptyChain[Validation]): NonEmptyChain[Validation] = {
    if(errors.isEmpty) combined else {
      combineInvalids(errors.tail, combined.combine(errors.head))
    }
  }

}

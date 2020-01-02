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

import models.Validation.ValidationResult
import models.{CRNModel, Validation}
import play.api.libs.json.{JsPath, Json}

trait CRNValidator extends BaseValidation {

  import cats.implicits._

  val crnModel: CRNModel

  def validate(implicit path: JsPath): ValidationResult[CRNModel] = {
    val patternCRN = "^[0-9]{8}$|^[A-Z]{2}[0-9]{6}$".r
    crnModel.crn match {
      case patternCRN() => crnModel.validNec
      case _ => CRNFormatCheck(crnModel).invalidNec
    }
  }
}

case class CRNFormatCheck(crnValue: CRNModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "CRN supplied is incorrect format. CRN should be 8 numbers or 2 letters and 6 numbers"
  val value = Json.toJson(crnValue)
}

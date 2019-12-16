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

import models.Validation.ValidationResult
import models.{UTRModel, Validation}

case object UTRError extends Validation {
  def errorMessages: String = "UTR Check Sum does not satisfy the check sum"
}

trait UTRValidator {

  import cats.implicits._

  val utrModel: UTRModel

  private def validateUTR(utr: String): ValidationResult[String] = {

    val utrInts = utr.map(_.asDigit)

    def checkSum = {

      val utrSum = (utrInts(1) * 6) + (utrInts(2) * 7) + (utrInts(3) * 8) + (utrInts(4) * 9) + (utrInts(5) * 10) + (utrInts(6) * 5) + (utrInts(7) * 4) + (utrInts(8) * 3) + (utrInts(9) * 2)

      val utrCalc = 11 - (utrSum % 11)

      if (utrCalc > 9) utrCalc - 9 else utrCalc
    }

    if (checkSum == utrInts(0)) {
      utr.validNec
    } else {
      UTRError.invalidNec
    }
  }

  def validate: ValidationResult[UTRModel] = validateUTR(utrModel.utr).map(UTRModel.apply)
}
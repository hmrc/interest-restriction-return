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

package validation.fullReturn

import java.time.LocalDate

import models.Validation
import models.Validation.ValidationResult
import models.fullReturn.AllocatedRestrictionsModel
import play.api.libs.json.{JsPath, Json}
import validation.BaseValidation

trait AllocatedRestrictionsValidator extends BaseValidation {

  import cats.implicits._

  val allocatedRestrictionsModel: AllocatedRestrictionsModel

  private def apRestrictionValidator(endDate: Option[LocalDate],
                                     amount: Option[BigDecimal],
                                     i: Int)(implicit path: JsPath): ValidationResult[(Option[LocalDate], Option[BigDecimal])] = {
    val period = (endDate, amount)
    period match {
      case (Some(_), None) => AllocatedRestrictionNotSupplied(i).invalidNec
      case (None, Some(_)) => AllocatedRestrictionSupplied(i).invalidNec
      case (Some(_), Some(amt)) if amt < 0 => AllocatedRestrictionNegative(i, amt).invalidNec
      case _ => period.validNec
    }
  }

  private def validateAllAps(implicit topPath: JsPath): ValidationResult[(Option[LocalDate], Option[LocalDate], Option[LocalDate])] = {
    val apDates = (allocatedRestrictionsModel.ap1End, allocatedRestrictionsModel.ap2End, allocatedRestrictionsModel.ap3End)
    apDates match {
      case (None, Some(_), _) => AllocatedRestrictionLaterPeriodSupplied(2).invalidNec
      case (None, _, Some(_)) => AllocatedRestrictionLaterPeriodSupplied(3).invalidNec
      case (_, None, Some(_)) => AllocatedRestrictionLaterPeriodSupplied(3).invalidNec
      case _ => apDates.validNec
    }
  }

  def validateTotalRestriction(implicit path: JsPath): ValidationResult[BigDecimal] = {

    val hasApRestrictions =
      allocatedRestrictionsModel.disallowanceAp1.isDefined ||
        allocatedRestrictionsModel.disallowanceAp2.isDefined ||
        allocatedRestrictionsModel.disallowanceAp3.isDefined

    if (hasApRestrictions && allocatedRestrictionsModel.totalDisallowances.isEmpty) {
      AllocatedRestrictionTotalNotSupplied().invalidNec
    } else {
      val totalDisallowances: BigDecimal = allocatedRestrictionsModel.totalDisallowances.getOrElse(0)
      if (totalDisallowances < 0) {
        AllocatedRestrictionTotalNegative(totalDisallowances).invalidNec
      } else {
        totalDisallowances.validNec
      }
    }
  }

  def validate(implicit path: JsPath): ValidationResult[AllocatedRestrictionsModel] =
    (apRestrictionValidator(allocatedRestrictionsModel.ap1End, allocatedRestrictionsModel.disallowanceAp1, 1),
      apRestrictionValidator(allocatedRestrictionsModel.ap2End, allocatedRestrictionsModel.disallowanceAp2, 2),
      apRestrictionValidator(allocatedRestrictionsModel.ap3End, allocatedRestrictionsModel.disallowanceAp3, 3),
      validateAllAps,
      validateTotalRestriction).mapN((_,_,_,_,_) => allocatedRestrictionsModel)
}

case class AllocatedRestrictionNotSupplied(i: Int)(implicit topPath: JsPath) extends Validation {
  val path = topPath \ s"disallowanceAp$i"
  val errorMessage: String = s"disallowanceAp$i must have a value when ap${i}End is supplied"
  val value = Json.obj()
}

case class AllocatedRestrictionSupplied(i: Int)(implicit topPath: JsPath) extends Validation {
  val path = topPath \ s"disallowanceAp$i"
  val errorMessage: String = s"disallowanceAp$i cannot have a value when no ap${i}End is supplied"
  val value = Json.obj()
}

case class AllocatedRestrictionLaterPeriodSupplied(i: Int)(implicit topPath: JsPath) extends Validation {
  val path = topPath \ s"ap${i}End"
  val errorMessage: String = s"ap${i}End cannot be supplied as a previous period is missing"
  val value = Json.obj()
}

case class AllocatedRestrictionNegative(i: Int, amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val path = topPath \ s"disallowanceAp$i"
  val errorMessage: String = s"disallowanceAp$i cannot be negative"
  val value = Json.obj()
}

case class AllocatedRestrictionTotalNotSupplied(implicit topPath: JsPath) extends Validation {
  val path = topPath \ "totalDisallowances"
  val errorMessage: String = "totalDisallowances must be supplied if restrictions are supplied"
  val value = Json.obj()
}

case class AllocatedRestrictionTotalNegative(amt: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val path = topPath \ "totalDisallowances"
  val errorMessage: String = "totalDisallowances cannot be negative"
  val value = Json.obj()
}
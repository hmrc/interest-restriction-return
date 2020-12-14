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

package v1.validation.fullReturn

import config.Constants.maxTwoDecimalsRegex
import play.api.libs.json.{JsPath, Json}
import v1.models.Validation
import v1.models.Validation.ValidationResult
import v1.models.fullReturn.AdjustedGroupInterestModel
import v1.validation.BaseValidation

import scala.math.BigDecimal
import scala.math.BigDecimal.RoundingMode

trait AdjustedGroupInterestValidator extends BaseValidation {

  import cats.implicits._

  val adjustedGroupInterestModel: AdjustedGroupInterestModel

  private def validateGroupRatio(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val isBetween0And100 = adjustedGroupInterestModel.groupRatio >= 0 && adjustedGroupInterestModel.groupRatio <= 100

    if (isBetween0And100) {
      adjustedGroupInterestModel.groupRatio.validNec
    } else {
      GroupRatioError(adjustedGroupInterestModel.groupRatio).invalidNec
    }
  }

  private def validateGroupRatioCalculation(implicit path: JsPath): ValidationResult[BigDecimal] = {

    (adjustedGroupInterestModel.qngie, adjustedGroupInterestModel.groupEBITDA, adjustedGroupInterestModel.groupRatio) match {
      case (qngie, _, _) if qngie % 0.01 != 0 =>
        QngieDecimalError(qngie).invalidNec
      case (_, groupEBITDA, _) if groupEBITDA % 0.01 != 0 =>
        GroupEBITDADecimalError(groupEBITDA).invalidNec
      case (_, _, groupRatio) if groupRatio % 0.00001 != 0 =>
        GroupRatioDecimalError(groupRatio).invalidNec
      case (_, groupEBITDA, groupRatio) if groupEBITDA <= 0 && groupRatio == 100 =>
        adjustedGroupInterestModel.groupRatio.validNec
      case (_, groupEBITDA, groupRatio) if groupEBITDA <= 0 && groupRatio != 100 =>
        NegativeOrZeroGroupEBITDAError(groupRatio).invalidNec
      case (qngie, groupEBITA, groupRatio) if qngie / groupEBITA <= 0 && groupRatio != 100 =>
        NegativeOrZeroGroupRatioError(groupRatio).invalidNec
      case (qngie, groupEBITDA, groupRatio) if groupRatio != ((qngie / groupEBITDA) * 100).min(100).setScale(5, RoundingMode.HALF_UP) =>
        GroupRatioCalculationError(adjustedGroupInterestModel).invalidNec
      case _ => adjustedGroupInterestModel.groupRatio.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[AdjustedGroupInterestModel] =
    (validateGroupRatio,
      validateGroupRatioCalculation).mapN((_, _) => adjustedGroupInterestModel)
}

case class QngieDecimalError(qngie: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "qngie has greater than the allowed 2 decimal places."
  val path = topPath \ "qngie"
  val value = Json.toJson(qngie)
}

case class GroupEBITDADecimalError(groupEBITDA: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "groupEBITDA has greater than the allowed 2 decimal places."
  val path = topPath \ "groupEBITDA"
  val value = Json.toJson(groupEBITDA)
}

case class GroupRatioDecimalError(groupEBITDA: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "groupRatio has greater than the allowed 5 decimal places."
  val path = topPath \ "groupRatio"
  val value = Json.toJson(groupEBITDA)
}

case class GroupRatioError(groupRatio: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "Group Ratio must be between 0 and 100%"
  val path = topPath \ "groupRatio"
  val value = Json.toJson(groupRatio)
}

case class GroupRatioCalculationError(details: AdjustedGroupInterestModel)(implicit topPath: JsPath) extends Validation {

  val errorMessage: String = s"The value for Group Ratio Percent you provided ${details.groupRatio}, " +
    s"does not match the value calculated from the provided QNGIE (${details.qngie}) and group-EBITDA ${details.groupEBITDA}, " +
    s"${((details.qngie / details.groupEBITDA) * 100).setScale(5, RoundingMode.HALF_UP).min(100)}"
  val path = topPath \ "groupEBITDA"
  val value = Json.toJson(details)
}

case class NegativeOrZeroGroupEBITDAError(groupRatio: BigDecimal)(implicit topPath: JsPath) extends Validation {

  val errorMessage: String = "If group-EBITDA is negative or zero, groupRatio must be set to 100"
  val path = topPath \ "groupEBITDA"
  val value = Json.toJson(groupRatio)
}

case class NegativeOrZeroGroupRatioError(groupRatio: BigDecimal)(implicit topPath: JsPath) extends Validation {

  val errorMessage: String = "If group ratio calculation is negative then group ratio should be 100%"
  val path = topPath \ "groupRatio"
  val value = Json.toJson(groupRatio)
}

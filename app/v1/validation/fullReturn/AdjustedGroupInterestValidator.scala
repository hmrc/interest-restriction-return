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

package v1.validation.fullReturn

import play.api.libs.json.{Json, JsPath}
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

    adjustedGroupInterestModel.groupRatio match {
      case _ if !isBetween0And100 => GroupRatioError(adjustedGroupInterestModel.groupRatio).invalidNec
      case groupRatio if groupRatio % 0.00001 != 0 => GroupRatioDecimalError(groupRatio).invalidNec
      case _ => adjustedGroupInterestModel.groupRatio.validNec
    }
  }

  private def validateQngie(implicit path: JsPath): ValidationResult[BigDecimal] = {
    if (adjustedGroupInterestModel.qngie % 0.01 != 0) {
      QngieDecimalError(adjustedGroupInterestModel.qngie).invalidNec
    } else {
      adjustedGroupInterestModel.qngie.validNec
    }
  }

  private def validateGroupEBITDA(implicit path: JsPath): ValidationResult[BigDecimal] = {
    if (adjustedGroupInterestModel.groupEBITDA % 0.01 != 0) {
      GroupEBITDADecimalError(adjustedGroupInterestModel.groupEBITDA).invalidNec
    } else {
      adjustedGroupInterestModel.groupEBITDA.validNec
    }
  }

  private def validateGroupRatioCalculation(implicit path: JsPath): ValidationResult[BigDecimal] = {
    (adjustedGroupInterestModel.qngie, adjustedGroupInterestModel.groupEBITDA, adjustedGroupInterestModel.groupRatio) match {
      case (_, groupEBITDA, groupRatio) if groupEBITDA <= 0 && groupRatio == 100 =>
        adjustedGroupInterestModel.groupRatio.validNec
      case (_, groupEBITDA, groupRatio) if groupEBITDA <= 0 && groupRatio != 100 =>
        NegativeOrZeroGroupEBITDAError(groupRatio).invalidNec
      case (qngie, groupEBITA, groupRatio) if qngie / groupEBITA <= 0 && groupRatio != 100 =>
        NegativeOrZeroGroupRatioError(groupRatio).invalidNec
      case (qngie, groupEBITDA, groupRatio) if validationGroupRationCalc(qngie, groupEBITDA, groupRatio) =>
        GroupRatioCalculationError(adjustedGroupInterestModel).invalidNec
      case _ => adjustedGroupInterestModel.groupRatio.validNec
    }
  }

  private def validationGroupRationCalc(qngie: BigDecimal, groupEBITDA: BigDecimal, groupRatio: BigDecimal): Boolean = {
    val hasDecimals = groupRatio.toString.contains(".")
    val decimalPlaces = if (hasDecimals) {
      groupRatio.toString.length - groupRatio.toString.indexOf(".") - 1
    } else {
      0
    }

    groupRatio != ((qngie / groupEBITDA) * 100).min(100).setScale(decimalPlaces, RoundingMode.DOWN)
  }

  def validate(implicit path: JsPath): ValidationResult[AdjustedGroupInterestModel] =
    (validateGroupRatio,
      validateQngie,
      validateGroupEBITDA,
      validateGroupRatioCalculation).mapN((_, _, _, _) => adjustedGroupInterestModel)
  }

  case class QngieDecimalError(qngie: BigDecimal)(implicit topPath: JsPath) extends Validation {
    val code = "QNGIE_DECIMAL"
    val errorMessage: String = "qngie has greater than the allowed 2 decimal places."
    val path: JsPath = topPath \ "qngie"
    val value = Some(Json.toJson(qngie))
  }

  case class GroupEBITDADecimalError(groupEBITDA: BigDecimal)(implicit topPath: JsPath) extends Validation {
    val code = "EBITDA_DECIMAL"
    val errorMessage: String = "groupEBITDA has greater than the allowed 2 decimal places."
    val path: JsPath = topPath \ "groupEBITDA"
    val value = Some(Json.toJson(groupEBITDA))
  }

  case class GroupRatioDecimalError(groupEBITDA: BigDecimal)(implicit topPath: JsPath) extends Validation {
    val code = "GROUP_RATIO_DECIMAL"
    val errorMessage: String = "groupRatio has greater than the allowed 5 decimal places."
    val path: JsPath = topPath \ "groupRatio"
    val value = Some(Json.toJson(groupEBITDA))
  }

  case class GroupRatioError(groupRatio: BigDecimal)(implicit topPath: JsPath) extends Validation {
    val code = "GROUP_RATIO_RANGE"
    val errorMessage: String = "Group Ratio must be between 0 and 100%"
    val path: JsPath = topPath \ "groupRatio"
    val value = Some(Json.toJson(groupRatio))
  }

  case class GroupRatioCalculationError(details: AdjustedGroupInterestModel)(implicit topPath: JsPath) extends Validation {
    val code = "GROUP_RATIO_CALC"
    val errorMessage: String = s"The value for Group Ratio Percent you provided ${details.groupRatio}, " +
      s"does not match the value calculated from the provided QNGIE (${details.qngie}) and group-EBITDA ${details.groupEBITDA}, " +
      s"${((details.qngie / details.groupEBITDA) * 100).setScale(5, RoundingMode.HALF_UP).min(100)}"
    val path: JsPath = topPath \ "groupEBITDA"
    val value = Some(Json.toJson(details))
  }

  case class NegativeOrZeroGroupEBITDAError(groupEBITDA: BigDecimal)(implicit topPath: JsPath) extends Validation {
    val code = "EBITDA_NEGATIVE"
    val errorMessage: String = "If group-EBITDA is negative or zero, groupRatio must be set to 100"
    val path: JsPath = topPath \ "groupEBITDA"
    val value = Some(Json.toJson(groupEBITDA))
  }

  case class NegativeOrZeroGroupRatioError(groupRatio: BigDecimal)(implicit topPath: JsPath) extends Validation {
    val code = "GROUP_RATIO_NEGATIVE"
    val errorMessage: String = "If group ratio calculation is negative then group ratio should be 100%"
    val path: JsPath = topPath \ "groupRatio"
    val value = Some(Json.toJson(groupRatio))
  }

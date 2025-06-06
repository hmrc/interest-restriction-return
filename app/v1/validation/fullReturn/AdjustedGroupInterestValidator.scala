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

package v1.validation.fullReturn

import play.api.libs.json.{JsPath, JsValue, Json}
import v1.models.Validation
import v1.models.Validation.ValidationResult
import v1.models.fullReturn.AdjustedGroupInterestModel
import v1.validation.BaseValidation

import scala.math.BigDecimal

trait AdjustedGroupInterestValidator extends BaseValidation {

  import cats.implicits.*

  val adjustedGroupInterestModel: AdjustedGroupInterestModel

  private def validateGroupRatio(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val isBetween0And100 = adjustedGroupInterestModel.groupRatio >= 0 && adjustedGroupInterestModel.groupRatio <= 100

    adjustedGroupInterestModel.groupRatio match {
      case _ if !isBetween0And100 => GroupRatioError(adjustedGroupInterestModel.groupRatio).invalidNec
      case groupRatio if groupRatio % 0.00001 != 0 => GroupRatioDecimalError(groupRatio).invalidNec
      case _ => adjustedGroupInterestModel.groupRatio.validNec
    }
  }

  private def validateQngieNegative(implicit path: JsPath): ValidationResult[BigDecimal] =
    if (adjustedGroupInterestModel.qngie < 0) {
      NegativeQNGIEError(adjustedGroupInterestModel.qngie).invalidNec
    } else {
      adjustedGroupInterestModel.qngie.validNec
    }

  private def validateQngieDecimal(implicit path: JsPath): ValidationResult[BigDecimal] =
    if (adjustedGroupInterestModel.qngie % 0.01 != 0) {
      QngieDecimalError(adjustedGroupInterestModel.qngie).invalidNec
    } else {
      adjustedGroupInterestModel.qngie.validNec
    }

  private def validateGroupEBITDA(implicit path: JsPath): ValidationResult[Option[BigDecimal]] =
    adjustedGroupInterestModel.groupEBITDA match {
      case Some(ebitda) if ebitda % 0.01 != 0 => GroupEBITDADecimalError(ebitda).invalidNec
      case _ => adjustedGroupInterestModel.groupEBITDA.validNec
    }

  private def validateGroupRatioCalculation(implicit path: JsPath): ValidationResult[BigDecimal] =
    (
      adjustedGroupInterestModel.qngie,
      adjustedGroupInterestModel.groupEBITDA,
      adjustedGroupInterestModel.groupRatio
    ) match {
      case (_, Some(groupEBITDA), groupRatio) if groupEBITDA <= 0 && groupRatio != 100 =>
        NegativeOrZeroGroupEBITDAError(groupRatio).invalidNec
      case _                                                                           => adjustedGroupInterestModel.groupRatio.validNec
    }

  def validate(implicit path: JsPath): ValidationResult[AdjustedGroupInterestModel] =
    (
      validateGroupRatio,
      validateQngieDecimal,
      validateQngieNegative,
      validateGroupEBITDA,
      validateGroupRatioCalculation
    ).mapN((_, _, _, _, _) => adjustedGroupInterestModel)
}

case class QngieDecimalError(qngie: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code: String           = "QNGIE_DECIMAL"
  val errorMessage: String   = "QNGIE must be to 2 decimal places or less"
  val path: JsPath           = topPath \ "qngie"
  val value: Option[JsValue] = Some(Json.toJson(qngie))
}

case class GroupEBITDADecimalError(groupEBITDA: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code: String           = "EBITDA_DECIMAL"
  val errorMessage: String   = "Group EBITDA must be to 2 decimal places or less"
  val path: JsPath           = topPath \ "groupEBITDA"
  val value: Option[JsValue] = Some(Json.toJson(groupEBITDA))
}

case class GroupRatioDecimalError(groupEBITDA: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code: String           = "GROUP_RATIO_DECIMAL"
  val errorMessage: String   = "Group ratio must be to 5 decimal places or less"
  val path: JsPath           = topPath \ "groupRatio"
  val value: Option[JsValue] = Some(Json.toJson(groupEBITDA))
}

case class GroupRatioError(groupRatio: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code: String           = "GROUP_RATIO_RANGE"
  val errorMessage: String   = "Group ratio must be between 0% and 100%"
  val path: JsPath           = topPath \ "groupRatio"
  val value: Option[JsValue] = Some(Json.toJson(groupRatio))
}

case class NegativeOrZeroGroupEBITDAError(groupEBITDA: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code: String           = "EBITDA_NEGATIVE"
  val errorMessage: String   = "If group EBITDA is negative or zero, set group ratio to 100"
  val path: JsPath           = topPath \ "groupEBITDA"
  val value: Option[JsValue] = Some(Json.toJson(groupEBITDA))
}

case class NegativeQNGIEError(groupRatio: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code: String           = "QNGIE_NEGATIVE"
  val errorMessage: String   = "QNGIE must be a positive number"
  val path: JsPath           = topPath \ "qngie"
  val value: Option[JsValue] = Some(Json.toJson(groupRatio))
}

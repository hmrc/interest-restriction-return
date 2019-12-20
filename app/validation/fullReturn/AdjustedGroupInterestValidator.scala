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

import config.Constants.maxTwoDecimalsRegex
import models.Validation
import models.Validation.ValidationResult
import models.fullReturn.AdjustedGroupInterestModel
import play.api.libs.json.{JsPath, Json}
import validation.BaseValidation

import scala.math.BigDecimal

trait AdjustedGroupInterestValidator extends BaseValidation {

  import cats.implicits._

  val adjustedGroupInterestModel: AdjustedGroupInterestModel

  private def validateGroupRatio(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val isBetween0And100 = adjustedGroupInterestModel.groupRatio >= 0 && adjustedGroupInterestModel.groupRatio <= 100
    val hasTwoDecimalPlaces = adjustedGroupInterestModel.groupRatio.toString matches maxTwoDecimalsRegex

    if (isBetween0And100 && hasTwoDecimalPlaces) {
      adjustedGroupInterestModel.groupRatio.validNec
    } else {
      GroupRatioError(adjustedGroupInterestModel.groupRatio).invalidNec
    }
  }

  private def validateGroupRatioCalculation(implicit path: JsPath): ValidationResult[BigDecimal] = {

    (adjustedGroupInterestModel.qngie, adjustedGroupInterestModel.groupEBITDA, adjustedGroupInterestModel.groupRatio) match {
      case (_, groupEBITDA, groupRatio) if groupEBITDA <= 0 && groupRatio == 100 =>
        adjustedGroupInterestModel.groupRatio.validNec
      case (_, groupEBITDA, groupRatio) if groupEBITDA <= 0 && groupRatio != 100 =>
        NegativeOrZeroGroupEBITDAError(groupRatio).invalidNec
      case (qngie, groupEBITDA, groupRatio) if groupRatio != (qngie / groupEBITDA) =>
        GroupRatioCalculationError(qngie, groupEBITDA, groupRatio).invalidNec
      case _ => adjustedGroupInterestModel.groupRatio.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[AdjustedGroupInterestModel] =
    (validateGroupRatio,
      validateGroupRatioCalculation).mapN((_, _) => adjustedGroupInterestModel)
}

case class GroupRatioError(groupRatio: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "Group Ratio must be between 0 and 100%, and to two decimal places"
  val path = topPath \ "groupRatio"
  val value = Json.toJson(groupRatio)
}

case class GroupRatioCalculationError(qngie: BigDecimal, groupEBITDA: BigDecimal, groupRatio: BigDecimal)(implicit topPath: JsPath) extends Validation {

  val errorMessage: String = s"The value for Group Ratio Percent you provided ${groupRatio}, does not match the value calculated from the provided QNGIE ($qngie) and group-EBITDA {$groupEBITDA}, ${(qngie / groupEBITDA)}"
  val path = topPath \ "groupEBITDA"
  val value = Json.toJson(qngie, groupEBITDA, groupRatio)
}

case class NegativeOrZeroGroupEBITDAError(groupRatio: BigDecimal)(implicit topPath: JsPath) extends Validation {

  val errorMessage: String = "If group-EBITDA is negative or zero, groupRatio must be set to 100"
  val path = topPath \ "groupEBITDA"
  val value = Json.toJson(groupRatio)
}

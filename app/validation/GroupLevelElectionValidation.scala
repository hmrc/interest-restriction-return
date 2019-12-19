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
import models.{GroupLevelElectionsModel, Validation}
import play.api.libs.json.{JsPath, Json}

trait GroupLevelElectionValidation extends BaseValidation {

  import cats.implicits._

  val groupLevelElectionsModel: GroupLevelElectionsModel

  private def validateGroupRatioElection(implicit path: JsPath): ValidationResult[Boolean] = {
    groupLevelElectionsModel.isElected match {
      case true => groupLevelElectionsModel.isElected.validNec
      case false => groupLevelElectionsModel.isElected.validNec
      case _ => GroupRatioElectionError(groupLevelElectionsModel.isElected).invalidNec
    }
  }

  private def validateGroupEBITDA(implicit path: JsPath): ValidationResult[Option[Boolean]] = {
    groupLevelElectionsModel.groupEBITDAChargeableGains match {
      case Some(true) => groupLevelElectionsModel.groupEBITDAChargeableGains.validNec
      case Some(false) => groupLevelElectionsModel.groupEBITDAChargeableGains.validNec
      case _ => GroupEBITDAError(groupLevelElectionsModel.groupEBITDAChargeableGains).invalidNec
    }
  }

  private def validateAlternativeCalculation(implicit path: JsPath): ValidationResult[Option[Boolean]] = {
    groupLevelElectionsModel.interestAllowanceAlternativeCalculation match {
      case Some(true) => groupLevelElectionsModel.interestAllowanceAlternativeCalculation.validNec
      case Some(false) => groupLevelElectionsModel.interestAllowanceAlternativeCalculation.validNec
      case _ => AlternativeCalculationError(groupLevelElectionsModel.interestAllowanceAlternativeCalculation).invalidNec
    }
  }

  def validateGroupLevelElectionModel(implicit path: JsPath): ValidationResult[GroupLevelElectionsModel] =
    (validateGroupRatioElection,
      optionValidations(groupLevelElectionsModel.groupRatioBlended.map(_.validate(path \ "groupRatioBlended"))),
      validateGroupEBITDA,
      validateAlternativeCalculation,
      optionValidations(groupLevelElectionsModel.interestAllowanceNonConsolidatedInvestment.map(_.validate(path \ "nonConsolidatedInvestments"))),
      optionValidations(groupLevelElectionsModel.interestAllowanceConsolidatedPartnership.map(_.validate(path \ "consolidatedPartnership")))).mapN((_, _, _, _, _, _) => groupLevelElectionsModel)
}

case class GroupLevelElectionModelError(model: GroupLevelElectionsModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "Ultimate Parent Company Model cannot contain data for UK and NonUK fields"
  val value = Json.toJson(model)
}

case class GroupRatioElectionError(election: Boolean)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "You must choose if you wish to make an election"
  val value = Json.toJson(election)
}

case class GroupEBITDAError(ebitdaElect: Option[Boolean])(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "You must choose if you wish to make an election for the group's EBITDA"
  val value = Json.toJson(ebitdaElect)
}

case class AlternativeCalculationError(altCalcElect: Option[Boolean])(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "You must choose if you wish to make an election to make an alternative calculation for interest allowance"
  val value = Json.toJson(altCalcElect)
}







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
import models.{GroupRatioModel, Validation}
import play.api.libs.json.{JsPath, Json}

trait GroupRatioValidator extends BaseValidation {

  import cats.implicits._

  val groupRatioModel: GroupRatioModel

  private def validateGroupRatioElected(implicit path: JsPath): ValidationResult[GroupRatioModel] = {
    (groupRatioModel.isElected,  groupRatioModel.groupRatioBlended.isDefined) match {
      case (true, true) => groupRatioModel.validNec
      case (true, false) => groupRatioModel.validNec
      case (false, false) => groupRatioModel.validNec
      case _ => GroupRatioError(groupRatioModel).invalidNec
    }
  }

  private def validateGroupEBITDA(implicit path: JsPath): ValidationResult[Option[Boolean]] = {
    groupRatioModel.groupEBITDAChargeableGains match {
      case Some(true) => groupRatioModel.groupEBITDAChargeableGains.validNec
      case Some(false) => groupRatioModel.groupEBITDAChargeableGains.validNec
      case _ => GroupEBITDAError(groupRatioModel.groupEBITDAChargeableGains).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[GroupRatioModel] =
    (validateGroupRatioElected,
      validateGroupEBITDA,
      optionValidations(groupRatioModel.groupRatioBlended.map(_.validate(path \ "groupRatioBlended")))
    ).mapN((_, _, _) => groupRatioModel)
}

case class GroupRatioError(groupRatio: GroupRatioModel)(implicit val topPath: JsPath) extends Validation {
  val errorMessage: String = "Group Ratio is not elected, unable to supply GroupEBITDA and Blended Election "
  val path = topPath \ "groupRatio"
  val value = Json.toJson(groupRatio)
}

case class GroupEBITDAError(ebitdaElect: Option[Boolean])(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "You must choose if you wish to make an election for the group's EBITDA"
  val value = Json.toJson(ebitdaElect)
}




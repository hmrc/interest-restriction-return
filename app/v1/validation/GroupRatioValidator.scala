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

package v1.validation

import play.api.libs.json.{JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.{GroupRatioModel, Validation}

trait GroupRatioValidator extends BaseValidation {

  import cats.implicits._

  val groupRatioModel: GroupRatioModel

  private def validateGroupEBITDA(implicit path: JsPath): ValidationResult[Option[Boolean]] = {
    (groupRatioModel.isElected,  groupRatioModel.groupEBITDAChargeableGains.isDefined) match {
      case (false, true) => GroupEBITDASupplied(groupRatioModel.groupEBITDAChargeableGains).invalidNec
      case (true, false) => GroupEBITDANotSupplied().invalidNec
      case _ => groupRatioModel.groupEBITDAChargeableGains.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[GroupRatioModel] =
    (groupRatioModel.validNec,
      validateGroupEBITDA,
      optionValidations(groupRatioModel.groupRatioBlended.map(_.validate(path \ "groupRatioBlended")))
    ).mapN((_, _, _) => groupRatioModel)
}

case class GroupRatioBlendedSupplied(groupRatio: GroupRatioModel)(implicit val topPath: JsPath) extends Validation {
  val errorMessage: String = "Group Ratio is not elected, unable to elect GroupRatioBlended Election"
  val path = topPath \ "groupRatioBlended"
  val value = Json.toJson(groupRatio)
}

case class GroupRatioBlendedNotSupplied()(implicit val topPath: JsPath) extends Validation {
  val errorMessage: String = "Group Ratio is elected, must supply GroupRatioBlended Election"
  val path = topPath \ "groupRatioBlended"
  val value = Json.obj()
}

case class GroupEBITDASupplied(ebitdaElect: Option[Boolean])(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "Group Ratio is not elected, unable to elect groupEBITDAChargeableGains Election"
  val path = topPath \ "groupEBITDAChargeableGains"
  val value = Json.toJson(ebitdaElect)
}

case class GroupEBITDANotSupplied()(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "Group Ratio is elected, must provide groupEBITDAChargeableGains Election"
  val path = topPath \ "groupEBITDAChargeableGains"
  val value = Json.obj()
}





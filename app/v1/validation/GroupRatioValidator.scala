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

package v1.validation

import play.api.libs.json.{JsPath, JsValue, Json}
import v1.models.Validation.ValidationResult
import v1.models.{GroupRatioModel, Validation}

trait GroupRatioValidator extends BaseValidation {

  import cats.implicits.*

  val groupRatioModel: GroupRatioModel

  private def validateGroupRatioElected(implicit path: JsPath): ValidationResult[GroupRatioModel] =
    (groupRatioModel.isElected, groupRatioModel.groupRatioBlended.isDefined) match {
      case (false, true) => GroupRatioBlendedSupplied(groupRatioModel).invalidNec
      case (true, false) => GroupRatioBlendedNotSupplied().invalidNec
      case _             => groupRatioModel.validNec
    }

  def validate(implicit path: JsPath): ValidationResult[GroupRatioModel] =
    (
      validateGroupRatioElected,
      optionValidations(groupRatioModel.groupRatioBlended.map(_.validate(path \ "groupRatioBlended")))
    ).mapN((_, _) => groupRatioModel)
}

case class GroupRatioBlendedSupplied(groupRatio: GroupRatioModel)(implicit val topPath: JsPath) extends Validation {
  val code: String           = "GROUP_RATIO_BLENDED_SUPPLIED"
  val errorMessage: String   = "Group ratio not elected, so group ratio blended not needed"
  val path: JsPath           = topPath \ "groupRatioBlended"
  val value: Option[JsValue] = Some(Json.toJson(groupRatio))
}

case class GroupRatioBlendedNotSupplied()(implicit val topPath: JsPath) extends Validation {
  val code: String           = "GROUP_RATIO_BLENDED_NOT_SUPPLIED"
  val errorMessage: String   =
    "If group ratio % is elected, a value for group ratio (blended) election must be true or false"
  val path: JsPath           = topPath \ "groupRatioBlended"
  val value: Option[JsValue] = None
}

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
import models.{GroupRatioBlendedModel, Validation}
import play.api.libs.json.{JsPath, Json}

trait GroupRatioBlendedValidator extends BaseValidation {

  import cats.implicits._

  val groupRatioBlendedModel: GroupRatioBlendedModel

  private def validateGroupRatioBlended(implicit path: JsPath): ValidationResult[GroupRatioBlendedModel] = {
    (groupRatioBlendedModel.isElected,  groupRatioBlendedModel.investorGroups.isDefined) match {
      case (true, true) => groupRatioBlendedModel.validNec
      case (true, false) => groupRatioBlendedModel.validNec
      case (false, false) => groupRatioBlendedModel.validNec
      case _ => GroupRatioBlendedNotElectedError(groupRatioBlendedModel).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[GroupRatioBlendedModel] = validateGroupRatioBlended.map(_ => groupRatioBlendedModel)
}


case class GroupRatioBlendedNotElectedError(groupRatioBlended: GroupRatioBlendedModel)(implicit val topPath: JsPath) extends Validation {
  val errorMessage: String = "Group Ratio Blended is not elected, unable to supply investor groups"
  val path = topPath \ "groupRatioBlended"
  val value = Json.toJson(groupRatioBlended)
}





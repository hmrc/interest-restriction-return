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
import v1.models.{GroupRatioBlendedModel, InvestorGroupModel, Validation}

trait GroupRatioBlendedValidator extends BaseValidation {

  import cats.implicits.*

  val groupRatioBlendedModel: GroupRatioBlendedModel

  private def validateGroupRatioBlended(implicit path: JsPath): ValidationResult[GroupRatioBlendedModel] =
    (groupRatioBlendedModel.isElected, groupRatioBlendedModel.investorGroups.isDefined) match {
      case (false, true) => GroupRatioBlendedNotElectedError(groupRatioBlendedModel).invalidNec
      case _             => groupRatioBlendedModel.validNec
    }

  def validate(implicit path: JsPath): ValidationResult[GroupRatioBlendedModel] = {

    val investorGroupsValidation: ValidationResult[Option[InvestorGroupModel]] =
      optionValidations(
        groupRatioBlendedModel.investorGroups.map(investors =>
          if (investors.isEmpty) {
            InvestorGroupsEmpty().invalidNec
          } else {
            combineValidations(investors.zipWithIndex.map { case (a, i) =>
              a.validate(JsPath \ s"investorGroups[$i]")
            }*)
          }
        )
      )

    (validateGroupRatioBlended, investorGroupsValidation).mapN((_, _) => groupRatioBlendedModel)
  }
}

case class GroupRatioBlendedNotElectedError(groupRatioBlended: GroupRatioBlendedModel)(implicit val topPath: JsPath)
    extends Validation {
  val code: String           = "INVESTOR_GROUPS_SUPPLIED"
  val errorMessage: String   = "Group ratio blended not elected so supply investor group not required"
  val path: JsPath           = topPath \ "groupRatioBlended"
  val value: Option[JsValue] = Some(Json.toJson(groupRatioBlended))
}

case class InvestorGroupsEmpty()(implicit val topPath: JsPath) extends Validation {
  val code: String           = "INVESTOR_GROUPS_EMPTY"
  val errorMessage: String   = "Investor groups must have at least 1 investor"
  val path: JsPath           = topPath \ "investorGroups"
  val value: Option[JsValue] = None
}

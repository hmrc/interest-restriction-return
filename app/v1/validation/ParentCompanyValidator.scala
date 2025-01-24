/*
 * Copyright 2024 HM Revenue & Customs
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
import v1.models.{ParentCompanyModel, Validation}

trait ParentCompanyValidator extends BaseValidation {

  import cats.implicits._

  val parentCompanyModel: ParentCompanyModel

  private def validateParentCompanyCanNotBeUltimateAndDeemed(implicit
    path: JsPath
  ): ValidationResult[ParentCompanyModel] = {
    val isUltimate = parentCompanyModel.ultimateParent.isDefined
    val isDeemed   = parentCompanyModel.deemedParent.isDefined

    if (isUltimate && isDeemed) {
      ParentCompanyCanNotBeUltimateAndDeemed(parentCompanyModel).invalidNec
    } else if (!isUltimate && !isDeemed) {
      ParentCompanyBothUltimateAndDeemedEmtpty(parentCompanyModel).invalidNec
    } else {
      parentCompanyModel.validNec
    }
  }

  private def validateOnlyTwoOrThreeDeemedParents(implicit path: JsPath): ValidationResult[ParentCompanyModel] = {
    val numOfDeemedParent = parentCompanyModel.deemedParent.fold(0)(x => x.length)
    numOfDeemedParent match {
      case x if x > 3  => MaxThreeDeemedParents(parentCompanyModel).invalidNec
      case x if x == 1 => MinTwoDeemedParents(parentCompanyModel).invalidNec
      case _           => parentCompanyModel.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[ParentCompanyModel] = {

    val validatedDeemedParent = parentCompanyModel.deemedParent.map(deemedParents =>
      if (deemedParents.isEmpty) {
        DeemedParentsEmpty().invalidNec
      } else {
        combineValidations(deemedParents.zipWithIndex.map { case (x, i) =>
          x.validate(path \ s"deemedParent[$i]")
        }*)
      }
    )

    (
      validateParentCompanyCanNotBeUltimateAndDeemed,
      validateOnlyTwoOrThreeDeemedParents,
      optionValidations(parentCompanyModel.ultimateParent.map(_.validate(path \ "ultimateParent"))),
      optionValidations(validatedDeemedParent)
    ).mapN((_, _, _, _) => parentCompanyModel)
  }
}

case class ParentCompanyCanNotBeUltimateAndDeemed(model: ParentCompanyModel)(implicit val path: JsPath)
    extends Validation {
  val code: String           = "PARENT_ULTIMATE_AND_DEEMED"
  val errorMessage: String   = "Parent company must be either ultimate or deemed parent"
  val value: Option[JsValue] = Some(Json.toJson(model))
}

case class ParentCompanyBothUltimateAndDeemedEmtpty(model: ParentCompanyModel)(implicit val path: JsPath)
    extends Validation {
  val code: String           = "ULTIMATE_AND_DEEMED_EMPTY"
  val errorMessage: String   = "Parent company must be either ultimate or deemed parent"
  val value: Option[JsValue] = Some(Json.toJson(model))
}

case class DeemedParentsEmpty()(implicit topPath: JsPath) extends Validation {
  val code: String           = "DEEMED_EMPTY"
  val errorMessage: String   = "Add at least 1 deemed parent"
  val path: JsPath           = topPath \ "deemedParent"
  val value: Option[JsValue] = None
}

case class MinTwoDeemedParents(model: ParentCompanyModel)(implicit topPath: JsPath) extends Validation {
  val code: String           = "DEEMED_MIN"
  val errorMessage: String   = "Minimum number of deemed parents is 2"
  val path: JsPath           = topPath \ "deemedParent"
  val value: Option[JsValue] = None
}

case class MaxThreeDeemedParents(model: ParentCompanyModel)(implicit topPath: JsPath) extends Validation {
  val code                   = "DEEMED_MAX"
  val errorMessage: String   = "Maximum number of deemed parents is 3"
  val path: JsPath           = topPath \ "deemedParent"
  val value: Option[JsValue] = None
}

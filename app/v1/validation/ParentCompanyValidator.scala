/*
 * Copyright 2020 HM Revenue & Customs
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
import v1.models.{ParentCompanyModel, Validation}

trait ParentCompanyValidator extends BaseValidation {

  import cats.implicits._

  val parentCompanyModel: ParentCompanyModel

  private def validateParentCompanyCanNotBeUltimateAndDeemed(implicit path: JsPath): ValidationResult[ParentCompanyModel] = {
    val isUltimate = parentCompanyModel.ultimateParent.isDefined
    val isDeemed = parentCompanyModel.deemedParent.isDefined

    if(isUltimate && isDeemed) {
      ParentCompanyCanNotBeUltimateAndDeemed(parentCompanyModel).invalidNec
    } else {
      parentCompanyModel.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[ParentCompanyModel] = {

    val validatedDeemedParent = parentCompanyModel.deemedParent.map(deemedParents =>
      if(deemedParents.isEmpty) DeemedParentsEmpty().invalidNec else {
        combineValidations(deemedParents.zipWithIndex.map {
          case (x, i) => x.validate(path \ s"deemedParent[$i]")
        }: _*)
      })

    (validateParentCompanyCanNotBeUltimateAndDeemed,
      optionValidations(parentCompanyModel.ultimateParent.map(_.validate(path \ "ultimateParent"))),
      optionValidations(validatedDeemedParent)
    ).mapN((_, _, _) => parentCompanyModel)
  }
}

case class ParentCompanyCanNotBeUltimateAndDeemed(model: ParentCompanyModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "Parent Company Model cannot contain data for Ultimate and Deemed fields"
  val value = Json.toJson(model)
}

case class DeemedParentsEmpty(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "deemedParent must have at least 1 deemed parent if supplied"
  val path = topPath \ "deemedParent"
  val value = Json.obj()
}








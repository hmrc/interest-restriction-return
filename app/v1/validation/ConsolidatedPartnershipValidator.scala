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

import play.api.libs.json.{Json, JsPath}
import v1.models.Validation.ValidationResult
import v1.models.{ConsolidatedPartnershipModel, PartnershipModel, Validation}

trait ConsolidatedPartnershipValidator extends BaseValidation {

  import cats.implicits._

  val consolidatedPartnershipModel: ConsolidatedPartnershipModel

  private def validateConsolidatedPartnershipModel(implicit path: JsPath): ValidationResult[ConsolidatedPartnershipModel] = {
    (consolidatedPartnershipModel.isElected || consolidatedPartnershipModel.isActive, consolidatedPartnershipModel.consolidatedPartnerships.isDefined) match {
      case (false, true) => ConsolidatedPartnershipsSupplied(consolidatedPartnershipModel).invalidNec
      case (true, false) => ConsolidatedPartnershipsNotSupplied(consolidatedPartnershipModel).invalidNec
      case _ => consolidatedPartnershipModel.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[ConsolidatedPartnershipModel] = {

    val consolidatedPartnershipsValidation: ValidationResult[Option[PartnershipModel]] =
      optionValidations(consolidatedPartnershipModel.consolidatedPartnerships.map(consolidatedPartnerships =>
        if(consolidatedPartnerships.isEmpty) ConsolidatedPartnershipsEmpty().invalidNec else {
          combineValidations(consolidatedPartnerships.zipWithIndex.map {
            case (a, i) => a.validate(JsPath \ s"consolidatedPartnerships[$i]")
          }: _*)
        }
      ))

    (validateConsolidatedPartnershipModel,
      consolidatedPartnershipsValidation).mapN((_,_) => consolidatedPartnershipModel)
  }
}

case class ConsolidatedPartnershipsSupplied(consolidatedPartnershipModel: ConsolidatedPartnershipModel)(implicit val topPath: JsPath) extends Validation {
  val code = "PARTNERSHIPS_SUPPLIED"
  val errorMessage: String = "Interest allowance (non-consolidated partnerships) election not made, so no details of non-consolidated partnership needed"
  val path: JsPath = topPath \ "consolidatedPartnership"
  val value = Some(Json.toJson(consolidatedPartnershipModel))
}

case class ConsolidatedPartnershipsNotSupplied(consolidatedPartnershipModel: ConsolidatedPartnershipModel)(implicit val topPath: JsPath) extends Validation {
  val code = "PARTNERSHIPS_NOT_SUPPLIED"
  val errorMessage: String = "Interest allowance (non-consolidated partnerships) election made, enter details of at least 1 non-consolidated partnership"
  val path: JsPath = topPath \ "consolidatedPartnership"
  val value = Some(Json.toJson(consolidatedPartnershipModel))
}

case class ConsolidatedPartnershipsEmpty()(implicit val topPath: JsPath) extends Validation {
  val code = "PARTNERSHIPS_EMPTY"
  val errorMessage: String = "Interest allowance (non-consolidated partnerships) election made, enter details of at least 1 non-consolidated partnership"
  val path: JsPath = topPath \ "consolidatedPartnership"
  val value = None
}






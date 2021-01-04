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
import v1.models.{DeemedParentModel, Validation}

trait DeemedParentValidator extends BaseValidation {

  import cats.implicits._

  val deemedParentModel: DeemedParentModel

  private def validateCorrectCompanyDetailsSupplied(implicit path: JsPath): ValidationResult[DeemedParentModel] = {
    val ctutr = deemedParentModel.ctutr.isDefined
    val sautr = deemedParentModel.sautr.isDefined
    val countryCode = deemedParentModel.countryOfIncorporation.isDefined
    (ctutr, sautr, countryCode) match {
      case (true, true, true) => DeemedParentWrongDetailsSuppliedError(deemedParentModel).invalidNec
      case (true, true, false) => WrongDeemedParentIsUkCompanyAndPartnership(deemedParentModel).invalidNec
      case (true, false, true) => WrongDeemedParentIsUKCompanyAndNonUK(deemedParentModel).invalidNec
      case (false, true, true) => WrongDeemedParentIsUkPartnershipAndNonUKCompany(deemedParentModel).invalidNec
      case _ => deemedParentModel.validNec
    }
  }


  def validate(implicit path: JsPath): ValidationResult[DeemedParentModel] =
    (validateCorrectCompanyDetailsSupplied,
      deemedParentModel.companyName.validate(path \ "companyName"),
      optionValidations(deemedParentModel.ctutr.map(_.validate(path \ "ctutr"))),
      optionValidations(deemedParentModel.countryOfIncorporation.map(_.validate(path \ "countryOfIncorporation")))
      ).mapN((_, _, _, _) => deemedParentModel)
}


case class DeemedParentWrongDetailsSuppliedError(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given the details for all three ultimate parents please give correct details"
  val value = Json.toJson(model)
}

case class WrongDeemedParentIsUkCompanyAndPartnership(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given details for a UK Company and Partnership"
  val value = Json.toJson(model)
}

case class WrongDeemedParentIsUkPartnershipAndNonUKCompany(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given details for a UK Partnership and NonUK Company"
  val value = Json.toJson(model)
}

case class WrongDeemedParentIsUKCompanyAndNonUK(model: DeemedParentModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "you have given details for a UK and Non UK Company"
  val value = Json.toJson(model)
}









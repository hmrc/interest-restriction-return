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
import v1.models.{NonConsolidatedInvestmentModel, Validation}

trait NonConsolidatedInvestmentValidator extends BaseValidation {

  import cats.implicits._

  val nonConsolidatedInvestmentModel: NonConsolidatedInvestmentModel

  private def validateInvestmentName(implicit path: JsPath): ValidationResult[String] = {
    if (nonConsolidatedInvestmentModel.investmentName.length >= 1 && nonConsolidatedInvestmentModel.investmentName.length <= 5000) {
      nonConsolidatedInvestmentModel.investmentName.validNec
    } else {
      NonConsolidatedInvestmentNameLengthError(nonConsolidatedInvestmentModel.investmentName).invalidNec
    }
  }

  private def validateCompanyNameCharacters(implicit path: JsPath): ValidationResult[String] = {
    val regex = "^[ -~¢-¥©®±×÷‐₠-₿−-∝≈≠≣-≥]*$".r
    nonConsolidatedInvestmentModel.investmentName match {
      case regex(_ *) => nonConsolidatedInvestmentModel.investmentName.validNec
      case _ => NonConsolidatedInvestmentNameCharacterError(nonConsolidatedInvestmentModel.investmentName).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[NonConsolidatedInvestmentModel] = {
    (validateInvestmentName, validateCompanyNameCharacters).mapN((_, _) => nonConsolidatedInvestmentModel)
  }
}

case class NonConsolidatedInvestmentNameLengthError(investmentName: String)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = s"Investment name is ${investmentName.length} characters long and should be between 1 and 5000 characters"
  val path = topPath \ "revisedReturnDetails"
  val value = Json.toJson(investmentName)
}

case class NonConsolidatedInvestmentNameCharacterError(investmentName: String)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "Investment name contains invalid characters"
  val path = topPath \ "revisedReturnDetails"
  val value = Json.toJson(investmentName)
}







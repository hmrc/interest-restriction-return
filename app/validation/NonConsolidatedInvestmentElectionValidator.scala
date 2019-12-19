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
import models.{NonConsolidatedInvestmentElectionModel, Validation}
import play.api.libs.json.{JsPath, Json}

trait NonConsolidatedInvestmentElectionValidator extends BaseValidation {

  import cats.implicits._

  val nonConsolidatedInvestmentElectionModel: NonConsolidatedInvestmentElectionModel

  private def validateNonConsolidatedInvestment(implicit path: JsPath): ValidationResult[NonConsolidatedInvestmentElectionModel] = {
    (nonConsolidatedInvestmentElectionModel.isElected, nonConsolidatedInvestmentElectionModel.nonConsolidatedInvestments.isDefined) match {
      case (true, true) => nonConsolidatedInvestmentElectionModel.validNec
      case (true, false) => nonConsolidatedInvestmentElectionModel.validNec
      case (false, false) => nonConsolidatedInvestmentElectionModel.validNec
      case _ => NonConsolidatedInvestmentElectionError(nonConsolidatedInvestmentElectionModel).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[NonConsolidatedInvestmentElectionModel] = validateNonConsolidatedInvestment.map(_ => nonConsolidatedInvestmentElectionModel)
}

case class NonConsolidatedInvestmentElectionError(nonConsolidatedInvestmentElectionModel: NonConsolidatedInvestmentElectionModel)(implicit val topPath: JsPath) extends Validation {
  val errorMessage: String = "You can only provide non-consolidated investments if non-consolidated election is made"
  val path = topPath \ "nonConsolidatedInvestments"
  val value = Json.toJson(nonConsolidatedInvestmentElectionModel)
}






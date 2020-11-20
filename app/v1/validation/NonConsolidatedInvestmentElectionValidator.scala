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
import v1.models.{NonConsolidatedInvestmentElectionModel, NonConsolidatedInvestmentModel, Validation}

trait NonConsolidatedInvestmentElectionValidator extends BaseValidation {

  import cats.implicits._

  val nonConsolidatedInvestmentElectionModel: NonConsolidatedInvestmentElectionModel

  private def validateNonConsolidatedInvestment(implicit path: JsPath): ValidationResult[NonConsolidatedInvestmentElectionModel] = {
    (nonConsolidatedInvestmentElectionModel.isElected, nonConsolidatedInvestmentElectionModel.nonConsolidatedInvestments.isDefined) match {
      case (false, true) => NonConsolidatedInvestmentSupplied(nonConsolidatedInvestmentElectionModel).invalidNec
      case (true, false) => NonConsolidatedInvestmentNotSupplied(nonConsolidatedInvestmentElectionModel).invalidNec
      case _ => nonConsolidatedInvestmentElectionModel.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[NonConsolidatedInvestmentElectionModel] = {

    val nonConsolidatedInvestmentsValidation: ValidationResult[Option[NonConsolidatedInvestmentModel]] =
      optionValidations(nonConsolidatedInvestmentElectionModel.nonConsolidatedInvestments.map(nonConsolidatedInvestments =>
        if(nonConsolidatedInvestments.isEmpty) NonConsolidatedInvestmentEmpty().invalidNec else {
          combineValidations(nonConsolidatedInvestments.zipWithIndex.map {
            case (a, i) => a.validate(JsPath \ s"nonConsolidatedInvestments[$i]")
          }: _*)
        }
      ))

    (validateNonConsolidatedInvestment,
      nonConsolidatedInvestmentsValidation).mapN((_,_) => nonConsolidatedInvestmentElectionModel)
  }
}

case class NonConsolidatedInvestmentSupplied(nonConsolidatedInvestmentElectionModel: NonConsolidatedInvestmentElectionModel)
                                            (implicit val topPath: JsPath) extends Validation {
  val errorMessage: String = "You can only provide non-consolidated investments if non-consolidated election is made"
  val path = topPath \ "nonConsolidatedInvestments"
  val value = Json.toJson(nonConsolidatedInvestmentElectionModel)
}

case class NonConsolidatedInvestmentNotSupplied(nonConsolidatedInvestmentElectionModel: NonConsolidatedInvestmentElectionModel)
                                               (implicit val topPath: JsPath) extends Validation {
  val errorMessage: String = "You must provide non-consolidated investments if non-consolidated election is made"
  val path = topPath \ "nonConsolidatedInvestments"
  val value = Json.toJson(nonConsolidatedInvestmentElectionModel)
}

case class NonConsolidatedInvestmentEmpty()(implicit val topPath: JsPath) extends Validation {
  val errorMessage: String = "nonConsolidatedInvestments must contain at least 1 value if supplied"
  val path = topPath \ "nonConsolidatedInvestments"
  val value = Json.obj()
}






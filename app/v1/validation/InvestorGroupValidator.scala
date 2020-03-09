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
import v1.models.{InvestorGroupModel, Validation}

trait InvestorGroupValidator extends BaseValidation {

  import cats.implicits._

  val investorGroupModel: InvestorGroupModel

  private def validateInvestorName(implicit path: JsPath): ValidationResult[String] = {
    if (investorGroupModel.groupName.length >= 1 && investorGroupModel.groupName.length <= 32767) {
      investorGroupModel.groupName.validNec
    } else {
      InvestorNameError(investorGroupModel.groupName).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[InvestorGroupModel] = {
    validateInvestorName.map(_ => investorGroupModel)
  }
}

case class InvestorNameError(investorName: String)(implicit val topPath: JsPath) extends Validation {
  val code = INVALID_LENGTH
  val message: String = "Investor Names must be between 1 and 32767 characters"
  val path = topPath \ "investorGroups"
}






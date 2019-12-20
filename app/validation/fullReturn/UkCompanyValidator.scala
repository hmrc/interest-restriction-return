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

package validation.fullReturn

import models.Validation.ValidationResult
import models.Validation
import models.fullReturn.UkCompanyModel
import play.api.libs.json.{JsPath, Json}
import validation.BaseValidation

trait UkCompanyValidator extends BaseValidation {

  import cats.implicits._

  val ukCompany: UkCompanyModel

  private def validateNetTaxInterestExpense(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val expense = ukCompany.netTaxInterestExpense
    if(expense < 0) {
    NetTaxInterestExpenseError(ukCompany.netTaxInterestExpense).invalidNec
        } else {
      ukCompany.netTaxInterestExpense.validNec
    }
  }

  private def validateNetTaxInterestIncome(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val income = ukCompany.netTaxInterestIncome
    if(income < 0) {
      NetTaxInterestIncomeError(ukCompany.netTaxInterestIncome).invalidNec
    } else {
      ukCompany.netTaxInterestIncome.validNec
    }
  }

  private def validateExpenseAndIncomeBothNotGreaterThanZero(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val expense = ukCompany.netTaxInterestExpense
    val income = ukCompany.netTaxInterestIncome

    if(expense != 0 && income != 0) {
      ExpenseAndIncomeBothNotGreaterThanZero(ukCompany.netTaxInterestExpense, ukCompany.netTaxInterestIncome).invalidNec
    } else
      {
        ukCompany.netTaxInterestIncome.validNec
      }
  }

  def validate(implicit path: JsPath): ValidationResult[UkCompanyModel] =
    (ukCompany.utr.validate(path \ "utr"),
      ukCompany.companyName.validate(path \ "companyName"),
      validateNetTaxInterestExpense,
      validateNetTaxInterestIncome,
      validateExpenseAndIncomeBothNotGreaterThanZero,
      optionValidations(ukCompany.allocatedRestrictions.map(_.validate(path \ "allocatedRestrictions"))),
      optionValidations(ukCompany.allocatedReactivations.map(_.validate(path \ "allocatedReactivations")))
      ).mapN((_,_,_,_,_,_,_) => ukCompany)
}

case class NetTaxInterestExpenseError(netTaxInterestExpense: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val path = topPath \ "netTaxInterestExpense"
  val errorMessage: String = "The supplied Net Tax Interest Expense is Negative, which it can not be."
  val value = Json.toJson(netTaxInterestExpense)
}

case class NetTaxInterestIncomeError(netTaxInterestIncome: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val path = topPath \ "netTaxInterestIncome"
  val errorMessage: String = "The supplied Net Tax Interest Income is Negative, which it can not be."
  val value = Json.toJson(netTaxInterestIncome)
}

case class ExpenseAndIncomeBothNotGreaterThanZero(netTaxInterestExpense: BigDecimal,netTaxInterestIncome: BigDecimal)
                                                 (implicit val path: JsPath) extends Validation {
  val errorMessage: String = "UK Company Model cannot contain Positive for Expense and Income fields"
  val value = Json.toJson(netTaxInterestExpense,netTaxInterestIncome)
}
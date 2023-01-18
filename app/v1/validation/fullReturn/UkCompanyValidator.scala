/*
 * Copyright 2023 HM Revenue & Customs
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

package v1.validation.fullReturn

import play.api.libs.json.{JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.fullReturn.UkCompanyModel
import v1.models.{AccountingPeriodModel, Validation}
import v1.validation.BaseValidation

trait UkCompanyValidator extends BaseValidation {

  import cats.implicits._

  val ukCompany: UkCompanyModel

  private def validateNetTaxInterestExpense(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val expense = ukCompany.netTaxInterestExpense
    if (expense < 0) {
      NetTaxInterestExpenseError(ukCompany.netTaxInterestExpense).invalidNec
    } else {
      ukCompany.netTaxInterestExpense.validNec
    }
  }

  private def validateNetTaxInterestExpenseDecimalPlaces(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val income = ukCompany.netTaxInterestExpense
    if (income % 0.01 != 0) {
      NetTaxInterestExpenseDecimalError(ukCompany.netTaxInterestExpense).invalidNec
    } else {
      ukCompany.netTaxInterestExpense.validNec
    }
  }

  private def validateNetTaxInterestIncome(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val income = ukCompany.netTaxInterestIncome
    if (income < 0) {
      NetTaxInterestIncomeError(ukCompany.netTaxInterestIncome).invalidNec
    } else {
      ukCompany.netTaxInterestIncome.validNec
    }
  }

  private def validateNetTaxInterestIncomeDecimalPlaces(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val income = ukCompany.netTaxInterestIncome
    if (income % 0.01 != 0) {
      NetTaxInterestIncomeDecimalError(ukCompany.netTaxInterestIncome).invalidNec
    } else {
      ukCompany.netTaxInterestIncome.validNec
    }
  }

  private def validateExpenseAndIncomeBothNotGreaterThanZero(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val expense = ukCompany.netTaxInterestExpense
    val income  = ukCompany.netTaxInterestIncome

    if (expense != 0 && income != 0) {
      ExpenseAndIncomeBothNotGreaterThanZero(ukCompany.netTaxInterestExpense, ukCompany.netTaxInterestIncome).invalidNec
    } else {
      ukCompany.netTaxInterestIncome.validNec
    }
  }

  private def validateRestrictionNotGreaterThanExpense(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val netExpense                       = ukCompany.netTaxInterestExpense
    val allocatedRestriction: BigDecimal = ukCompany.allocatedRestrictions.fold[BigDecimal](0) { restrictions =>
      restrictions.totalDisallowances
    }
    if (allocatedRestriction > netExpense) {
      RestrictionNotGreaterThanExpense(netExpense, allocatedRestriction).invalidNec
    } else {
      netExpense.validNec
    }
  }

  private def validateNoTotalNetTaxInterestIncomeDuringRestriction(implicit
    path: JsPath
  ): ValidationResult[BigDecimal] = {
    val netTaxIncome: BigDecimal       = ukCompany.netTaxInterestIncome
    val restrictionApplied: BigDecimal = ukCompany.allocatedRestrictions.fold[BigDecimal](0) { restriction =>
      restriction.totalDisallowances
    }
    if (netTaxIncome > 0 && restrictionApplied > 0) {
      NoTotalNetTaxInterestIncomeDuringRestriction(netTaxIncome).invalidNec
    } else {
      netTaxIncome.validNec

    }
  }

  private def validateTaxEBITDADecimalPlaces(implicit path: JsPath): ValidationResult[BigDecimal] = {
    val income = ukCompany.taxEBITDA
    if (income % 0.01 != 0) {
      TaxEBITDADecimalError(ukCompany.taxEBITDA).invalidNec
    } else {
      ukCompany.taxEBITDA.validNec
    }
  }

  private def companyEstimateReasonIsValidLength(reason: String): Boolean = reason.length >= 1 && reason.length <= 5000

  private def companyEstimateReasonHasValidCharacters(reason: String): Boolean = {
    val regex = "^[ -~¢-¥©®±×÷‐₠-₿−-∝≈≠≣-≥]*$".r
    reason match {
      case regex(_*) => true
      case _         => false
    }
  }

  private def validateCompanyEstimateReason(implicit path: JsPath): ValidationResult[Option[String]] =
    ukCompany.companyEstimateReason match {
      case Some(reason) if !companyEstimateReasonIsValidLength(reason)      =>
        CompanyEstimateReasonLengthError(reason).invalidNec
      case Some(reason) if !companyEstimateReasonHasValidCharacters(reason) =>
        CompanyEstimateReasonCharacterError(reason).invalidNec
      case _                                                                => ukCompany.companyEstimateReason.validNec
    }

  def validate(groupAccountingPeriod: AccountingPeriodModel)(implicit path: JsPath): ValidationResult[UkCompanyModel] =
    (
      ukCompany.utr.validate(path \ "utr"),
      ukCompany.companyName.validate(path \ "companyName"),
      validateNetTaxInterestExpense,
      validateNetTaxInterestExpenseDecimalPlaces,
      validateNetTaxInterestIncome,
      validateNetTaxInterestIncomeDecimalPlaces,
      validateExpenseAndIncomeBothNotGreaterThanZero,
      validateRestrictionNotGreaterThanExpense,
      validateNoTotalNetTaxInterestIncomeDuringRestriction,
      optionValidations(
        ukCompany.allocatedRestrictions.map(_.validate(groupAccountingPeriod)(path \ "allocatedRestrictions"))
      ),
      optionValidations(ukCompany.allocatedReactivations.map(_.validate(path \ "allocatedReactivations"))),
      validateTaxEBITDADecimalPlaces,
      validateCompanyEstimateReason
    ).mapN((_, _, _, _, _, _, _, _, _, _, _, _, _) => ukCompany)
}

case class NetTaxInterestExpenseError(netTaxInterestExpense: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code                 = "NEGATIVE_EXPENSE"
  val path                 = topPath \ "netTaxInterestExpense"
  val errorMessage: String = "Net tax interest expense must be a positive number"
  val value                = Some(Json.toJson(netTaxInterestExpense))
}

case class NetTaxInterestExpenseDecimalError(netTaxInterestExpense: BigDecimal)(implicit topPath: JsPath)
    extends Validation {
  val code                 = "EXPENSE_DECIMAL"
  val path                 = topPath \ "netTaxInterestExpense"
  val errorMessage: String = "Net tax interest expense must be to 2 decimal places or less"
  val value                = Some(Json.toJson(netTaxInterestExpense))
}

case class NetTaxInterestIncomeError(netTaxInterestIncome: BigDecimal)(implicit topPath: JsPath) extends Validation {
  val code                 = "NEGATIVE_INCOME"
  val path                 = topPath \ "netTaxInterestIncome"
  val errorMessage: String = "Net tax interest income must be a positive number"
  val value                = Some(Json.toJson(netTaxInterestIncome))
}

case class NetTaxInterestIncomeDecimalError(netTaxInterestIncome: BigDecimal)(implicit topPath: JsPath)
    extends Validation {
  val code                 = "INCOME_DECIMAL"
  val path                 = topPath \ "netTaxInterestIncome"
  val errorMessage: String = "Net tax interest income must be to 2 decimal places or less"
  val value                = Some(Json.toJson(netTaxInterestIncome))
}

case class ExpenseAndIncomeBothNotGreaterThanZero(netTaxInterestExpense: BigDecimal, netTaxInterestIncome: BigDecimal)(
  implicit val path: JsPath
) extends Validation {
  val code                 = "EXPENSE_AND_INCOME"
  val errorMessage: String = "A company cannot have both net tax interest expense and net tax interest income"
  val value                = Some(
    Json.toJson(("netTaxInterestExpense: " + netTaxInterestExpense, "netTaxInterestIncome: " + netTaxInterestIncome))
  )
}

case class RestrictionNotGreaterThanExpense(netTaxInterestExpense: BigDecimal, allocatedRestriction: BigDecimal)(
  implicit val path: JsPath
) extends Validation {
  val code                 = "RESTRICTION_GREATER_THAN_EXPENSE"
  val errorMessage: String = "A company cannot have a restriction greater than its net tax-interest expense"
  val value                = Some(
    Json.toJson(("netTaxInterestExpense: " + netTaxInterestExpense, "allocatedRestriction: " + allocatedRestriction))
  )
}

case class NoTotalNetTaxInterestIncomeDuringRestriction(netTaxIncome: BigDecimal)(implicit val path: JsPath)
    extends Validation {
  val code                 = "INCOME_AND_RESTRICTION"
  val errorMessage: String = s"A company with net tax-interest income cannot have a restriction allocated to it"
  val value                = None
}

case class TaxEBITDADecimalError(netTaxIncome: BigDecimal)(implicit val topPath: JsPath) extends Validation {
  val code                 = "EBITDA_DECIMAL"
  val path                 = topPath \ "EBITDA"
  val errorMessage: String = s"Tax EBITDA must be to 2 decimal places or less"
  val value                = Some(Json.toJson(netTaxIncome))
}

case class CompanyEstimateReasonLengthError(reason: String)(implicit val topPath: JsPath) extends Validation {
  val code                 = "COMPANY_REASON_LENGTH"
  val errorMessage: String = s"Estimate reason must be between 1 and 5,000 characters"
  val path: JsPath         = topPath \ "companyEstimateReason"
  val value                = Some(Json.toJson(reason))
}

case class CompanyEstimateReasonCharacterError(reason: String)(implicit val topPath: JsPath) extends Validation {
  val code                 = "COMPANY_REASON_CHARACTERS"
  val errorMessage: String = "Company estimate reason contains invalid characters"
  val path: JsPath         = topPath \ "companyEstimateReason"
  val value                = Some(Json.toJson(reason))
}

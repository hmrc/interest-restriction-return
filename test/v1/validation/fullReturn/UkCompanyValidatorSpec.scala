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

import assets.fullReturn.AllocatedRestrictionsConstants._
import assets.fullReturn.UkCompanyConstants._
import play.api.libs.json.JsPath
import v1.models.fullReturn.AllocatedRestrictionsModel
import v1.models.{AccountingPeriodModel, CompanyNameModel, UTRModel}
import v1.validation
import v1.validation._

class UkCompanyValidatorSpec extends BaseValidationSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  private val groupAccountingPeriod: AccountingPeriodModel = AccountingPeriodModel(
    startDate = ap1EndDate.minusDays(1),
    endDate = ap3EndDate
  )

  private val allocatedRestriction: Option[AllocatedRestrictionsModel] = Some(
    allocatedRestrictionsModel.copy(disallowanceAp1 = 10.0, disallowanceAp2 = Some(10.0), disallowanceAp3 = Some(10.01))
  )

  "UkCompanyValidator" should {

    "Return valid" when {

      "a valid Full Reactivation UK Company model is validated" in {
        val model = ukCompanyModelReactivationMax

        rightSide(model.validate(groupAccountingPeriod)) shouldBe model
      }

      "a valid Full Restriction UK Company model is validated" in {
        rightSide(ukCompanyModelRestrictionMax.validate(groupAccountingPeriod)) shouldBe ukCompanyModelRestrictionMax
      }
    }

    "Return invalid" when {

      "CTUTR is invalid" in {
        leftSideError(
          ukCompanyModelReactivationMax.copy(utr = invalidUtr).validate(groupAccountingPeriod)
        ).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "CTUTR is empty" in {
        leftSideError(
          ukCompanyModelReactivationMax.copy(utr = UTRModel("")).validate(groupAccountingPeriod)
        ).errorMessage shouldBe UTRLengthError(UTRModel("")).errorMessage
      }

      "CTUTR is to short" in {
        leftSideError(
          ukCompanyModelReactivationMax.copy(utr = UTRModel("1")).validate(groupAccountingPeriod)
        ).errorMessage shouldBe UTRLengthError(invalidShortUtr).errorMessage
      }

      "CTUTR is to long" in {
        leftSideError(
          ukCompanyModelReactivationMax.copy(utr = UTRModel("11234567890")).validate(groupAccountingPeriod)
        ).errorMessage shouldBe UTRLengthError(invalidLongUtr).errorMessage
      }

      "CTUTR contains invalid characters" in {
        leftSideError(
          ukCompanyModelReactivationMax.copy(utr = UTRModel("ʰʲʺ£$%˦˫qw")).validate(groupAccountingPeriod)
        ).errorMessage shouldBe UTRChecksumError(UTRModel("ʰʲʺ£$%˦˫qw")).errorMessage
      }

      "CompanyName is invalid when too long" in {
        leftSideError(
          ukCompanyModelReactivationMax.copy(companyName = companyNameTooLong).validate(groupAccountingPeriod)
        ).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }

      "CompanyName is invalid when empty" in {
        leftSideError(
          ukCompanyModelReactivationMax.copy(companyName = CompanyNameModel("")).validate(groupAccountingPeriod)
        ).errorMessage shouldBe CompanyNameLengthError(companyNameIsZero.name).errorMessage
      }

      "CompanyName is invalid when containing invalid characters" in {
        leftSideError(
          ukCompanyModelReactivationMax
            .copy(companyName = CompanyNameModel("ʰʲʺ£$%˦˫qw"))
            .validate(groupAccountingPeriod)
        ).errorMessage shouldBe CompanyNameCharactersError("ʰʲʺ£$%˦˫qw").errorMessage
      }

      "netTaxInterestExpense is < 0" in {
        leftSideError(
          ukCompanyModelReactivationMax.copy(netTaxInterestExpense = -1).validate(groupAccountingPeriod)
        ).errorMessage shouldBe NetTaxInterestExpenseError(-1).errorMessage
      }

      "netTaxInterestExpense is >2 DP" in {
        leftSideError(
          ukCompanyModelReactivationMax
            .copy(netTaxInterestExpense = 2.222, netTaxInterestIncome = 0)
            .validate(groupAccountingPeriod)
        ).errorMessage shouldBe NetTaxInterestExpenseDecimalError(2.222).errorMessage
      }

      "netTaxInterestIncomes is < 0" in {
        leftSideError(
          ukCompanyModelReactivationMax.copy(netTaxInterestIncome = -1).validate(groupAccountingPeriod)
        ).errorMessage shouldBe NetTaxInterestIncomeError(-1).errorMessage
      }

      "netTaxInterestIncome is >2 DP" in {
        leftSideError(
          ukCompanyModelReactivationMax.copy(netTaxInterestIncome = 2.222).validate(groupAccountingPeriod)
        ).errorMessage shouldBe NetTaxInterestIncomeDecimalError(2.222).errorMessage
      }

      "ExpenseAndIncomeBothNotGreaterThanZero where both values are > 0" in {
        leftSideError(
          ukCompanyModelReactivationMax
            .copy(netTaxInterestExpense = 20.00, netTaxInterestIncome = 30.00)
            .validate(groupAccountingPeriod)
        ).errorMessage shouldBe ExpenseAndIncomeBothNotGreaterThanZero(20.00, 30.00).errorMessage
      }

      "RestrictionNotGreaterThanExpense where restriction values > expense" in {
        leftSideError(
          ukCompanyModelRestrictionMax
            .copy(netTaxInterestExpense = 20.00, allocatedRestrictions = allocatedRestriction)
            .validate(groupAccountingPeriod)
        ).errorMessage shouldBe RestrictionNotGreaterThanExpense(20.00, 30.01).errorMessage
      }

      "taxEBITDA is >2 DP" in {
        leftSideError(
          ukCompanyModelReactivationMax.copy(taxEBITDA = 2.222).validate(groupAccountingPeriod)
        ).errorMessage shouldBe TaxEBITDADecimalError(2.222).errorMessage
      }

      "companyEstimateReason contains more than 5,000 characters" in {
        val estimateReason = "a" * 5001
        leftSideError(
          ukCompanyModelReactivationMax
            .copy(companyEstimateReason = Some(estimateReason))
            .validate(groupAccountingPeriod)
        ).errorMessage shouldBe CompanyEstimateReasonLengthError(estimateReason).errorMessage
      }

      "companyEstimateReason contains 0 characters" in {
        val estimateReason = ""
        leftSideError(
          ukCompanyModelReactivationMax
            .copy(companyEstimateReason = Some(estimateReason))
            .validate(groupAccountingPeriod)
        ).errorMessage shouldBe CompanyEstimateReasonLengthError(estimateReason).errorMessage
      }

      "companyEstimateReason contains invalid characters" in {
        val estimateReason = "ʰʲʺ£$%˦˫qwNew!£$%^&*()_ComPan\n with spacs Ā to ʯ, Ḁ to ỿ :' ₠ to ₿ Å and K lenth is " +
          "160 no numbers allowed New!£$%^&*()_ComPany with spaces Ā to ʯ, Ḁ to ỿ"

        leftSideError(
          ukCompanyModelReactivationMax
            .copy(companyEstimateReason = Some(estimateReason))
            .validate(groupAccountingPeriod)
        ).errorMessage shouldBe CompanyEstimateReasonCharacterError(estimateReason).errorMessage
      }

      "Passing true and CompanyName invalid characters should not succeed" in {
        leftSideError(
          ukCompanyModelReactivationMax
            .copy(companyName = CompanyNameModel("ʰʲʺ˦˫˥ʺ˦˫˥"))
            .validate(groupAccountingPeriod)
        ).errorMessage shouldBe validation.CompanyNameCharactersError("ʰʲʺ˦˫˥ʺ˦˫˥").errorMessage
      }
    }
  }
}

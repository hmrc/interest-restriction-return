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

package v1.validation.fullReturn

import assets.fullReturn.AllocatedRestrictionsConstants.{ap1End, ap3End}
import assets.fullReturn.UkCompanyConstants._
import play.api.libs.json.JsPath
import v1.models.AccountingPeriodModel
import v1.validation.{BaseValidationSpec, CompanyNameLengthError, UTRChecksumError}

class UkCompanyValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  val groupAccountingPeriod = AccountingPeriodModel(
    startDate = ap1End.minusDays(1),
    endDate = ap3End
  )

  "Full UK Company Validation" should {

    "Return valid" when {

      "a valid Full UK Company model is validated" in {
        rightSide(ukCompanyModelReactivationMax.validate(groupAccountingPeriod)) shouldBe ukCompanyModelReactivationMax
      }
    }

    "Return invalid" when {

      "CTUTR is invalid" in {
        leftSideError(ukCompanyModelReactivationMax.copy(ctutr = invalidUtr).validate(groupAccountingPeriod)).message shouldBe UTRChecksumError(invalidUtr).message
      }

      "CompanyName is invalid" in {
        leftSideError(ukCompanyModelReactivationMax.copy(companyName = companyNameTooLong).validate(groupAccountingPeriod)).message shouldBe
          CompanyNameLengthError(companyNameTooLong.name).message
      }

      "netTaxInterestExpense is < 0" in {
        leftSideError(ukCompanyModelReactivationMax.copy(netTaxInterestExpense = -1).validate(groupAccountingPeriod)).message shouldBe NetTaxInterestExpenseError(-1).message
      }

      "netTaxInterestIncomes is < 0" in {
        leftSideError(ukCompanyModelReactivationMax.copy(netTaxInterestIncome = -1).validate(groupAccountingPeriod)).message shouldBe NetTaxInterestIncomeError(-1).message
      }

      "ExpenseAndIncomeBothNotGreaterThanZero where both values are > 0" in {
        leftSideError(ukCompanyModelReactivationMax.copy(netTaxInterestExpense = 20.00,netTaxInterestIncome = 30.00).validate(groupAccountingPeriod)).message shouldBe ExpenseAndIncomeBothNotGreaterThanZero(20.00,30.00).message
      }

    }
  }
}

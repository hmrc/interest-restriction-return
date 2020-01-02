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

package validation.fullReturn

import assets.fullReturn.AllocatedRestrictionsConstants.{ap1End, ap3End}
import assets.fullReturn.UkCompanyConstants._
import models.AccountingPeriodModel
import play.api.libs.json.JsPath
import validation.{BaseValidationSpec, CompanyNameLengthError, UTRChecksumError}

class UkCompanyValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  val groupAccountingPeriod = AccountingPeriodModel(
    startDate = ap1End.minusDays(1),
    endDate = ap3End
  )

  "Full UK Company Validation" should {

    "Return valid" when {

      "a valid Full UK Company model is validated" in {
        rightSide(ukCompanyModelMax.validate(groupAccountingPeriod)) shouldBe ukCompanyModelMax
      }
    }

    "Return invalid" when {

      "CTUTR is invalid" in {
        leftSideError(ukCompanyModelMax.copy(utr = invalidUtr).validate(groupAccountingPeriod)).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "CompanyName is invalid" in {
        leftSideError(ukCompanyModelMax.copy(companyName = companyNameTooLong).validate(groupAccountingPeriod)).errorMessage shouldBe
          CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }

      "netTaxInterestExpense is < 0" in {
        leftSideError(ukCompanyModelMax.copy(netTaxInterestExpense = -1).validate(groupAccountingPeriod)).errorMessage shouldBe NetTaxInterestExpenseError(-1).errorMessage
      }

      "netTaxInterestIncomes is < 0" in {
        leftSideError(ukCompanyModelMax.copy(netTaxInterestIncome = -1).validate(groupAccountingPeriod)).errorMessage shouldBe NetTaxInterestIncomeError(-1).errorMessage
      }

      "ExpenseAndIncomeBothNotGreaterThanZero where both values are > 0" in {
        leftSideError(ukCompanyModelMax.copy(netTaxInterestExpense = 20.00,netTaxInterestIncome = 30.00).validate(groupAccountingPeriod)).errorMessage shouldBe ExpenseAndIncomeBothNotGreaterThanZero(20.00,30.00).errorMessage
      }

    }
  }
}

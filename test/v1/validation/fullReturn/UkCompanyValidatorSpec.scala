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

import assets.fullReturn.AllocatedRestrictionsConstants._
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

  val allocatedRestriction = Some(allocatedRestrictionsModel.copy(disallowanceAp1 = Some(10),disallowanceAp2 = Some(10), disallowanceAp3 = Some(10.01), totalDisallowances = Some(30.01)))

  "Full UK Company Validation" should {

    "Return valid" when {

      "a valid Full Reactivation UK Company model is validated" in {
        rightSide(ukCompanyModelReactivationMax.validate(groupAccountingPeriod)) shouldBe ukCompanyModelReactivationMax
      }

      "a valid Full Restriction UK Company model is validated" in {
        rightSide(ukCompanyModelRestrictionMax.validate(groupAccountingPeriod)) shouldBe ukCompanyModelRestrictionMax
      }
    }

    "Return invalid" when {

      "CTUTR is invalid" in {
        leftSideError(ukCompanyModelReactivationMax.copy(ctutr = invalidUtr).validate(groupAccountingPeriod)).errorMessage shouldBe UTRChecksumError(invalidUtr).errorMessage
      }

      "CompanyName is invalid" in {
        leftSideError(ukCompanyModelReactivationMax.copy(companyName = companyNameTooLong).validate(groupAccountingPeriod)).errorMessage shouldBe
          CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }

      "netTaxInterestExpense is < 0" in {
        leftSideError(ukCompanyModelReactivationMax.copy(netTaxInterestExpense = -1).validate(groupAccountingPeriod)).errorMessage shouldBe NetTaxInterestExpenseError(-1).errorMessage
      }

      "netTaxInterestIncomes is < 0" in {
        leftSideError(ukCompanyModelReactivationMax.copy(netTaxInterestIncome = -1).validate(groupAccountingPeriod)).errorMessage shouldBe NetTaxInterestIncomeError(-1).errorMessage
      }

      "ExpenseAndIncomeBothNotGreaterThanZero where both values are > 0" in {
        leftSideError(ukCompanyModelReactivationMax.copy(netTaxInterestExpense = 20.00,netTaxInterestIncome = 30.00).validate(groupAccountingPeriod)).errorMessage shouldBe ExpenseAndIncomeBothNotGreaterThanZero(20.00,30.00).errorMessage
      }

      "RestrictionNotGreaterThanExpense where restriction values > expense" in {
        leftSideError(ukCompanyModelRestrictionMax.copy(netTaxInterestExpense = 20.00, allocatedRestrictions = allocatedRestriction).validate(groupAccountingPeriod)).errorMessage shouldBe RestrictionNotGreaterThanExpense(20.00,30.01).errorMessage
      }
    }
  }
}

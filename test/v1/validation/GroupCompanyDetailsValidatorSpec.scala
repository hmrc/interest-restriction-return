/*
 * Copyright 2025 HM Revenue & Customs
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

import java.time.LocalDate

import data.GroupCompanyDetailsConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.AccountingPeriodModel

class GroupCompanyDetailsValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "GroupCompanyDetailsValidator" should {

    "Return valid" when {

      "valid GroupCompanyDetails are supplied" in {
        rightSide(groupCompanyDetailsModel.validate) shouldBe groupCompanyDetailsModel.accountingPeriod
      }
    }

    "Return invalid" when {

      "Accounting Period is invalid" in {
        val invalidAmountOfMonths = 20

        val startDate = LocalDate.now().minusMonths(invalidAmountOfMonths)
        val endDate   = LocalDate.now().minusDays(1)

        leftSideError(
          groupCompanyDetailsModel.copy(accountingPeriod = AccountingPeriodModel(startDate, endDate)).validate
        ).errorMessage shouldBe
          AccountingPeriod18MonthsMax(endDate).errorMessage
      }
    }
  }
}

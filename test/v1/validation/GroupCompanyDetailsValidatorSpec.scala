/*
 * Copyright 2021 HM Revenue & Customs
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

import assets.GroupCompanyDetailsConstants._
import config.Constants
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.AccountingPeriodModel

class GroupCompanyDetailsValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "GroupCompanyDetailsValidator" should {

    "Return valid" when {

      "valid GroupCompanyDetails are supplied" in {
        rightSide(groupCompanyDetailsModel.validate) shouldBe groupCompanyDetailsModel
      }

      "Total companies is == IntMax(16)" in {
        val model = groupCompanyDetailsModel.copy(totalCompanies = Constants.intMax)
        rightSide(model.validate) shouldBe model
      }
    }

    "Return invalid" when {

      "Total companies is < 0" in {
        leftSideError(groupCompanyDetailsModel.copy(totalCompanies = -1).validate).errorMessage shouldBe GroupCompanyDetailsTotalCompaniesError(-1).errorMessage
      }

      "Total companies is == 0" in {
        leftSideError(groupCompanyDetailsModel.copy(totalCompanies = 0).validate).errorMessage shouldBe GroupCompanyDetailsTotalCompaniesError(0).errorMessage
      }

      "Total companies is == IntMax(16)+1" in {
        leftSideError(groupCompanyDetailsModel.copy(totalCompanies = Constants.intMax + 1).validate).errorMessage shouldBe
          GroupCompanyDetailsTotalCompaniesError(Constants.intMax + 1).errorMessage
      }

      "Accounting Period is invalid" in {

        val startDate = LocalDate.now().minusMonths(20)
        val endDate = LocalDate.now().minusDays(1)

        leftSideError(groupCompanyDetailsModel.copy(accountingPeriod = AccountingPeriodModel(startDate, endDate)).validate).errorMessage shouldBe
          AccountingPeriod18MonthsMax(endDate).errorMessage
      }
    }
  }
}

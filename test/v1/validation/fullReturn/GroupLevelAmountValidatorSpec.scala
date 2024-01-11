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

import data.fullReturn.GroupLevelAmountConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec

class GroupLevelAmountValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "GroupLevelAmountValidator" should {

    "Return valid" when {

      "all amounts are > 0" in {
        rightSide(groupLevelAmountModel.validate) shouldBe groupLevelAmountModel
      }
    }

    "Return invalid" when {

      "interestReactivationCap is < 0" in {
        val result = leftSideError(groupLevelAmountModel.copy(interestReactivationCap = -0.1).validate)
        result.errorMessage shouldBe ReactivationCapCannotBeNegative(-0.1).errorMessage
        result.path         shouldBe path \ "interestReactivationCap"
      }

      "interestReactivationCap is >2 decimal places" in {
        leftSideError(groupLevelAmountModel.copy(interestReactivationCap = 1.111).validate).errorMessage shouldBe
          ReactivationCapDecimalError(1.111).errorMessage
      }

      "interestAllowanceBroughtForward is < 0" in {
        val result = leftSideError(groupLevelAmountModel.copy(interestAllowanceBroughtForward = -0.1).validate)
        result.errorMessage shouldBe InterestAllowanceBroughtForwardCannotBeNegative(-0.1).errorMessage
        result.path         shouldBe path \ "interestAllowanceBroughtForward"
      }

      "interestAllowanceBroughtForward is >2 decimal places" in {
        leftSideError(
          groupLevelAmountModel.copy(interestAllowanceBroughtForward = 1.111).validate
        ).errorMessage shouldBe
          InterestAllowanceBroughtForwardDecimalError(1.111).errorMessage
      }

      "interestAllowanceForPeriod is < 0" in {
        val result = leftSideError(groupLevelAmountModel.copy(interestAllowanceForPeriod = -0.1).validate)
        result.errorMessage shouldBe InterestAllowanceForPeriodCannotBeNegative(-0.1).errorMessage
        result.path         shouldBe path \ "interestAllowanceForPeriod"
      }

      "interestAllowanceForPeriod is >2 decimal places" in {
        leftSideError(groupLevelAmountModel.copy(interestAllowanceForPeriod = 1.111).validate).errorMessage shouldBe
          InterestAllowanceForPeriodDecimalError(1.111).errorMessage
      }

      "interestCapacityForPeriod is < 0" in {
        val result = leftSideError(groupLevelAmountModel.copy(interestCapacityForPeriod = -0.1).validate)
        result.errorMessage shouldBe InterestCapacityForPeriodCannotBeNegative(-0.1).errorMessage
        result.path         shouldBe path \ "interestCapacityForPeriod"
      }

      "interestCapacityForPeriod is >2 decimal places" in {
        leftSideError(groupLevelAmountModel.copy(interestCapacityForPeriod = 1.111).validate).errorMessage shouldBe
          InterestCapacityForPeriodDecimalError(1.111).errorMessage
      }
    }
  }
}

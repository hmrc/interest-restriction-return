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

import assets.fullReturn.GroupLevelAmountConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec

class GroupLevelAmountValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "GroupLevelAmount" should {

    "Return valid" when {

      "all amounts are > 0" in {
        rightSide(groupLevelAmountModel.validate) shouldBe groupLevelAmountModel
      }
    }

    "Return invalid" when {

      "interestReactivationCap is < 0" in {
        leftSideError(groupLevelAmountModel.copy(interestReactivationCap = Some(-0.1)).validate).message shouldBe
          GroupLevelAmountCannotBeNegative("interestReactivationCap", -0.1).message
      }

      "interestAllowanceBroughtForward is < 0" in {
        leftSideError(groupLevelAmountModel.copy(interestAllowanceBroughtForward = -0.1).validate).message shouldBe
          GroupLevelAmountCannotBeNegative("interestAllowanceBroughtForward", -0.1).message
      }

      "interestAllowanceForPeriod is < 0" in {
        leftSideError(groupLevelAmountModel.copy(interestAllowanceForPeriod = -0.1).validate).message shouldBe
          GroupLevelAmountCannotBeNegative("interestAllowanceForPeriod", -0.1).message
      }

      "interestCapacityForPeriod is < 0" in {
        leftSideError(groupLevelAmountModel.copy(interestCapacityForPeriod = -0.1).validate).message shouldBe
          GroupLevelAmountCannotBeNegative("interestCapacityForPeriod", -0.1).message
      }
    }
  }
}

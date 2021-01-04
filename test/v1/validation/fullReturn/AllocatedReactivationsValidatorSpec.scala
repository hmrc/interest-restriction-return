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

package v1.validation.fullReturn

import assets.fullReturn.AllocatedReactivationsConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec

class AllocatedReactivationsValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "AllocatedReactivationsValidator" should {

    "Return valid" when {

      "currentPeriodReactivation > 0" in {
        val model = allocatedReactivationsModel.copy(currentPeriodReactivation = 0.01)
        rightSide(model.validate) shouldBe model
      }

      "currentPeriodReactivation == 0" in {
        val model = allocatedReactivationsModel.copy(currentPeriodReactivation = 0)
        rightSide(model.validate) shouldBe model
      }
    }

    "Return invalid" when {

      "currentPeriodReactivation is < 0" in {
        leftSideError(allocatedReactivationsModel.copy(currentPeriodReactivation = -1).validate).errorMessage shouldBe
          AllocatedReactivationsCannotBeNegative(-1).errorMessage
      }

      "currentPeriodReactivation is >2 decimal places" in {
        leftSideError(allocatedReactivationsModel.copy(currentPeriodReactivation = 1.111).validate).errorMessage shouldBe
          AllocatedReactivationsDecimalError(1.111).errorMessage
      }
    }
  }
}

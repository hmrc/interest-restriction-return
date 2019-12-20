/*
 * Copyright 2019 HM Revenue & Customs
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

package validation

import assets.GroupRatioConstants._
import assets.GroupRatioBlendedConstants._
import play.api.libs.json.JsPath

class GroupRatioValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Group Ratio " when {
    "Return valid" when {

      "isElected is true  and groupRatioBlended is not given" in {
        val model = groupRatioModelMax.copy(isElected = true ,groupRatioBlended = None)
        rightSide(model.validate) shouldBe model
      }

      "isElected is true  and groupRatioBlended is given" in {
        val model = groupRatioModelMax.copy(isElected = true ,groupRatioBlended = Some(groupRatioBlendedModelMax))
        rightSide(model.validate) shouldBe model
      }

      "isElected is false  and groupRatioBlended is not given" in {
        val model = groupRatioModelMax.copy(isElected = false ,groupRatioBlended = None)
        rightSide(model.validate) shouldBe model
      }

      "groupEBIDTA is false" in {
        val model = groupRatioModelMax.copy(groupEBITDAChargeableGains = Some(false))
        rightSide(model.validate) shouldBe model
      }
    }

    "Return invalid" when {

      "isElected is false and Group Ratio Blended is given" in {
        val model = groupRatioModelMax.copy(isElected = false, groupRatioBlended = Some(groupRatioBlendedModelMax))
        leftSideError(model.validate).errorMessage shouldBe GroupRatioError(model).errorMessage
      }

    }

  }
}

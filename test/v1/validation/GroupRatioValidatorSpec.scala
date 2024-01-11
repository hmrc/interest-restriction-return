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

package v1.validation

import data.GroupRatioConstants._
import data.GroupRatioBlendedConstants._
import play.api.libs.json.JsPath

class GroupRatioValidatorSpec extends BaseValidationSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "GroupRatioValidator" should {

    "Return valid" when {

      "isElected is true and bot are provided" in {
        rightSide(groupRatioModelMax.validate) shouldBe groupRatioModelMax
      }

      "isElected is false and neither are provided" in {
        rightSide(groupRatioModelMin.validate) shouldBe groupRatioModelMin
      }

      "isElected is false and GroupEBITDA is given" in {
        val model      =
          groupRatioModelMax.copy(isElected = false, groupRatioBlended = None, groupEBITDAChargeableGains = false)
        val validModel =
          groupRatioModelMax.copy(isElected = false, groupRatioBlended = None, groupEBITDAChargeableGains = false)
        rightSide(model.validate) shouldBe validModel
      }

      "isElected is true and GroupEBITDA is false" in {
        val model      = groupRatioModelMax.copy(isElected = true, groupEBITDAChargeableGains = false)
        val validModel = groupRatioModelMax.copy(isElected = true, groupEBITDAChargeableGains = false)
        rightSide(model.validate) shouldBe validModel
      }
    }

    "Return invalid" when {
      "isElected is false and Group Ratio Blended is given" in {
        val model = groupRatioModelMax.copy(isElected = false, groupRatioBlended = Some(groupRatioBlendedModelMax))
        leftSideError(model.validate).errorMessage shouldBe GroupRatioBlendedSupplied(model).errorMessage
      }

      "isElected is true and Group Ratio Blended is not provided" in {
        val model = groupRatioModelMax.copy(isElected = true, groupRatioBlended = None)
        leftSideError(model.validate).errorMessage shouldBe GroupRatioBlendedNotSupplied().errorMessage
      }
    }
  }
}

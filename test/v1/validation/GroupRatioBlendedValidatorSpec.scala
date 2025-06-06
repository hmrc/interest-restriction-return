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

import play.api.libs.json.JsPath
import v1.models.{CompanyNameModel, GroupRatioBlendedModel}
import data.InvestorGroupConstants.*

class GroupRatioBlendedValidatorSpec extends BaseValidationSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "GroupRatioBlendedValidator" should {

    "Return valid" when {

      "isElected is true and a Seq of investor group given" in {
        val model = GroupRatioBlendedModel(isElected = true, investorGroups = Some(Seq(investorGroupsGroupRatioModel)))
        rightSide(model.validate) shouldBe model
      }

      "isElected is true and no investor group given" in {
        val model = GroupRatioBlendedModel(isElected = true, investorGroups = None)
        rightSide(model.validate) shouldBe model
      }

      "isElected is false and no investor group are given" in {
        val model = GroupRatioBlendedModel(isElected = false, investorGroups = None)
        rightSide(model.validate) shouldBe model
      }
    }

    "Return invalid" when {

      "isElected is false and some investor group are given" in {
        val model = GroupRatioBlendedModel(isElected = false, investorGroups = Some(Seq(investorGroupsGroupRatioModel)))
        leftSideError(model.validate).errorMessage shouldBe GroupRatioBlendedNotElectedError(model).errorMessage
      }

      "investor group are empty" in {
        val model = GroupRatioBlendedModel(isElected = true, investorGroups = Some(Nil))
        leftSideError(model.validate).errorMessage shouldBe InvestorGroupsEmpty().errorMessage
      }

      "investorGroup contains v1.validation errors" in {
        val model = GroupRatioBlendedModel(
          isElected = true,
          investorGroups = Some(Seq(investorGroupsModelMin.copy(groupName = CompanyNameModel(""))))
        )
        leftSideError(model.validate).errorMessage shouldBe CompanyNameLengthError("").errorMessage
      }
    }
  }
}

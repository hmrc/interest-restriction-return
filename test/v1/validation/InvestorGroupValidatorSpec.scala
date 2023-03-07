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

import assets.InvestorGroupConstants._
import play.api.libs.json.JsPath
import v1.models.CompanyNameModel

class InvestorGroupValidatorSpec extends BaseValidationSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "InvestorGroupValidator" should {

    "Return valid" when {

      "investor names given without elections" in {
        val model = investorGroupsModelMin
        model.validate.toEither.value shouldBe model
      }

      "investor names given with elections" in {
        val model = investorGroupsGroupRatioModel
        model.validate.toEither.value shouldBe model
      }
    }

    "Return invalid" when {

      "Investor Name" when {

        "Investor name is greater than 160" in {
          val model = investorGroupsGroupRatioModel.copy(groupName = CompanyNameModel("a" * (32767 + 1)))
          model.validate.toEither.left.value.head.errorMessage shouldBe CompanyNameLengthError(
            "a" * (32767 + 1)
          ).errorMessage
        }

        "investor name is blank" in {
          val model = investorGroupsGroupRatioModel.copy(groupName = CompanyNameModel(""))
          model.validate.toEither.left.value.head.errorMessage shouldBe CompanyNameLengthError("").errorMessage
        }

        "investor name contains invalid characters" in {
          val model = investorGroupsGroupRatioModel.copy(groupName = CompanyNameModel("ʰʲʺ£$%˦˫qw"))
          model.validate.toEither.left.value.head.errorMessage shouldBe CompanyNameCharactersError(
            "ʰʲʺ£$%˦˫qw"
          ).errorMessage
        }
      }
    }
  }
}

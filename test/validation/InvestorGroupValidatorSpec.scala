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

import assets.InvestorGroupConstants._
import play.api.libs.json.JsPath

class InvestorGroupValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Investor Group" when {

    "Return valid" when {

      "isElected is true and no investor names given" in {
        val model = investorGroupsModel.copy(investorName)
        model.validate.toEither.right.get shouldBe model
      }
    }

    "Return invalid" when {

      "Investor Name" when {
        "Investor name is greater than 32767" in {
          val model = investorGroupsModel.copy(investorName = "a" * (32767 + 1))

          model.validate.toEither.left.get.head.errorMessage shouldBe InvestorNameError(investorName).errorMessage
        }

        "isElected is true and no investor names given" in {
          val model = investorGroupsModel.copy(investorName = "")
          model.validate.toEither.left.get.head.errorMessage shouldBe InvestorNameError(investorName).errorMessage
        }
      }
    }
  }
}

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

package v1.validation

import play.api.libs.json.JsPath
import v1.models.{NonConsolidatedInvestmentElectionModel, NonConsolidatedInvestmentModel}

class NonConsolidatedInvestmentElectionValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Non-Consolidated Investment" when {

    "Return valid" when {

      "isElected is true and a Seq of investments are given" in {
        val model = NonConsolidatedInvestmentElectionModel(
          isElected = true,
          nonConsolidatedInvestments = Some(Seq(NonConsolidatedInvestmentModel("Big Bucks 1")))
        )
        rightSide(model.validate) shouldBe model
      }

      "isElected is false and no investments are given" in {
        val model = NonConsolidatedInvestmentElectionModel(isElected = false, nonConsolidatedInvestments = None)
        rightSide(model.validate) shouldBe model
      }
    }

    "Return invalid" when {

      "isElected is true and no investments are given" in {
        val model = NonConsolidatedInvestmentElectionModel(isElected = true, nonConsolidatedInvestments = None)
        leftSideError(model.validate).errorMessage shouldBe NonConsolidatedInvestmentNotSupplied(model).errorMessage
      }

      "isElected is false and some investments are given" in {
        val model = NonConsolidatedInvestmentElectionModel(
          isElected = false,
          nonConsolidatedInvestments = Some(Seq(NonConsolidatedInvestmentModel("Big Bucks 1")))
        )
        leftSideError(model.validate).errorMessage shouldBe NonConsolidatedInvestmentSupplied(model).errorMessage
      }

      "nonConsolidatedInvestments is invalid" in {
        val model = NonConsolidatedInvestmentElectionModel(
          isElected = true,
          nonConsolidatedInvestments = Some(Seq(NonConsolidatedInvestmentModel("")))
        )

        leftSideError(model.validate).errorMessage shouldBe NonConsolidatedInvestmentNameError("").errorMessage
      }
    }
  }
}

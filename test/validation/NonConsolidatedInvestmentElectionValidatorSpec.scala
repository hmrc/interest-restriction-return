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

import models.{NonConsolidatedInvestmentElectionModel, NonConsolidatedInvestmentModel}
import play.api.libs.json.JsPath

class NonConsolidatedInvestmentElectionValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Non-Consolidated Investment" when {
    "Return valid" when {

      "isElected is true and a Seq of investments are given" in {
        val model = NonConsolidatedInvestmentElectionModel(isElected = true, nonConsolidatedInvestments = Some(Seq(NonConsolidatedInvestmentModel("Big Bucks 1"))))
        model.validate.toEither.right.get shouldBe model
      }

      "isElected is true and no investments are given" in {
        val model = NonConsolidatedInvestmentElectionModel(isElected = true, nonConsolidatedInvestments = None)
        model.validate.toEither.right.get shouldBe model
      }
    }

    "isElected is false and no investments are given" in {
      val model = NonConsolidatedInvestmentElectionModel(isElected = false, nonConsolidatedInvestments = None)
      model.validate.toEither.right.get shouldBe model

    }

    "Return invalid" when {

        "isElected is false and some investments are given" in {
        val model = NonConsolidatedInvestmentElectionModel(isElected = false, nonConsolidatedInvestments = Some(Seq(NonConsolidatedInvestmentModel("Big Bucks 1"))))
          model.validate.toEither.left.get.head.errorMessage shouldBe NonConsolidatedInvestmentElectionError(model).errorMessage
      }
    }

  }
}

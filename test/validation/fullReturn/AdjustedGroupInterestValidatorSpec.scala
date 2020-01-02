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

package validation.fullReturn

import assets.fullReturn.AdjustedGroupInterestConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec
import validation.BaseValidationSpec

class AdjustedGroupInterestValidatorSpec extends BaseValidationSpec with BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "Adjusted Group Interest Validation" should {

    "Return Valid" when {

      "a valid Adjusted Group Interest Model is validated" in {
        rightSide(adjustedGroupInterestModel.validate) shouldBe adjustedGroupInterestModel
      }

      "Group EBITDA is zero and group ratio is 100" in {

        val qngie: BigDecimal = 100.0
        val groupEBITDA: BigDecimal = 0.0
        val groupRatio:BigDecimal = 100.0

        val model = adjustedGroupInterestModel.copy(
          qngie = qngie,
          groupEBITDA = groupEBITDA,
          groupRatio = groupRatio
        )
        rightSide(model.validate) shouldBe model
      }

      "Group Ratio" when {

        "is 1%" in {

          val qngie = 100.0
          val groupEBITDA = 10000

          val model = adjustedGroupInterestModel.copy(
            qngie = qngie,
            groupEBITDA = groupEBITDA,
            groupRatio = 1.00
          )
          rightSide(model.validate) shouldBe model
        }
        "is 50%" in {

          val qngie = 100.0
          val groupEBITDA = 200

          val model = adjustedGroupInterestModel.copy(

            qngie = qngie,
            groupEBITDA = groupEBITDA,
            groupRatio = 50
          )
          rightSide(model.validate) shouldBe model
        }
        "is > 100 and is capped" in {

          val qngie = 100000.0
          val groupEBITDA = 10.0

          val model = adjustedGroupInterestModel.copy(

            qngie = qngie,
            groupEBITDA = groupEBITDA,
            groupRatio = 100
          )
          rightSide(model.validate) shouldBe model
        }

        "has a decimal which is a round number" in {

          val qngie = 5.0
          val groupEBITDA = 8.0
          val groupRatio = 62.5

          val model = adjustedGroupInterestModel.copy(

            qngie = qngie,
            groupEBITDA = groupEBITDA,
            groupRatio = groupRatio
          )
          rightSide(model.validate) shouldBe model
        }

        "has no decimal places" in {

          val qngie = 100
          val groupEBITDA = 200
          val groupRatio = 50

          val model = adjustedGroupInterestModel.copy(
            qngie = qngie,
            groupEBITDA = groupEBITDA,
            groupRatio = groupRatio
          )
          rightSide(model.validate) shouldBe model
        }

        "has two decimal places" in {

          val qngie = 100.0
          val groupEBITDA = 260.0
          val groupRatio = 38.46

          val model = adjustedGroupInterestModel.copy(
            qngie = qngie,
            groupEBITDA = groupEBITDA,
            groupRatio = groupRatio
          )
          rightSide(model.validate) shouldBe model
        }
      }
    }

    "Return invalid" when {

      "Group Ratio" when {

        "is negative" in {

          val groupRatio: BigDecimal = -100.00
          val model = adjustedGroupInterestModel.copy(
            groupRatio = groupRatio
          )
          leftSideError(model.validate).errorMessage shouldBe GroupRatioError(groupRatio).errorMessage
        }

        "is greater than 100" in {

          val groupRatio: BigDecimal = 100.01
          val model = adjustedGroupInterestModel.copy(
            groupRatio = groupRatio
          )
          leftSideError(model.validate).errorMessage shouldBe GroupRatioError(groupRatio).errorMessage
        }

        "has more than two decimal places" in {

          val qngie = 5.0
          val groupEBITDA = 8.0
          val groupRatio = 0.625

          val model = adjustedGroupInterestModel.copy(

            qngie = qngie,
            groupEBITDA = groupEBITDA,
            groupRatio = groupRatio
          )
          leftSideError(model.validate).errorMessage shouldBe GroupRatioError(groupRatio).errorMessage
        }

        "is less than the calculated groupRatio" in {

          val groupRatio: BigDecimal = 0.5
          val model = adjustedGroupInterestModel.copy(
            groupRatio = groupRatio
          )
          leftSideError(model.validate).errorMessage shouldBe GroupRatioCalculationError(model).errorMessage

        }
      }
    }
  }
}
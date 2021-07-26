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

import assets.fullReturn.AdjustedGroupInterestConstants._
import cats.data.NonEmptyChain
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.validation.BaseValidationSpec

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
        val groupRatio: BigDecimal = 100.0

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
          val groupRatio = 100

          val model = adjustedGroupInterestModel.copy(

            qngie = qngie,
            groupEBITDA = groupEBITDA,
            groupRatio = groupRatio
          )
          rightSide(model.validate) shouldBe model
        }

        "GroupRatio & QNGIE are zero" in {
          val qngie = 0
          val groupRatio = 0

          val model = adjustedGroupInterestModel.copy(
            qngie = qngie,
            groupRatio = groupRatio
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

        "has five decimal places" in {
          val qngie = 100.04
          val groupEBITDA = 260.15
          val groupRatio = 38.45473

          val model = adjustedGroupInterestModel.copy(
            qngie = qngie,
            groupEBITDA = groupEBITDA,
            groupRatio = groupRatio
          )
          val result = model.validate
          rightSide(result) shouldBe model
        }

        "only provides some decimal places" in {

          val qngie = 100.04
          val groupEBITDA = 260.15
          val groupRatio = 38.45

          val model = adjustedGroupInterestModel.copy(
            qngie = qngie,
            groupEBITDA = groupEBITDA,
            groupRatio = groupRatio
          )
          val result = model.validate
          rightSide(result) shouldBe model
        }
      }
    }

    "Return invalid" when {
      "qngie" when {
        "has more than two decimal places" in {
          val qngie: BigDecimal = 100.111
          val model = adjustedGroupInterestModel.copy(
            qngie = qngie
          )
          leftSideError(model.validate).errorMessage shouldBe QngieDecimalError(qngie).errorMessage
        }

        "is calculated to be negative" in {
          val model = adjustedGroupInterestModel.copy(
            qngie = -1,
            groupEBITDA = 1,
            groupRatio = 1
          )
          leftSideError(model.validate).errorMessage shouldBe NegativeQNGIEError(groupRatio).errorMessage
        }
      }

      "Group EBITDA" when {
        "has more than two decimal places" in {
          val qngie = 5.0
          val groupEBITDA = 8.888
          val groupRatio = 0.62

          val model = adjustedGroupInterestModel.copy(

            qngie = qngie,
            groupEBITDA = groupEBITDA,
            groupRatio = groupRatio
          )
          leftSideError(model.validate).errorMessage shouldBe GroupEBITDADecimalError(groupRatio).errorMessage
        }
      }

      "Group Ratio" when {
        "is negative" in {
          val groupRatio: BigDecimal = -100.00
          val model = adjustedGroupInterestModel.copy(
            groupRatio = groupRatio
          )
          leftSideError(model.validate).errorMessage shouldBe GroupRatioError(groupRatio).errorMessage
        }

        "is calculated to be negative but not set to 100%" in {
          val groupRatio: BigDecimal = 99.00
          val model = adjustedGroupInterestModel.copy(
            qngie = -1,
            groupEBITDA = 1,
            groupRatio = groupRatio
          )
          leftSideError(model.validate).errorMessage shouldBe NegativeQNGIEError(groupRatio).errorMessage
        }
      }

      "is greater than 100" in {
        val groupRatio: BigDecimal = 100.01
        val model = adjustedGroupInterestModel.copy(
          groupRatio = groupRatio
        )
        leftSideError(model.validate).errorMessage shouldBe GroupRatioError(groupRatio).errorMessage
      }

      "has more than five decimal places" in {
        val qngie = 5.0
        val groupEBITDA = 8.0
        val groupRatio = 0.625555

        val model = adjustedGroupInterestModel.copy(

          qngie = qngie,
          groupEBITDA = groupEBITDA,
          groupRatio = groupRatio
        )
        leftSideError(model.validate).errorMessage shouldBe GroupRatioDecimalError(groupRatio).errorMessage
      }
    }

    "GroupEBITDA" when {
      "is negative" in {
        val groupEBITDA: BigDecimal = -1
        val model = adjustedGroupInterestModel.copy(groupEBITDA = groupEBITDA)
        leftSideError(model.validate).errorMessage shouldBe NegativeOrZeroGroupEBITDAError(groupEBITDA).errorMessage
      }

      "is zero" in {
        val groupEBITDA: BigDecimal = 0
        val model = adjustedGroupInterestModel.copy(groupEBITDA = groupEBITDA)
        leftSideError(model.validate).errorMessage shouldBe NegativeOrZeroGroupEBITDAError(groupEBITDA).errorMessage
      }
    }

    "All fields fail validation" in {
      val qngie = 5.0122
      val groupEBITDA = 8.888
      val groupRatio = 0.621234

      val model = adjustedGroupInterestModel.copy(

        qngie = qngie,
        groupEBITDA = groupEBITDA,
        groupRatio = groupRatio
      )
      val expectedErrors = NonEmptyChain(
        GroupRatioDecimalError(0.621234), QngieDecimalError(5.0122),
        GroupEBITDADecimalError(8.888))

      model.validate.toEither shouldBe Left(expectedErrors)
    }

    "Multiple fields fail validation" in {
      val qngie = 5.0122
      val groupEBITDA = 8.88
      val groupRatio = 0.621234

      val model = adjustedGroupInterestModel.copy(

        qngie = qngie,
        groupEBITDA = groupEBITDA,
        groupRatio = groupRatio
      )
      val expectedErrors = NonEmptyChain(GroupRatioDecimalError(0.621234), QngieDecimalError(5.0122))
      model.validate.toEither shouldBe Left(expectedErrors)
    }
  }
}
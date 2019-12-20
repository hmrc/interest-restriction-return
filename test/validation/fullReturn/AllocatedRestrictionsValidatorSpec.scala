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

package validation.fullReturn

import assets.fullReturn.AllocatedRestrictionsConstants._
import models.fullReturn.AllocatedRestrictionsModel
import play.api.libs.json.JsPath
import utils.BaseSpec

class AllocatedRestrictionsValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  val restrictionModel = AllocatedRestrictionsModel(
    ap1End = None,
    disallowanceAp1 = None,
    ap2End = None,
    disallowanceAp2 = None,
    ap3End = None,
    disallowanceAp3 = None,
    totalDisallowances = None
  )

  "AllocatedRestrictionsValidator" should {

    "Return valid" when {

      "Ap1 supplied with the total disallowed amount" in {

        val model = restrictionModel.copy(
          ap1End = Some(ap1End),
          disallowanceAp1 = Some(disallowanceAp1),
          totalDisallowances = Some(totalDisallowances)
        )

        rightSide(model.validate) shouldBe model
      }

      "Ap1 and Ap2 supplied with the total disallowed amount" in {

        val model = restrictionModel.copy(
          ap1End = Some(ap1End),
          disallowanceAp1 = Some(disallowanceAp1),
          ap2End = Some(ap2End),
          disallowanceAp2 = Some(disallowanceAp2),
          totalDisallowances = Some(totalDisallowances)
        )

        rightSide(model.validate) shouldBe model
      }

      "Ap1, Ap2 and Ap3 supplied with the total disallowed amount" in {

        val model = restrictionModel.copy(
          ap1End = Some(ap1End),
          disallowanceAp1 = Some(disallowanceAp1),
          ap2End = Some(ap2End),
          disallowanceAp2 = Some(disallowanceAp2),
          ap3End = Some(ap3End),
          disallowanceAp3 = Some(disallowanceAp3),
          totalDisallowances = Some(totalDisallowances)
        )

        rightSide(model.validate) shouldBe model
      }

    }

    "Return invalid" when {

      "Ap1" when {

        "is supplied with no amount" in {

          val model = restrictionModel.copy(
            ap1End = Some(ap1End),
            totalDisallowances = Some(totalDisallowances)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionNotSupplied(1).errorMessage
        }

        "is supplied with no date" in {

          val model = restrictionModel.copy(
            disallowanceAp1 = Some(disallowanceAp1),
            totalDisallowances = Some(totalDisallowances)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionSupplied(1).errorMessage
        }

        "is supplied with negative amount" in {

          val model = restrictionModel.copy(
            ap1End = Some(ap1End),
            disallowanceAp1 = Some(-1),
            totalDisallowances = Some(totalDisallowances)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionNegative(1, -1).errorMessage
        }
      }

      "Ap2" when {

        "is supplied with no amount" in {

          val model = restrictionModel.copy(
            ap2End = Some(ap2End),
            totalDisallowances = Some(totalDisallowances)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionNotSupplied(2).errorMessage
        }

        "is supplied with no date" in {

          val model = restrictionModel.copy(
            disallowanceAp2 = Some(disallowanceAp2),
            totalDisallowances = Some(totalDisallowances)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionSupplied(2).errorMessage
        }

        "is supplied with negative amount" in {

          val model = restrictionModel.copy(
            ap2End = Some(ap2End),
            disallowanceAp2 = Some(-1),
            totalDisallowances = Some(totalDisallowances)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionNegative(2, -1).errorMessage
        }

        "is supplied without Ap1" in {

          val model = restrictionModel.copy(
            ap2End = Some(ap2End),
            disallowanceAp2 = Some(disallowanceAp2),
            totalDisallowances = Some(totalDisallowances)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionLaterPeriodSupplied(2).errorMessage
        }
      }

      "Ap3" when {

        "is supplied with no amount" in {

          val model = restrictionModel.copy(
            ap3End = Some(ap3End),
            totalDisallowances = Some(totalDisallowances)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionNotSupplied(3).errorMessage
        }

        "is supplied with no date" in {

          val model = restrictionModel.copy(
            disallowanceAp3 = Some(disallowanceAp3),
            totalDisallowances = Some(totalDisallowances)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionSupplied(3).errorMessage
        }

        "is supplied with negative amount" in {

          val model = restrictionModel.copy(
            ap3End = Some(ap3End),
            disallowanceAp3 = Some(-1),
            totalDisallowances = Some(totalDisallowances)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionNegative(3, -1).errorMessage
        }

        "is supplied without Ap1 and Ap2" in {

          val model = restrictionModel.copy(
            ap3End = Some(ap3End),
            disallowanceAp3 = Some(disallowanceAp3),
            totalDisallowances = Some(totalDisallowances)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionLaterPeriodSupplied(3).errorMessage
        }

        "is supplied without Ap2" in {

          val model = restrictionModel.copy(
            ap1End = Some(ap1End),
            disallowanceAp1 = Some(disallowanceAp1),
            ap3End = Some(ap3End),
            disallowanceAp3 = Some(disallowanceAp3),
            totalDisallowances = Some(totalDisallowances)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionLaterPeriodSupplied(3).errorMessage
        }
      }

      "totalDisallowances" when {

        "is not supplied when Ap1 is" in {

          val model = restrictionModel.copy(
            ap1End = Some(ap1End),
            disallowanceAp1 = Some(disallowanceAp1)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionTotalNotSupplied().errorMessage
        }

        "is not supplied when Ap1 & Ap2 is" in {

          val model = restrictionModel.copy(
            ap1End = Some(ap1End),
            disallowanceAp1 = Some(disallowanceAp1),
            ap2End = Some(ap2End),
            disallowanceAp2 = Some(disallowanceAp2)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionTotalNotSupplied().errorMessage
        }

        "is not supplied when Ap1, Ap2 & Ap3 is" in {

          val model = restrictionModel.copy(
            ap1End = Some(ap1End),
            disallowanceAp1 = Some(disallowanceAp1),
            ap2End = Some(ap2End),
            disallowanceAp2 = Some(disallowanceAp2),
            ap3End = Some(ap3End),
            disallowanceAp3 = Some(disallowanceAp3)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionTotalNotSupplied().errorMessage
        }

        "is negative" in {

          val model = restrictionModel.copy(
            ap1End = Some(ap1End),
            disallowanceAp1 = Some(disallowanceAp1),
            totalDisallowances = Some(-1)
          )

          leftSideError(model.validate).errorMessage shouldBe AllocatedRestrictionTotalNegative(-1).errorMessage
        }
      }
    }
  }
}

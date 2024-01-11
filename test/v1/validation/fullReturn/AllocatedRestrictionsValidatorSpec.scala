/*
 * Copyright 2024 HM Revenue & Customs
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

import data.fullReturn.AllocatedRestrictionsConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.AccountingPeriodModel
import v1.models.fullReturn.AllocatedRestrictionsModel
import java.time.LocalDate

class AllocatedRestrictionsValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  private val MINIMUM_DATE: LocalDate = LocalDate.parse("1900-01-01")
  private val MAXIMUM_DATE: LocalDate = LocalDate.parse("2099-12-31")
  private val aYearInMonths: Long     = 12

  private val groupAccountingPeriod: AccountingPeriodModel = AccountingPeriodModel(
    startDate = ap1EndDate.minusDays(1),
    endDate = ap3EndDate
  )

  private val restrictionModel: AllocatedRestrictionsModel = AllocatedRestrictionsModel(
    ap1EndDate = ap1EndDate,
    disallowanceAp1 = disallowanceAp1,
    ap2EndDate = None,
    disallowanceAp2 = None,
    ap3EndDate = None,
    disallowanceAp3 = None
  )

  "AllocatedRestrictionsValidator" should {

    "Return valid" when {

      "Ap1 supplied" in {

        val model = restrictionModel.copy(ap1EndDate = groupAccountingPeriod.endDate.plusDays(1))

        rightSide(model.validate(groupAccountingPeriod)) shouldBe model
      }

      "Ap1 and Ap2 supplied" in {

        val model = restrictionModel.copy(
          ap1EndDate = groupAccountingPeriod.endDate.minusDays(1),
          ap2EndDate = Some(groupAccountingPeriod.endDate.plusDays(1)),
          disallowanceAp2 = Some(disallowanceAp2)
        )

        rightSide(model.validate(groupAccountingPeriod)) shouldBe model
      }

      "Ap1, Ap2 and Ap3 supplied" in {

        val model = restrictionModel.copy(
          ap2EndDate = Some(ap2EndDate),
          disallowanceAp2 = Some(disallowanceAp2),
          ap3EndDate = Some(ap3EndDate),
          disallowanceAp3 = Some(disallowanceAp3)
        )

        rightSide(model.validate(groupAccountingPeriod)) shouldBe model
      }

    }

    "Return invalid" when {

      "Ap1" when {

        "is supplied with negative amount" in {

          val model = restrictionModel.copy(
            disallowanceAp1 = -1
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe AllocatedRestrictionNegative(
            1,
            -1
          ).errorMessage
        }

        "is supplied with >2 decimal places" in {

          val model = restrictionModel.copy(
            disallowanceAp1 = 1.111
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe AllocatedRestrictionDecimalError(
            1,
            1.111
          ).errorMessage
        }

        "is supplied with a date that is equal to Group Accounting Period start date" in {

          val model = restrictionModel.copy(
            ap1EndDate = groupAccountingPeriod.startDate
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe
            Ap1NotAfterGroupStartDate(groupAccountingPeriod.startDate, groupAccountingPeriod.startDate).errorMessage
        }

        "is supplied with a date that is less than Group Accounting Period start date" in {

          val model = restrictionModel.copy(
            ap1EndDate = groupAccountingPeriod.startDate.minusDays(1)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe
            Ap1NotAfterGroupStartDate(
              groupAccountingPeriod.startDate.minusDays(1),
              groupAccountingPeriod.startDate
            ).errorMessage
        }

        "is supplied with a date that is less than the minimum date" in {

          val model = restrictionModel.copy(
            ap1EndDate = MINIMUM_DATE.minusDays(1)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe
            DateRangeError(MINIMUM_DATE.minusDays(1), "ap1EndDate").errorMessage
        }

        "is supplied with a date that is after than the max date" in {

          val model = restrictionModel.copy(
            ap1EndDate = MAXIMUM_DATE.plusDays(1)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe
            DateRangeError(MAXIMUM_DATE.plusDays(1), "ap1EndDate").errorMessage
        }
      }

      "Ap2" when {

        "is supplied with no amount" in {

          val model = restrictionModel.copy(
            ap2EndDate = Some(ap2EndDate)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe AllocatedRestrictionNotSupplied(
            2
          ).errorMessage
        }

        "is supplied with no date" in {

          val model = restrictionModel.copy(
            disallowanceAp2 = Some(disallowanceAp2)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe AllocatedRestrictionSupplied(
            2
          ).errorMessage
        }

        "is supplied with negative amount" in {

          val model = restrictionModel.copy(
            ap2EndDate = Some(ap2EndDate),
            disallowanceAp2 = Some(-1)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe AllocatedRestrictionNegative(
            2,
            -1
          ).errorMessage
        }

        "is supplied with >2 decimal places" in {

          val model = restrictionModel.copy(
            ap2EndDate = Some(ap2EndDate),
            disallowanceAp2 = Some(1.111)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe AllocatedRestrictionDecimalError(
            2,
            1.111
          ).errorMessage
        }

        "is supplied with a date equal to Ap1" in {

          val model = restrictionModel.copy(
            ap2EndDate = Some(ap1EndDate),
            disallowanceAp2 = Some(disallowanceAp2)
          )

          leftSideError(
            model.validate(groupAccountingPeriod)
          ).errorMessage shouldBe AllocatedRestrictionDateBeforePrevious(2).errorMessage
        }

        "is supplied with a date less than Ap1" in {

          val model = restrictionModel.copy(
            ap2EndDate = Some(ap1EndDate.minusDays(1)),
            disallowanceAp2 = Some(disallowanceAp2)
          )

          leftSideError(
            model.validate(groupAccountingPeriod)
          ).errorMessage shouldBe AllocatedRestrictionDateBeforePrevious(2).errorMessage
        }

        "is supplied with a date more than 12 months after Ap1" in {

          val model = restrictionModel.copy(
            ap2EndDate = Some(ap1EndDate.plusMonths(aYearInMonths).plusDays(1)),
            disallowanceAp2 = Some(disallowanceAp2)
          )

          leftSideError(
            model.validate(groupAccountingPeriod)
          ).errorMessage shouldBe AllocatedRestrictionDateGreaterThan12MonthsAfterPrevious(2).errorMessage
        }

        "is supplied with a date that is less than the minimum date" in {

          val model = restrictionModel.copy(
            ap2EndDate = Some(MINIMUM_DATE.minusDays(1)),
            disallowanceAp2 = Some(disallowanceAp2)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe
            DateRangeError(MINIMUM_DATE.minusDays(1), "ap2EndDate").errorMessage
        }

        "is supplied with a date that is after than the max date" in {

          val model = restrictionModel.copy(
            ap2EndDate = Some(MAXIMUM_DATE.plusDays(1)),
            disallowanceAp2 = Some(disallowanceAp2)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe
            DateRangeError(MAXIMUM_DATE.plusDays(1), "ap2EndDate").errorMessage
        }

        "is supplied when ap1 supplied with a date later than GPOA" in {
          val model = restrictionModel.copy(
            ap1EndDate = ap3EndDate.plusDays(1),
            ap2EndDate = Some(ap3EndDate.plusDays(2)),
            disallowanceAp2 = Some(disallowanceAp2)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe
            DateAfterGPOA(2).errorMessage
        }
      }

      "Ap3" when {

        "is supplied with no amount" in {

          val model = restrictionModel.copy(
            ap3EndDate = Some(ap3EndDate)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe AllocatedRestrictionNotSupplied(
            3
          ).errorMessage
        }

        "is supplied with no date" in {

          val model = restrictionModel.copy(
            disallowanceAp3 = Some(disallowanceAp3)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe AllocatedRestrictionSupplied(
            3
          ).errorMessage
        }

        "is supplied with negative amount" in {

          val model = restrictionModel.copy(
            ap3EndDate = Some(ap3EndDate),
            disallowanceAp3 = Some(-1)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe AllocatedRestrictionNegative(
            3,
            -1
          ).errorMessage
        }

        "is supplied with >2 decimal places" in {

          val model = restrictionModel.copy(
            ap3EndDate = Some(ap3EndDate),
            disallowanceAp3 = Some(1.111)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe AllocatedRestrictionDecimalError(
            3,
            1.111
          ).errorMessage
        }

        "is supplied without Ap1 and Ap2" in {

          val model = restrictionModel.copy(
            ap3EndDate = Some(ap3EndDate),
            disallowanceAp3 = Some(disallowanceAp3)
          )

          leftSideError(
            model.validate(groupAccountingPeriod)
          ).errorMessage shouldBe AllocatedRestrictionLaterPeriodSupplied(3).errorMessage
        }

        "is supplied without Ap2" in {

          val model = restrictionModel.copy(
            ap3EndDate = Some(ap3EndDate),
            disallowanceAp3 = Some(disallowanceAp3)
          )

          leftSideError(
            model.validate(groupAccountingPeriod)
          ).errorMessage shouldBe AllocatedRestrictionLaterPeriodSupplied(3).errorMessage
        }

        "is supplied with a date equal to Ap2" in {

          val model = restrictionModel.copy(
            ap1EndDate = ap2EndDate,
            ap2EndDate = Some(ap3EndDate),
            disallowanceAp2 = Some(disallowanceAp2),
            ap3EndDate = Some(ap3EndDate),
            disallowanceAp3 = Some(disallowanceAp3)
          )

          leftSideError(
            model.validate(groupAccountingPeriod)
          ).errorMessage shouldBe AllocatedRestrictionDateBeforePrevious(3).errorMessage
        }

        "is supplied with a date less than Ap2" in {

          val model = restrictionModel.copy(
            ap1EndDate = ap2EndDate,
            ap2EndDate = Some(ap3EndDate.plusDays(1)),
            disallowanceAp2 = Some(disallowanceAp2),
            ap3EndDate = Some(ap3EndDate),
            disallowanceAp3 = Some(disallowanceAp3)
          )

          leftSideError(
            model.validate(groupAccountingPeriod)
          ).errorMessage shouldBe AllocatedRestrictionDateBeforePrevious(3).errorMessage
        }

        "is supplied with a date more than 12 months after Ap2" in {

          val model = restrictionModel.copy(
            ap2EndDate = Some(ap2EndDate),
            disallowanceAp2 = Some(disallowanceAp2),
            ap3EndDate = Some(ap2EndDate.plusMonths(aYearInMonths).plusDays(1)),
            disallowanceAp3 = Some(disallowanceAp3)
          )

          leftSideError(
            model.validate(groupAccountingPeriod)
          ).errorMessage shouldBe AllocatedRestrictionDateGreaterThan12MonthsAfterPrevious(3).errorMessage
        }

        "is supplied with a date that is less than the minimum date" in {

          val model = restrictionModel.copy(
            ap2EndDate = Some(ap2EndDate),
            disallowanceAp2 = Some(disallowanceAp2),
            ap3EndDate = Some(MINIMUM_DATE.minusDays(1)),
            disallowanceAp3 = Some(disallowanceAp3)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe
            DateRangeError(MINIMUM_DATE.minusDays(1), "ap3EndDate").errorMessage
        }

        "is supplied with a date that is after than the max date" in {

          val model = restrictionModel.copy(
            ap2EndDate = Some(ap2EndDate),
            disallowanceAp2 = Some(disallowanceAp2),
            ap3EndDate = Some(MAXIMUM_DATE.plusDays(1)),
            disallowanceAp3 = Some(disallowanceAp3)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe
            DateRangeError(MAXIMUM_DATE.plusDays(1), "ap3EndDate").errorMessage
        }

        "is supplied when ap2 supplied with a date later than GPOA" in {
          val model = restrictionModel.copy(
            ap1EndDate = ap3EndDate.minusDays(3), //
            ap2EndDate = Some(ap3EndDate.plusDays(1)),
            ap3EndDate = Some(ap3EndDate.plusDays(2)),
            disallowanceAp2 = Some(disallowanceAp2),
            disallowanceAp3 = Some(disallowanceAp3)
          )

          leftSideError(model.validate(groupAccountingPeriod)).errorMessage shouldBe
            DateAfterGPOA(3).errorMessage
        }
      }

    }
  }
}

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

package assets.fullReturn

import java.time.LocalDate

import assets.AccountingPeriodConstants._
import play.api.libs.json.Json
import v1.models.fullReturn.AllocatedRestrictionsModel

object AllocatedRestrictionsConstants {

  val ap1EndDate: LocalDate = startDate.plusDays(1)
  val ap2EndDate: LocalDate = startDate.plusMonths(12)
  val ap3EndDate: LocalDate = endDate

  val disallowanceAp1: BigDecimal = 1.00
  val disallowanceAp2: BigDecimal = 2.00
  val disallowanceAp3: BigDecimal = 3.00

  val incorrectDisallowances: BigDecimal = 10.00

  val allocatedRestrictionsModel = AllocatedRestrictionsModel(
    ap1EndDate = ap1EndDate,
    ap2EndDate = Some(ap2EndDate),
    ap3EndDate = Some(ap3EndDate),
    disallowanceAp1 = disallowanceAp1,
    disallowanceAp2 = Some(disallowanceAp2),
    disallowanceAp3 = Some(disallowanceAp3)
  )

  val zeroAllocatedRestrictionsModel = AllocatedRestrictionsModel(
    ap1EndDate = ap1EndDate,
    ap2EndDate = None,
    ap3EndDate = None,
    disallowanceAp1 = 0,
    disallowanceAp2 = None,
    disallowanceAp3 = None
  )

  val allocatedRestrictionsJson = Json.obj(
    "ap1EndDate" -> ap1EndDate,
    "ap2EndDate" -> ap2EndDate,
    "ap3EndDate" -> ap3EndDate,
    "disallowanceAp1" -> disallowanceAp1,
    "disallowanceAp2" -> disallowanceAp2,
    "disallowanceAp3" -> disallowanceAp3
  )
}

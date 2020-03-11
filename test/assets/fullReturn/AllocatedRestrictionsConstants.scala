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

package assets.fullReturn

import java.time.LocalDate

import assets.AccountingPeriodConstants._
import play.api.libs.json.Json
import v1.models.fullReturn.AllocatedRestrictionsModel

object AllocatedRestrictionsConstants {

  val ap1End: LocalDate = startDate.plusDays(1)
  val ap2End: LocalDate = startDate.plusMonths(12)
  val ap3End: LocalDate = endDate

  val disallowanceAp1: BigDecimal = 1.11
  val disallowanceAp2: BigDecimal = 2.22
  val disallowanceAp3: BigDecimal = 3.33
  val totalDisallowances: BigDecimal = 6.66

  val incorrectDisallowances: BigDecimal = 10

  val allocatedRestrictionsModel = AllocatedRestrictionsModel(
    ap1End = Some(ap1End),
    ap2End = Some(ap2End),
    ap3End = Some(ap3End),
    disallowanceAp1 = Some(disallowanceAp1),
    disallowanceAp2 = Some(disallowanceAp2),
    disallowanceAp3 = Some(disallowanceAp3),
    totalDisallowances = Some(totalDisallowances)
  )

  val zeroAllocatedRestrictionsModel = AllocatedRestrictionsModel(
    ap1End = Some(ap1End),
    ap2End = None,
    ap3End = None,
    disallowanceAp1 = Some(0),
    disallowanceAp2 = None,
    disallowanceAp3 = None,
    totalDisallowances = Some(0)
  )

  val allocatedRestrictionsJson = Json.obj(
    "ap1End" -> ap1End,
    "ap2End" -> ap2End,
    "ap3End" -> ap3End,
    "disallowanceAp1" -> disallowanceAp1,
    "disallowanceAp2" -> disallowanceAp2,
    "disallowanceAp3" -> disallowanceAp3,
    "totalDisallowances" -> totalDisallowances
  )
}

/*
 * Copyright 2025 HM Revenue & Customs
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

package data.fullReturn

import play.api.libs.json.{JsObject, Json}
import v1.models.fullReturn.GroupLevelAmountModel

object GroupLevelAmountConstants {

  val interestReactivationCap: BigDecimal         = 300
  val interestAllowanceBroughtForward: BigDecimal = 3.00
  val interestAllowanceForPeriod: BigDecimal      = 4.00
  val interestCapacityForPeriod: BigDecimal       = 5.00

  val groupLevelAmountModel: GroupLevelAmountModel = GroupLevelAmountModel(
    interestReactivationCap = interestReactivationCap,
    interestAllowanceBroughtForward = interestAllowanceBroughtForward,
    interestAllowanceForPeriod = interestAllowanceForPeriod,
    interestCapacityForPeriod = interestCapacityForPeriod
  )

  val groupLevelAmountJson: JsObject = Json.obj(
    "interestReactivationCap"         -> interestReactivationCap,
    "interestAllowanceBroughtForward" -> interestAllowanceBroughtForward,
    "interestAllowanceForPeriod"      -> interestAllowanceForPeriod,
    "interestCapacityForPeriod"       -> interestCapacityForPeriod
  )

  val groupLevelAmountNoCapModel: GroupLevelAmountModel = GroupLevelAmountModel(
    interestReactivationCap = 0,
    interestAllowanceBroughtForward = interestAllowanceBroughtForward,
    interestAllowanceForPeriod = interestAllowanceForPeriod,
    interestCapacityForPeriod = interestCapacityForPeriod
  )
}

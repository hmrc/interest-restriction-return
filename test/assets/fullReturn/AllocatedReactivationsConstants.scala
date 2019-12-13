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

package assets.fullReturn

import models.fullReturn.AllocatedReactivationsModel
import play.api.libs.json.Json

object AllocatedReactivationsConstants {

  val ap1NetDisallowances = 1.11
  val currentPeriodReactivation = 2.22

  val allocatedReactivationsModel = AllocatedReactivationsModel(
    ap1NetDisallowances = ap1NetDisallowances,
    currentPeriodReactivation = currentPeriodReactivation
  )

  val allocatedReactivationsJson = Json.obj(
    "ap1NetDisallowances" -> ap1NetDisallowances,
    "currentPeriodReactivation" -> currentPeriodReactivation
  )
}

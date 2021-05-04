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

package assets

import assets.PartnershipsConstants._
import play.api.libs.json.Json
import v1.models.ConsolidatedPartnershipModel

object ConsolidatedPartnershipConstants extends BaseConstants {

  val consolidatedPartnerships = "investment1"

  val consolidatedPartnershipsModelMax = ConsolidatedPartnershipModel(
    isElected = true,
    isActive = true,
    consolidatedPartnerships = Some(Seq(partnershipModel))
  )

  val consolidatedPartnershipsJsonMax = Json.obj(
    "isElected" -> true,
    "isActive" -> true,
    "consolidatedPartnerships" -> Json.arr(partnershipJson)
  )

  val consolidatedPartnershipsModelMin = ConsolidatedPartnershipModel(
    isElected = false,
    isActive = false,
    consolidatedPartnerships = None
  )

  val consolidatedPartnershipsJsonMin = Json.obj(
    "isElected" -> false,
    "isActive" -> false
  )
}

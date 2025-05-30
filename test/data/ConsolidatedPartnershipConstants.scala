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

package data

import data.PartnershipsConstants.*
import play.api.libs.json.{JsObject, Json}
import v1.models.ConsolidatedPartnershipModel

object ConsolidatedPartnershipConstants {

  val consolidatedPartnerships: String = "investment1"

  val consolidatedPartnershipsModelMax: ConsolidatedPartnershipModel = ConsolidatedPartnershipModel(
    isElected = true,
    isActive = true,
    consolidatedPartnerships = Some(Seq(partnershipModel))
  )

  val consolidatedPartnershipsJsonMax: JsObject = Json.obj(
    "isElected"                -> true,
    "isActive"                 -> true,
    "consolidatedPartnerships" -> Json.arr(partnershipJson)
  )

  val consolidatedPartnershipsModelMin: ConsolidatedPartnershipModel = ConsolidatedPartnershipModel(
    isElected = false,
    isActive = false,
    consolidatedPartnerships = None
  )

  val consolidatedPartnershipsJsonMin: JsObject = Json.obj(
    "isElected" -> false,
    "isActive"  -> false
  )
}

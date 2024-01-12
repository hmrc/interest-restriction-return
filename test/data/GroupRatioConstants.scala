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

package data

import data.GroupRatioBlendedConstants._
import play.api.libs.json.{JsObject, Json}
import v1.models.GroupRatioModel

object GroupRatioConstants {

  val groupRatioJsonMax: JsObject = Json.obj(
    "isElected"                        -> true,
    "groupEBITDAChargeableGains"       -> Some(true),
    "groupRatioBlended"                -> groupRatioBlendedJsonMax,
    "activeGroupEBITDAChargeableGains" -> true
  )

  val groupRatioModelMax: GroupRatioModel = GroupRatioModel(
    isElected = true,
    groupEBITDAChargeableGains = true,
    groupRatioBlended = Some(groupRatioBlendedModelMax),
    activeGroupEBITDAChargeableGains = true
  )

  val groupRatioJsonMin: JsObject = Json.obj(
    "isElected"                        -> false,
    "groupEBITDAChargeableGains"       -> false,
    "activeGroupEBITDAChargeableGains" -> false
  )

  val groupRatioModelMin: GroupRatioModel = GroupRatioModel(
    isElected = false,
    groupEBITDAChargeableGains = false,
    groupRatioBlended = None,
    activeGroupEBITDAChargeableGains = false
  )
}

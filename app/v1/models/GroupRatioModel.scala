/*
 * Copyright 2023 HM Revenue & Customs
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

package v1.models

import play.api.libs.json.{Json, OWrites, Reads}
import v1.validation.GroupRatioValidator

case class GroupRatioModel(
  isElected: Boolean,
  groupEBITDAChargeableGains: Boolean,
  activeGroupEBITDAChargeableGains: Boolean,
  groupRatioBlended: Option[GroupRatioBlendedModel]
) extends GroupRatioValidator {
  override val groupRatioModel: GroupRatioModel = this
}

object GroupRatioModel {
  implicit val writes: OWrites[GroupRatioModel] = Json.writes[GroupRatioModel]
  implicit val reads: Reads[GroupRatioModel]    = Json.reads[GroupRatioModel]
}

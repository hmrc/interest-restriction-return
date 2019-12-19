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

package models

import play.api.libs.json.Json
import validation.GroupLevelElectionValidation

case class GroupLevelElectionsModel(isElected: Boolean,
                                    groupRatioBlended: Option[GroupRatioBlendedModel],
                                    groupEBITDAChargeableGains: Option[Boolean],
                                    interestAllowanceAlternativeCalculation: Option[Boolean],
                                    interestAllowanceNonConsolidatedInvestment: Option[NonConsolidatedInvestmentElectionModel],
                                    interestAllowanceConsolidatedPartnership: Option[ConsolidatedPartnershipModel]) extends GroupLevelElectionValidation {
  override val groupLevelElectionsModel = this
}

object GroupLevelElectionsModel {
  implicit val format = Json.format[GroupLevelElectionsModel]
}
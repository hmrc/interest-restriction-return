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

package assets

import play.api.libs.json.Json
import v1.models.{Elections, InvestorGroupModel}

object InvestorGroupConstants extends Elections {

  val groupName = "some investor group"
  val groupRatioElectionList = allValues.toList
  val fixedRatioElectionList = fixedRatioValues.toList
  val allValuesJson = Seq(
    GroupRatioBlended.toString,
    GroupEBITDA.toString,
    InterestAllowanceAlternativeCalculation.toString,
    InterestAllowanceNonConsolidatedInvestment.toString,
    InterestAllowanceConsolidatedPartnership.toString
  )
  val fixedRatioValuesJson = Seq(
    InterestAllowanceAlternativeCalculation.toString,
    InterestAllowanceNonConsolidatedInvestment.toString,
    InterestAllowanceConsolidatedPartnership.toString
  )

  val investorGroupsGroupRatioModel = InvestorGroupModel(
    groupName = groupName,
    elections = Some(groupRatioElectionList)
  )

  val investorGroupsFixedRatioModel = InvestorGroupModel(
    groupName = groupName,
    elections = Some(fixedRatioElectionList)
  )

  val investorGroupsModelMin = InvestorGroupModel(
    groupName = groupName,
    elections = None
  )

  val investorGroupsJsonMin = Json.obj(
    "groupName" -> groupName
  )

  val investorGroupsGroupRatioJson = Json.obj(
    "groupName" -> groupName,
    "elections" -> allValuesJson
  )
  val investorGroupsFixedRatioJson = Json.obj(
    "groupName" -> fixedRatioValuesJson
  )

}

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

package data

import play.api.libs.json.{JsArray, JsObject, JsString, Json}
import v1.models.Elections._
import v1.models.{CompanyNameModel, Elections, InvestorGroupModel}

object InvestorGroupConstants {

  val groupName: String = "some investor group"

  val groupRatioElectionList: List[Elections] = allValues.toList
  val fixedRatioElectionList: List[Elections] = fixedRatioValues.toList

  val allValuesJson: JsArray        = Json.arr(
    JsString(GroupRatioBlended.toString),
    JsString(GroupEBITDA.toString),
    JsString(InterestAllowanceAlternativeCalculation.toString),
    JsString(InterestAllowanceNonConsolidatedInvestment.toString),
    JsString(InterestAllowanceConsolidatedPartnership.toString)
  )
  val fixedRatioValuesJson: JsArray = Json.arr(
    JsString(InterestAllowanceAlternativeCalculation.toString),
    JsString(InterestAllowanceNonConsolidatedInvestment.toString),
    JsString(InterestAllowanceConsolidatedPartnership.toString)
  )

  val investorGroupsGroupRatioModel: InvestorGroupModel = InvestorGroupModel(
    groupName = CompanyNameModel(groupName),
    elections = Some(groupRatioElectionList)
  )

  val investorGroupsFixedRatioModel: InvestorGroupModel = InvestorGroupModel(
    groupName = CompanyNameModel(groupName),
    elections = Some(fixedRatioElectionList)
  )

  val investorGroupsModelMin: InvestorGroupModel = InvestorGroupModel(
    groupName = CompanyNameModel(groupName),
    elections = None
  )

  val investorGroupsJsonMin: JsObject = Json.obj(
    "groupName" -> groupName
  )

  val investorGroupsGroupRatioJson: JsObject = Json.obj(
    "groupName" -> groupName,
    "elections" -> allValuesJson
  )

  val investorGroupsFixedRatioJson: JsObject = Json.obj(
    "groupName" -> groupName,
    "elections" -> fixedRatioValuesJson
  )
}

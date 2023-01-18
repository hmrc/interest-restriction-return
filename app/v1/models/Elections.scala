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

import play.api.libs.json.Format
import utils.enums.Enums

sealed trait Elections

object Elections {

  case object GroupRatioBlended extends WithName("groupRatioBlended") with Elections
  case object GroupEBITDA extends WithName("groupEBITDA") with Elections
  case object InterestAllowanceAlternativeCalculation
      extends WithName("interestAllowanceAlternativeCalculation")
      with Elections
  case object InterestAllowanceNonConsolidatedInvestment
      extends WithName("interestAllowanceNonConsolidatedInvestment")
      with Elections
  case object InterestAllowanceConsolidatedPartnership
      extends WithName("interestAllowanceConsolidatedPartnership")
      with Elections

  val allValues = Seq(
    GroupRatioBlended,
    GroupEBITDA,
    InterestAllowanceAlternativeCalculation,
    InterestAllowanceNonConsolidatedInvestment,
    InterestAllowanceConsolidatedPartnership
  )

  val fixedRatioValues = Seq(
    InterestAllowanceAlternativeCalculation,
    InterestAllowanceNonConsolidatedInvestment,
    InterestAllowanceConsolidatedPartnership
  )

  implicit val format: Format[Elections] = Enums.format[Elections]

}

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

package v1.models

import play.api.libs.json.*
import scala.reflect.ClassTag
import v1.models.Elections.*
import scala.language.implicitConversions

enum Elections(name: String) {
  case GroupRatioBlended extends Elections("groupRatioBlended")
  case GroupEBITDA extends Elections("groupEBITDA")
  case InterestAllowanceAlternativeCalculation extends Elections("interestAllowanceAlternativeCalculation")
  case InterestAllowanceNonConsolidatedInvestment extends Elections("interestAllowanceNonConsolidatedInvestment")
  case InterestAllowanceConsolidatedPartnership extends Elections("interestAllowanceConsolidatedPartnership")

  override implicit def toString: String = this.name
}
object Elections {

  final val allValues: Seq[Elections] = Seq(
    GroupRatioBlended,
    GroupEBITDA,
    InterestAllowanceAlternativeCalculation,
    InterestAllowanceNonConsolidatedInvestment,
    InterestAllowanceConsolidatedPartnership
  )

  final val fixedRatioValues: Seq[Elections] = Seq(
    InterestAllowanceAlternativeCalculation,
    InterestAllowanceNonConsolidatedInvestment,
    InterestAllowanceConsolidatedPartnership
  )

  implicit val format: Format[Elections] = new Format[Elections] {
    def writes(election: Elections): JsValue = JsString(election.toString)

    def reads(json: JsValue): JsResult[Elections] = json match {
      case JsString(s) =>
        try
          s match {
            case "groupRatioBlended"                          => JsSuccess(Elections.GroupRatioBlended)
            case "groupEBITDA"                                => JsSuccess(Elections.GroupEBITDA)
            case "interestAllowanceAlternativeCalculation"    =>
              JsSuccess(Elections.InterestAllowanceAlternativeCalculation)
            case "interestAllowanceNonConsolidatedInvestment" =>
              JsSuccess(Elections.InterestAllowanceNonConsolidatedInvestment)
            case "interestAllowanceConsolidatedPartnership"   =>
              JsSuccess(Elections.InterestAllowanceConsolidatedPartnership)
            case _                                            => JsError(s"Unknown value for Elections")
          }
        catch {
          case _: NoSuchElementException =>
            JsError(s"Unknown value for Elections")
        }

      case _ => JsError("String value expected")
    }
  }
}

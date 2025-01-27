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

package data.fullReturn

import data.BaseConstants
import data.fullReturn.AllocatedReactivationsConstants._
import data.fullReturn.AllocatedRestrictionsConstants._
import play.api.libs.json.{JsObject, Json}
import v1.models.fullReturn.UkCompanyModel

object UkCompanyConstants extends BaseConstants {

  val netTaxInterestExpense: BigDecimal = 20.00
  val netTaxInterestIncome: BigDecimal  = 50.00
  val taxEBITDA: BigDecimal             = 5.00

  val ukCompanyModelMax: UkCompanyModel = UkCompanyModel(
    companyName = companyName,
    utr = ctutr,
    consenting = true,
    qicElection = true,
    netTaxInterestExpense = netTaxInterestExpense,
    netTaxInterestIncome = 0,
    taxEBITDA = taxEBITDA,
    allocatedRestrictions = Some(allocatedRestrictionsModel),
    allocatedReactivations = Some(allocatedReactivationsModel),
    companyEstimateReason = None
  )

  val ukCompanyJsonMax: JsObject = Json.obj(
    "companyName"            -> companyName,
    "utr"                    -> ctutr,
    "consenting"             -> true,
    "qicElection"            -> true,
    "netTaxInterestExpense"  -> netTaxInterestExpense,
    "netTaxInterestIncome"   -> 0,
    "taxEBITDA"              -> taxEBITDA,
    "allocatedRestrictions"  -> allocatedRestrictionsJson,
    "allocatedReactivations" -> allocatedReactivationsJson
  )

  val ukCompanyModelReactivationMax: UkCompanyModel = UkCompanyModel(
    companyName = companyName,
    utr = ctutr,
    consenting = true,
    qicElection = true,
    netTaxInterestExpense = 0,
    netTaxInterestIncome = netTaxInterestIncome,
    taxEBITDA = taxEBITDA,
    allocatedRestrictions = None,
    allocatedReactivations = Some(allocatedReactivationsModel),
    companyEstimateReason = None
  )

  val ukCompanyReactivationJsonMax: JsObject = Json.obj(
    "companyName"            -> companyName,
    "utr"                    -> ctutr,
    "consenting"             -> true,
    "qicElection"            -> true,
    "netTaxInterestExpense"  -> 0,
    "netTaxInterestIncome"   -> netTaxInterestIncome,
    "taxEBITDA"              -> taxEBITDA,
    "allocatedReactivations" -> allocatedReactivationsJson
  )

  val ukCompanyModelRestrictionMax: UkCompanyModel = UkCompanyModel(
    companyName = companyName,
    utr = ctutr,
    consenting = true,
    qicElection = true,
    netTaxInterestExpense = netTaxInterestExpense,
    netTaxInterestIncome = 0,
    taxEBITDA = taxEBITDA,
    allocatedRestrictions = Some(allocatedRestrictionsModel),
    allocatedReactivations = None,
    companyEstimateReason = None
  )

  val ukCompanyRestrictionJsonMax: JsObject = Json.obj(
    "companyName"           -> companyName,
    "utr"                   -> ctutr,
    "consenting"            -> true,
    "qicElection"           -> true,
    "netTaxInterestExpense" -> netTaxInterestExpense,
    "netTaxInterestIncome"  -> 0,
    "taxEBITDA"             -> taxEBITDA,
    "allocatedRestrictions" -> allocatedRestrictionsJson
  )

  val ukCompanyModelMin: UkCompanyModel = UkCompanyModel(
    companyName = companyName,
    utr = ctutr,
    consenting = true,
    qicElection = true,
    netTaxInterestExpense = 0,
    netTaxInterestIncome = netTaxInterestIncome,
    taxEBITDA = taxEBITDA,
    allocatedRestrictions = None,
    allocatedReactivations = None,
    companyEstimateReason = None
  )

  val ukCompanyJsonMin: JsObject = Json.obj(
    "companyName"           -> companyName,
    "utr"                   -> ctutr,
    "consenting"            -> true,
    "qicElection"           -> true,
    "netTaxInterestExpense" -> 0,
    "netTaxInterestIncome"  -> netTaxInterestIncome,
    "taxEBITDA"             -> taxEBITDA
  )
}

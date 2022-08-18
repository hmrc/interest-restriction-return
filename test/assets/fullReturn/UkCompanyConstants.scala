/*
 * Copyright 2022 HM Revenue & Customs
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

package assets.fullReturn

import assets.BaseConstants
import assets.fullReturn.AllocatedReactivationsConstants._
import assets.fullReturn.AllocatedRestrictionsConstants._
import play.api.libs.json.Json
import v1.models.fullReturn.UkCompanyModel

object UkCompanyConstants extends BaseConstants {

  val netTaxInterestExpense: BigDecimal = 20.00
  val netTaxInterestIncome: BigDecimal  = 50.00
  val taxEBITDA: BigDecimal             = 5.00

  val ukCompanyModelMax = UkCompanyModel(
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

  val ukCompanyJsonMax = Json.obj(
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

  val ukCompanyModelReactivationMax = UkCompanyModel(
    companyName = companyName,
    utr = ctutr,
    consenting = true,
    qicElection = true,
    netTaxInterestExpense = 0,
    netTaxInterestIncome = netTaxInterestIncome, // £50
    taxEBITDA = taxEBITDA,
    allocatedRestrictions = None,
    allocatedReactivations = Some(allocatedReactivationsModel), // £2.00
    companyEstimateReason = None
  )

  val ukCompanyReactivationJsonMax = Json.obj(
    "companyName"            -> companyName,
    "utr"                    -> ctutr,
    "consenting"             -> true,
    "qicElection"            -> true,
    "netTaxInterestExpense"  -> 0,
    "netTaxInterestIncome"   -> netTaxInterestIncome,
    "taxEBITDA"              -> taxEBITDA,
    "allocatedReactivations" -> allocatedReactivationsJson
  )

  val ukCompanyModelRestrictionMax = UkCompanyModel(
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

  val ukCompanyRestrictionJsonMax = Json.obj(
    "companyName"           -> companyName,
    "utr"                   -> ctutr,
    "consenting"            -> true,
    "qicElection"           -> true,
    "netTaxInterestExpense" -> netTaxInterestExpense,
    "netTaxInterestIncome"  -> 0,
    "taxEBITDA"             -> taxEBITDA,
    "allocatedRestrictions" -> allocatedRestrictionsJson
  )

  val ukCompanyModelMin = UkCompanyModel(
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

  val ukCompanyJsonMin = Json.obj(
    "companyName"           -> companyName,
    "utr"                   -> ctutr,
    "consenting"            -> true,
    "qicElection"           -> true,
    "netTaxInterestExpense" -> 0,
    "netTaxInterestIncome"  -> netTaxInterestIncome,
    "taxEBITDA"             -> taxEBITDA
  )
}

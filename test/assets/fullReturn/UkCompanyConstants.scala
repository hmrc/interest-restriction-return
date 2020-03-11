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

package assets.fullReturn

import assets.BaseConstants
import assets.fullReturn.AllocatedReactivationsConstants._
import assets.fullReturn.AllocatedRestrictionsConstants._
import play.api.libs.json.Json
import v1.models.fullReturn.UkCompanyModel

object UkCompanyConstants extends BaseConstants {

  val netTaxInterestExpense: BigDecimal = 20.11
  val netTaxInterestIncome: BigDecimal = 20.11
  val taxEBITDA: BigDecimal = 3.33

  val ukCompanyModelMax = UkCompanyModel(
    companyName = companyName,
    ctutr = ctutr,
    consenting = true,
    netTaxInterestExpense = netTaxInterestExpense,
    netTaxInterestIncome = 0,
    taxEBITDA = taxEBITDA,
    allocatedRestrictions = Some(allocatedRestrictionsModel),
    allocatedReactivations = Some(allocatedReactivationsModel)
  )

  val ukCompanyJsonMax = Json.obj(
    "companyName" -> companyName,
    "ctutr" -> ctutr,
    "consenting" -> true,
    "netTaxInterestExpense" -> netTaxInterestExpense,
    "netTaxInterestIncome" -> 0,
    "taxEBITDA" -> taxEBITDA,
    "allocatedRestrictions" -> allocatedRestrictionsJson,
    "allocatedReactivations" -> allocatedReactivationsJson
  )

  val ukCompanyModelReactivationMax = UkCompanyModel(
    companyName = companyName,
    ctutr = ctutr,
    consenting = true,
    netTaxInterestExpense = netTaxInterestExpense,
    netTaxInterestIncome = 0,
    taxEBITDA = taxEBITDA,
    allocatedRestrictions = None,
    allocatedReactivations = Some(allocatedReactivationsModel)
  )

  val ukCompanyReactivationJsonMax = Json.obj(
    "companyName" -> companyName,
    "ctutr" -> ctutr,
    "consenting" -> true,
    "netTaxInterestExpense" -> netTaxInterestExpense,
    "netTaxInterestIncome" -> 0,
    "taxEBITDA" -> taxEBITDA,
    "allocatedReactivations" -> allocatedReactivationsJson
  )

  val ukCompanyModelRestrictionMax = UkCompanyModel(
    companyName = companyName,
    ctutr = ctutr,
    consenting = true,
    netTaxInterestExpense = netTaxInterestExpense,
    netTaxInterestIncome = 0,
    taxEBITDA = taxEBITDA,
    allocatedRestrictions = Some(allocatedRestrictionsModel),
    allocatedReactivations = None
  )

  val ukCompanyRestrictionJsonMax = Json.obj(
    "companyName" -> companyName,
    "ctutr" -> ctutr,
    "consenting" -> true,
    "netTaxInterestExpense" -> netTaxInterestExpense,
    "netTaxInterestIncome" -> 0,
    "taxEBITDA" -> taxEBITDA,
    "allocatedRestrictions" -> allocatedRestrictionsJson
  )

  val ukCompanyModelMin = UkCompanyModel(
    companyName = companyName,
    ctutr = ctutr,
    consenting = true,
    netTaxInterestExpense = 0,
    netTaxInterestIncome = netTaxInterestIncome,
    taxEBITDA = taxEBITDA,
    allocatedRestrictions = None,
    allocatedReactivations = None
  )

  val ukCompanyJsonMin = Json.obj(
    "companyName" -> companyName,
    "ctutr" -> ctutr,
    "consenting" -> true,
    "netTaxInterestExpense" -> 0,
    "netTaxInterestIncome" -> netTaxInterestIncome,
    "taxEBITDA" -> taxEBITDA
  )
}

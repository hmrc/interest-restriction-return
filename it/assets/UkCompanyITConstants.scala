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

package assets

import play.api.libs.json.Json
import assets.AllocatedReactivationsITConstants._
import assets.AllocatedRestrictionsITConstants._
object UkCompanyITConstants extends BaseITConstants {


  val netTaxInterestExpense: BigDecimal = 0
  val netTaxInterestIncome: BigDecimal = 2.22
  val taxEBITDA: BigDecimal = 3.33

  val ukCompanyJson = Json.obj(
    "companyName" -> companyName,
    "utr" -> ctutr,
    "consenting" -> true,
    "qicElection" -> false
  )

  val ukCompanyFullJson = Json.obj(
    "companyName" -> companyName,
    "utr" -> ctutr,
    "consenting" -> true,
    "qicElection" -> false,
    "netTaxInterestExpense" -> netTaxInterestExpense,
    "netTaxInterestIncome" -> netTaxInterestIncome,
    "taxEBITDA" -> taxEBITDA,
    "allocatedReactivations" -> allocatedReactivationsJson
  )
}

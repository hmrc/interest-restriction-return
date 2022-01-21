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

package assets

import assets.NonConsolidatedInvestmentConstants._
import play.api.libs.json.Json
import v1.models.NonConsolidatedInvestmentElectionModel

object NonConsolidatedInvestmentElectionConstants {

  val nonConsolidatedInvestmentModelMax = NonConsolidatedInvestmentElectionModel(
    isElected = true,
    nonConsolidatedInvestments = Some(Seq(nonConsolidatedModel))
  )

  val nonConsolidatedInvestmentJsonMax = Json.obj(
    "isElected" -> true,
    "nonConsolidatedInvestments" -> Json.arr(nonConsolidatedJson)
  )

  val nonConsolidatedInvestmentModelMin = NonConsolidatedInvestmentElectionModel(
    isElected = false,
    nonConsolidatedInvestments = None
  )

  val nonConsolidatedInvestmentJsonMin = Json.obj(
    "isElected" -> false
  )
}

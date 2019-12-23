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

package assets.fullReturn

import assets.BaseConstants
import models.fullReturn.AdjustedGroupInterestModel
import play.api.libs.json.Json

object AdjustedGroupInterestConstants extends BaseConstants {

  val qngie: BigDecimal = 100
  val groupEBITDA: BigDecimal = 200
  val groupRatio: BigDecimal = (qngie / groupEBITDA) * 100

  val adjustedGroupInterestModel = AdjustedGroupInterestModel(
    qngie = qngie,
    groupEBITDA = groupEBITDA,
    groupRatio = groupRatio
  )

  val adjustedGroupInterestJson = Json.obj(
    "qngie" -> qngie,
    "groupEBITDA" -> groupEBITDA,
    "groupRatio" -> groupRatio
  )

}

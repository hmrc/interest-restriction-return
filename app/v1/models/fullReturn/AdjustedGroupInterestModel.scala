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

package v1.models.fullReturn

import play.api.libs.json._
import v1.validation.fullReturn.AdjustedGroupInterestValidator

case class AdjustedGroupInterestModel(qngie: BigDecimal, groupEBITDA: Option[BigDecimal], groupRatio: BigDecimal)
    extends AdjustedGroupInterestValidator {
  override val adjustedGroupInterestModel: AdjustedGroupInterestModel = this
}

object AdjustedGroupInterestModel {
  implicit val adjustedGroupInterestModelReads: Reads[AdjustedGroupInterestModel]   =
    Json.reads[AdjustedGroupInterestModel]
  implicit val adjustedGroupInterestModelWrites: Writes[AdjustedGroupInterestModel] = Writes { models =>
    val ebitda = models.groupEBITDA.getOrElse(BigDecimal(0))
    JsObject(
      Json
        .obj(
          "qngie"       -> models.qngie,
          "groupEBITDA" -> ebitda,
          "groupRatio"  -> models.groupRatio
        )
        .fields
        .filterNot(_._2 == JsNull)
    )
  }
}

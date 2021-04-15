/*
 * Copyright 2021 HM Revenue & Customs
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

import java.time.LocalDate
import play.api.libs.json.{Format, JsNull, JsObject, Json, Writes}
import v1.models.fullReturn.FullReturnModel.writes
import v1.validation.fullReturn.AllocatedRestrictionsValidator

case class AllocatedRestrictionsModel(ap1EndDate: LocalDate,
                                      ap2EndDate: Option[LocalDate],
                                      ap3EndDate: Option[LocalDate],
                                      disallowanceAp1: BigDecimal,
                                      disallowanceAp2: Option[BigDecimal],
                                      disallowanceAp3: Option[BigDecimal],
                                      totalDisallowances: Option[BigDecimal]
                                     ) extends AllocatedRestrictionsValidator {
  override val allocatedRestrictionsModel: AllocatedRestrictionsModel = this
}

object AllocatedRestrictionsModel {

  val writes: Writes[AllocatedRestrictionsModel] = Writes { model =>
    JsObject(Json.obj(
      "ap1EndDate" -> model.ap1EndDate,
      "ap2EndDate" -> model.ap2EndDate,
      "ap3EndDate" -> model.ap3EndDate,
      "disallowanceAp1" -> model.disallowanceAp1,
      "disallowanceAp2" -> model.disallowanceAp2,
      "disallowanceAp3" -> model.disallowanceAp3,
      "totalDisallowances" -> model.totalDisallowances
    ).fields.filterNot(_._2 == JsNull))
  }

  implicit val format: Format[AllocatedRestrictionsModel] = Format[AllocatedRestrictionsModel](Json.reads[AllocatedRestrictionsModel], writes)
}


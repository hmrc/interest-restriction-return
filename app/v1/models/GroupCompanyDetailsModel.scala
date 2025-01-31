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
import v1.validation.GroupCompanyDetailsValidator

case class GroupCompanyDetailsModel(accountingPeriod: AccountingPeriodModel) extends GroupCompanyDetailsValidator {
  override val groupCompanyDetails: GroupCompanyDetailsModel = this
}

object GroupCompanyDetailsModel {
  val writes: Writes[GroupCompanyDetailsModel] = Writes { models =>
    JsObject(
      Json
        .obj(
          "totalCompanies"   -> 1,
          "accountingPeriod" -> models.accountingPeriod
        )
        .fields
        .filterNot(_._2 == JsNull)
    )
  }

  implicit val format: Format[GroupCompanyDetailsModel] =
    Format[GroupCompanyDetailsModel](Json.reads[GroupCompanyDetailsModel], writes)

}

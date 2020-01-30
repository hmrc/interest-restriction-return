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

package v1.models

import play.api.libs.json.{Format, Json}
import v1.validation.DeemedParentValidator

case class DeemedParentModel(companyName: CompanyNameModel,
                             knownAs: Option[String],
                             ctutr: Option[UTRModel],
                             sautr: Option[UTRModel],
                             crn: Option[CRNModel],
                             countryOfIncorporation: Option[CountryCodeModel],
                             nonUkCrn: Option[String])
  extends DeemedParentValidator {
  override val deemedParentModel: DeemedParentModel = this
}


object DeemedParentModel {
  implicit def format: Format[DeemedParentModel] = Json.format[DeemedParentModel]
}

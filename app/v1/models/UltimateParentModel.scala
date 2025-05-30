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

package v1.models

import play.api.libs.json.{Format, Json}
import v1.validation.UltimateParentValidator

case class UltimateParentModel(
  companyName: CompanyNameModel,
  isUk: Boolean,
  ctutr: Option[UTRModel],
  sautr: Option[UTRModel],
  countryOfIncorporation: Option[CountryCodeModel],
  legalEntityIdentifier: Option[LegalEntityIdentifierModel]
) extends UltimateParentValidator {
  override val ultimateParentModel: UltimateParentModel = this
}

object UltimateParentModel {

  implicit def format: Format[UltimateParentModel] = Json.format[UltimateParentModel]
}

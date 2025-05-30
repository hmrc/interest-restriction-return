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

package v1.models.abbreviatedReturn

import play.api.libs.json.{Json, OFormat}
import v1.models.{CompanyNameModel, UTRModel}
import v1.validation.abbreviatedReturn.UkCompanyValidator

case class UkCompanyModel(companyName: CompanyNameModel, utr: UTRModel, consenting: Boolean, qicElection: Boolean)
    extends UkCompanyValidator {
  override val ukCompany: UkCompanyModel = this
}

object UkCompanyModel {
  implicit val format: OFormat[UkCompanyModel] = Json.format[UkCompanyModel]
}

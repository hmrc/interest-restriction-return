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

package v1.schemas.helpers

import play.api.libs.json.Json
import v1.models.{CRNModel, CountryCodeModel, UTRModel}

case class UltimateParent(isUk: Option[Boolean] = Some(true),
                          companyName: Option[String] = Some("Ladon ltd"),
                          knownAs: Option[String] = Some("TwixBar"),
                          ctutr: Option[UTRModel] = Some(UTRModel("1234567890")),
                          sautr: Option[UTRModel] = Some(UTRModel("1234567890")),
                          crn: Option[CRNModel] = Some(CRNModel("AB123456")),
                          countryOfIncorporation: Option[CountryCodeModel] = Some(CountryCodeModel("US")),
                          nonUkCrn: Option[String] = Some("Aa1234567890")
                         )

object UltimateParent {
  implicit val writes = Json.writes[UltimateParent]
}



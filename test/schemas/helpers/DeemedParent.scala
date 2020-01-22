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

package schemas.helpers

import play.api.libs.json.Json
import v1.models.{CRNModel, UTRModel}

case class DeemedParent(isUk: Option[Boolean] = Some(true),
                        companyName: Option[String] = Some("name"),
                        knownAs: Option[String] = Some("efg"),
                        ctutr: Option[UTRModel] = Some(UTRModel("1111111111")),
                        sautr: Option[UTRModel] = Some(UTRModel("1111111111")),
                        crn: Option[CRNModel] = Some(CRNModel("AB123456")),
                        countryOfIncorporation: Option[String] = Some("US"),
                        nonUkCrn: Option[String] = Some("12345678")
                       )

object DeemedParent {
  implicit val writes = Json.writes[DeemedParent]
}
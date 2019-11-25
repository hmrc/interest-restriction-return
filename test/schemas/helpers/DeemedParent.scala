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

package schemas.helpers

import play.api.libs.json.{Json, Writes}

sealed trait DeemedParent

case class UkDeemedParent(companyName: Option[String] = Some("name"),
                          knownAs: Option[String] = Some("efg"),
                          ctutr: Option[String] = Some("1111111111"),
                          crn: Option[String] = Some("AB123456"),
                          sautr: Option[String] = Some("1111111111")
                         ) extends DeemedParent

object UkDeemedParent {
  implicit val writes = Json.writes[UkDeemedParent]
}

case class NonUkDeemedParent(companyName: Option[String] = Some("name"),
                             knownAs: Option[String] = Some("efg"),
                             countryOfIncorporation: Option[String] = Some("US"),
                             crn: Option[String] = Some("AB123456")
                            ) extends DeemedParent

object NonUkDeemedParent {
  implicit val writes = Json.writes[NonUkDeemedParent]
}

object DeemedParent {
  implicit def writes: Writes[DeemedParent] = Writes {
    case x: UkDeemedParent => Json.toJson(x)(UkDeemedParent.writes)
    case x: NonUkDeemedParent => Json.toJson(x)(NonUkDeemedParent.writes)
  }
}
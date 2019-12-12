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

sealed trait UltimateParent

case class UkUltimateParent(registeredCompanyName: Option[String] = Some("cde ltd"),
                            knownAs: Option[String] = Some("efg"),
                            ctutr: Option[String] = Some("1234567890"),
                            crn: Option[String] = Some("AB123456"),
                            sautr: Option[String] = Some("1234567890")
                           ) extends UltimateParent

object UkUltimateParent {
  implicit val writes = Json.writes[UkUltimateParent]
}

case class NonUkUltimateParent(registeredCompanyName: Option[String] = Some("cde ltd"),
                               knownAs: Option[String] = Some("efg"),
                               countryOfIncorporation: Option[String] = Some("US"),
                               crn: Option[String] = Some("AB123456")
                              ) extends UltimateParent

object NonUkUltimateParent {
  implicit val writes = Json.writes[NonUkUltimateParent]
}

case class OptionalUkUltimateParent(registeredCompanyName: Option[String] = Some("cde ltd"),
                                    cturt:Option[String] = Some("1234567890"),
                                    crn: Option[String] = Some("AB123456")
                                   ) extends UltimateParent

object OptionalUkUltimateParent {
  implicit val writes = Json.writes[OptionalUkUltimateParent]
}

case class OptionalNonUkUltimateParent(registeredCompanyName: Option[String] = Some("cde ltd"),
                                       countryOfIncorporation: Option[String] = Some("US"),
                                       localCompanyNumber: Option[Boolean] = Some(true)
                                      ) extends UltimateParent

object OptionalNonUkUltimateParent {
  implicit val writes = Json.writes[OptionalNonUkUltimateParent]
}

object UltimateParent {
  implicit def writes: Writes[UltimateParent] = Writes {
    case x: UkUltimateParent => Json.toJson(x)(UkUltimateParent.writes)
    case x: NonUkUltimateParent => Json.toJson(x)(NonUkUltimateParent.writes)
    case x: OptionalUkUltimateParent => Json.toJson(x)(OptionalUkUltimateParent.writes)
    case x: OptionalNonUkUltimateParent => Json.toJson(x)(OptionalNonUkUltimateParent.writes)
  }
}
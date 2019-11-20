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

package schemas.helpers.fullReturn

import java.time.LocalDate

import play.api.libs.json.Json

case class AllocatedRestrictions(ap1End: Option[LocalDate] = Some(LocalDate.parse("2019-11-11")),
                                 ap2End: Option[LocalDate] = Some(LocalDate.parse("2019-12-11")),
                                 ap3End: Option[LocalDate] = Some(LocalDate.parse("2020-03-01")),
                                 disallowanceAp1: Option[BigDecimal] = Some(3000),
                                 disallowanceAp2: Option[BigDecimal] = Some(4000),
                                 disallowanceAp3: Option[BigDecimal] = Some(5000))

object AllocatedRestrictions {
  implicit val format = Json.format[AllocatedRestrictions]
}
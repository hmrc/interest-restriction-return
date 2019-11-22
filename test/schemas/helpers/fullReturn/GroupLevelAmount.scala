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

import play.api.libs.json.Json

case class GroupLevelAmount(totalDisallowedAmount: Option[BigDecimal] = Some(120000),
                            interestReactivationCap: Option[BigDecimal]= Some(125000),
                            interestAllowanceForward: Option[BigDecimal] =Some(130000),
                            interestAllowanceForPeriod: Option[BigDecimal] = Some(140000),
                            interestCapacityForPeriod: Option[BigDecimal] = Some(150000))

object GroupLevelAmount {
  implicit val format = Json.format[GroupLevelAmount]
}
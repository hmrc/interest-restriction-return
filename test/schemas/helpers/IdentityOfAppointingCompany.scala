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

import play.api.libs.json.Json

case class IdentityOfAppointingCompany(companyName: Option[String] = Some("identity of an Appointing Company"),
                                            ctutr: Option[String] = Some("1234567890"),
                                            crn: Option[String] = Some("1234567890"),
                                            companyOfIncorporation: Option[String] =  Some("US"),
                                            localCompanyNumber: Option[Boolean] = Some(true)
                                            )

object IdentityOfAppointingCompany {
  implicit val writes = Json.writes[IdentityOfAppointingCompany]
}
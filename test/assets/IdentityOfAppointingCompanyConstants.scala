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

package assets

import models.IdentityOfAppointingCompanyModel
import play.api.libs.json.Json

object IdentityOfAppointingCompanyConstants extends BaseConstants {

  val identityOfAppointingCompanyName = "some other appointing company"
  val countryCode = "US"

  val identityOfAppointingCompanyJsonMax = Json.obj(
    "companyName" -> identityOfAppointingCompanyName,
    "ctutr" -> ctutr,
    "crn" -> crn,
    "countryOfIncorporation" -> "US",
    "localCompanyNumber" -> true
  )

  val identityOfAppointingCompanyModelMax = IdentityOfAppointingCompanyModel(
    companyName = identityOfAppointingCompanyName,
    ctutr = Some(ctutr),
    crn = Some(crn),
    countryOfIncorporation = Some(countryCode),
    localCompanyNumber = Some(true)
  )

  val identityOfAppointingCompanyJsonMin = Json.obj(
    "companyName" -> identityOfAppointingCompanyName,
    "localCompanyNumber" -> true
  )

  val identityOfAppointingCompanyModelMin = IdentityOfAppointingCompanyModel(
    companyName = identityOfAppointingCompanyName,
    ctutr = None,
    crn = None,
    countryOfIncorporation = None,
    localCompanyNumber = None
  )
}

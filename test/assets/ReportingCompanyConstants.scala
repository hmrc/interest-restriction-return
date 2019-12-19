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

import models.ReportingCompanyModel
import play.api.libs.json.Json

object ReportingCompanyConstants extends BaseConstants {

  val reportingCompanyName = "some reporting company"

  val reportingCompanyJsonMax = Json.obj(
    "companyName" -> companyName,
    "ctutr" -> ctutr,
    "crn" -> crn,
    "sameAsUltimateParent" -> false
  )

  val reportingCompanyModelMax = ReportingCompanyModel(
    companyName = companyName,
    ctutr = ctutr,
    crn = Some(crn),
    sameAsUltimateParent = false
  )

  val reportingCompanyJsonMin = Json.obj(
    "companyName" -> companyName,
    "ctutr" -> ctutr,
    "sameAsUltimateParent" -> true
  )

  val reportingCompanyModelMin = ReportingCompanyModel(
    companyName = companyName,
    ctutr = ctutr,
    crn = None,
    sameAsUltimateParent = true
  )
}

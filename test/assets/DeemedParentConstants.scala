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

import models.DeemedParentModel
import play.api.libs.json.Json

object DeemedParentConstants extends BaseConstants {

  val companyName = "some company ltd"
  val knownAs = "some company"
  val nonUkCountryCode = "US"

  val deemedParentModelMax = DeemedParentModel(
    companyName = companyName,
    ctutr = Some(ctutr),
    crn = Some(crn),
    knownAs = Some(knownAs),
    countryOfIncorporation = Some(nonUkCountryCode)
  )

  val deemedParentJsonMax = Json.obj(
    "companyName" -> companyName,
    "ctutr" -> ctutr,
    "crn" -> crn,
    "knownAs" -> knownAs,
    "countryOfIncorporation" -> nonUkCountryCode
  )

  val deemedParentModelMin = DeemedParentModel(
    companyName = companyName,
    ctutr = None,
    crn = None,
    knownAs = None,
    countryOfIncorporation = None
  )

  val deemedParentJsonMin = Json.obj(
    "companyName" -> companyName
  )
}

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

package assets

import models.DeemedParentModel
import play.api.libs.json.Json

object DeemedParentConstants extends BaseConstants {

  val deemedParentModelMax = DeemedParentModel(
    isUk = true,
    companyName = companyName,
    knownAs = Some(knownAs),
    ctutr = Some(ctutr),
    sautr = Some(sautr),
    crn = Some(crn),
    nonUkCrn = Some(nonUkCrn),
    countryOfIncorporation = Some(nonUkCountryCode)
  )

  val deemedParentModelMin = DeemedParentModel(
    isUk = true,
    companyName = companyName,
    knownAs = None,
    ctutr = None,
    crn = None,
    sautr = None,
    nonUkCrn = None,
    countryOfIncorporation = None
  )

  val deemedParentModelUkCompany = DeemedParentModel(
    isUk = true,
    companyName = companyName,
    knownAs = Some(knownAs),
    ctutr = Some(ctutr),
    sautr = None,
    crn = Some(crn),
    nonUkCrn = None,
    countryOfIncorporation = None
  )

  val deemedParentModelNonUkCompany = DeemedParentModel(
    isUk = false,
    companyName = companyName,
    knownAs = Some(knownAs),
    ctutr = None,
    sautr = None,
    crn = None,
    nonUkCrn = Some(nonUkCrn),
    countryOfIncorporation = Some(nonUkCountryCode)
  )

  val deemedParentModelUkPartnership = DeemedParentModel(
    isUk = true,
    companyName = companyName,
    knownAs = Some(knownAs),
    ctutr = None,
    sautr = Some(sautr),
    crn = Some(crn),
    nonUkCrn = None,
    countryOfIncorporation = None
  )

  val deemedParentJsonMax = Json.obj(
    "isUk" -> true,
    "companyName" -> companyName,
    "knownAs" -> knownAs,
    "ctutr" -> ctutr,
    "sautr" -> sautr,
    "crn" -> crn,
    "nonUkCrn" -> nonUkCrn,
    "countryOfIncorporation" -> Some(nonUkCountryCode)
  )

  val deemedParentJsonMin = Json.obj(
    "isUk" -> true,
    "companyName" -> companyName
  )

  val deemedParentJsonUkCompany = Json.obj(
    "isUk" -> true,
    "companyName" -> companyName,
    "knownAs" -> knownAs,
    "ctutr" -> ctutr,
    "crn" -> crn
  )

  val deemedParentJsonNonUkCompany= Json.obj(
    "isUk" -> false,
    "companyName" -> companyName,
    "knownAs" -> knownAs,
    "nonUkCrn" -> crn,
    "countryOfIncorporation" -> nonUkCountryCode
  )

  val deemedParentJsonUkPartnership = Json.obj(
    "isUk" -> false,
    "companyName" -> companyName,
    "knownAs" -> knownAs,
    "sautr" -> sautr,
    "crn" -> crn
  )
}

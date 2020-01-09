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

import models.UltimateParentModel
import play.api.libs.json.Json

object UltimateParentConstants extends BaseConstants {

  val otherUkTaxReference = "other reference"

  val ultimateParentModelMax = UltimateParentModel(
    isUk = true,
    companyName = companyName,
    knownAs = Some(knownAs),
    ctutr = Some(ctutr),
    sautr = Some(sautr),
    crn = Some(crn),
    nonUkCrn = Some(nonUkCrn),
    countryOfIncorporation = Some(nonUkCountryCode)
  )

  val ultimateParentModelMin = UltimateParentModel(
    isUk = true,
    companyName = companyName,
    knownAs = None,
    ctutr = None,
    sautr = None,
    crn = None,
    nonUkCrn = None,
    countryOfIncorporation = None
  )

  val ultimateParentModelUkCompany = UltimateParentModel(
    isUk = true,
    companyName = companyName,
    knownAs = Some(knownAs),
    ctutr = Some(ctutr),
    sautr = None,
    crn = Some(crn),
    nonUkCrn = None,
    countryOfIncorporation = None
  )

  val ultimateParentModelNonUkCompany = UltimateParentModel(
    isUk = false,
    companyName = companyName,
    knownAs = Some(knownAs),
    ctutr = None,
    sautr = None,
    crn = None,
    nonUkCrn = Some(nonUkCrn),
    countryOfIncorporation = Some(nonUkCountryCode)
  )

  val ultimateParentModelUkPartnership = UltimateParentModel(
    isUk = true,
    companyName = companyName,
    knownAs = Some(knownAs),
    ctutr = None,
    sautr = Some(sautr),
    crn = Some(crn),
    nonUkCrn = None,
    countryOfIncorporation = None
  )

  val ultimateParentJsonMax = Json.obj(
    "isUk" -> true,
    "companyName" -> companyName,
    "knownAs" -> knownAs,
    "ctutr" -> ctutr,
    "sautr" -> sautr,
    "crn" -> crn,
    "nonUkCrn" -> nonUkCrn,
    "countryOfIncorporation" -> nonUkCountryCode
  )

  val ultimateParentJsonMin = Json.obj(
    "isUk" -> true,
    "companyName" -> companyName
  )

  val ultimateParentJsonUkCompany = Json.obj(
    "isUk" -> true,
    "companyName" -> companyName,
    "knownAs" -> knownAs,
    "ctutr" -> ctutr,
    "crn" -> crn
  )

  val ultimateParentJsonNonUkCompany = Json.obj(
    "isUk" -> false,
    "companyName" -> companyName,
    "knownAs" -> knownAs,
    "nonUkCrn" -> nonUkCrn,
    "countryOfIncorporation" -> nonUkCountryCode
  )

  val ultimateParentJsonUkPartnership = Json.obj(
    "isUk" -> true,
    "companyName" -> companyName,
    "knownAs" -> knownAs,
    "sautr" -> ctutr,
    "crn" -> crn
  )
}

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

import play.api.libs.json.Json
import v1.models.UltimateParentModel

object UltimateParentConstants extends BaseConstants {

  val otherUkTaxReference = "other reference"

  val ultimateParentModelMax = UltimateParentModel(
    companyName = companyName,
    knownAs = Some(knownAs),
    ctutr = Some(ctutr),
    sautr = Some(sautr),
    crn = Some(crnLetters),
    nonUkCrn = Some(nonUkCrn),
    countryOfIncorporation = Some(nonUkCountryCode)
  )

  val ultimateParentModelMin = UltimateParentModel(
    companyName = companyName,
    knownAs = None,
    ctutr = None,
    sautr = None,
    crn = None,
    nonUkCrn = None,
    countryOfIncorporation = None
  )

  val ultimateParentModelUkCompany = UltimateParentModel(
    companyName = companyName,
    knownAs = Some(knownAs),
    ctutr = Some(ctutr),
    sautr = None,
    crn = Some(crnLetters),
    nonUkCrn = None,
    countryOfIncorporation = None
  )

  val ultimateParentModelNonUkCompany = UltimateParentModel(
    companyName = companyName,
    knownAs = Some(knownAs),
    ctutr = None,
    sautr = None,
    crn = None,
    nonUkCrn = Some(nonUkCrn),
    countryOfIncorporation = Some(nonUkCountryCode)
  )

  val ultimateParentModelUkPartnership = UltimateParentModel(
    companyName = companyName,
    knownAs = Some(knownAs),
    ctutr = None,
    sautr = Some(sautr),
    crn = Some(crnLetters),
    nonUkCrn = None,
    countryOfIncorporation = None
  )

  val ultimateParentJsonMax = Json.obj(
    "companyName" -> companyName,
    "knownAs" -> knownAs,
    "ctutr" -> ctutr,
    "sautr" -> sautr,
    "crn" -> crnLetters,
    "nonUkCrn" -> nonUkCrn,
    "countryOfIncorporation" -> nonUkCountryCode
  )

  val ultimateParentJsonMin = Json.obj(
    "companyName" -> companyName
  )

  val ultimateParentJsonUkCompany = Json.obj(
    "companyName" -> companyName,
    "knownAs" -> knownAs,
    "ctutr" -> ctutr,
    "crn" -> crnLetters
  )

  val ultimateParentJsonNonUkCompany = Json.obj(
    "companyName" -> companyName,
    "knownAs" -> knownAs,
    "nonUkCrn" -> nonUkCrn,
    "countryOfIncorporation" -> nonUkCountryCode
  )

  val ultimateParentJsonUkPartnership = Json.obj(
    "companyName" -> companyName,
    "knownAs" -> knownAs,
    "sautr" -> ctutr,
    "crn" -> crnLetters
  )
}

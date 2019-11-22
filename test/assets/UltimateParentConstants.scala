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

import models.{NonUkParentModel, UkParentModel}
import play.api.libs.json.Json

object UltimateParentConstants extends BaseConstants {

  val registeredCompanyName = "some company"
  val knownAs = "something"
  val otherUkTaxReference = "other reference"

  val ukParentModelMax = UkParentModel(
    registeredCompanyName = registeredCompanyName,
    ctutr = Some(utr),
    crn = crn,
    knownAs = Some(knownAs),
    sautr = Some(otherUkTaxReference)
  )

  val ukParentJsonMax = Json.obj(
    "registeredCompanyName" -> registeredCompanyName,
    "ctutr" -> utr,
    "crn" -> crn,
    "knownAs" -> knownAs,
    "sautr" -> otherUkTaxReference
  )

  val nonUkParentModelMax = NonUkParentModel(
    registeredCompanyName = registeredCompanyName,
    knownAs = Some(knownAs),
    countryOfIncorporation = "US",
    crn = crn
  )

  val nonUkParentJsonMax = Json.obj(
    "registeredCompanyName" -> registeredCompanyName,
    "knownAs" -> knownAs,
    "countryOfIncorporation" -> "US",
    "crn" -> crn
  )

  val ukParentModelMin = UkParentModel(
    registeredCompanyName = registeredCompanyName,
    ctutr = None,
    crn = crn,
    knownAs = None,
    sautr = None
  )

  val ukParentJsonMin = Json.obj(
    "registeredCompanyName" -> registeredCompanyName,
    "crn" -> crn
  )

  val nonUkParentModelMin = NonUkParentModel(
    registeredCompanyName = registeredCompanyName,
    knownAs = None,
    countryOfIncorporation = "US",
    crn = crn
  )

  val nonUkParentJsonMin = Json.obj(
    "registeredCompanyName" -> registeredCompanyName,
    "countryOfIncorporation" -> "US",
    "crn" -> crn
  )
}

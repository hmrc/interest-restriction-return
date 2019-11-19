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

import models.abbreviatedReturn.{NonUkParentModel, UkParentModel}
import play.api.libs.json.Json

object UltimateParentConstants extends BaseConstants {

  val registeredCompanyName = "some company"
  val knownAs = "something"
  val otherUkTaxReference = "other reference"

  val ukParentModel = UkParentModel(
    registeredCompanyName = registeredCompanyName,
    utr = utr,
    crn = crn,
    knownAs = Some(knownAs),
    otherUkTaxReference = Some(otherUkTaxReference)
  )

  val ukParentJson = Json.obj(
    "registeredCompanyName" -> registeredCompanyName,
    "utr" -> utr,
    "crn" -> crn,
    "knownAs" -> knownAs,
    "otherUkTaxReference" -> otherUkTaxReference
  )

  val nonUkParentModel = NonUkParentModel(
    registeredCompanyName = registeredCompanyName,
    knownAs = Some(knownAs),
    countryOfIncorporation = "US",
    crn = crn
  )

  val nonUkParentJson = Json.obj(
    "registeredCompanyName" -> registeredCompanyName,
    "knownAs" -> knownAs,
    "countryOfIncorporation" -> "US",
    "crn" -> crn
  )
}

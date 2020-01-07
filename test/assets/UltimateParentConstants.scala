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

  val knownAs = "something"
  val otherUkTaxReference = "other reference"

  val ultimateParentModelMax = UltimateParentModel(
    registeredCompanyName = companyName,
    ctutr = Some(ctutr),
    crn = Some(crn),
    knownAs = Some(knownAs),
    countryOfIncorporation = None,
    nonUkCrn = None
  )

  val ultimateParentJsonMax = Json.obj(
    "registeredCompanyName" -> companyName,
    "ctutr" -> ctutr,
    "crn" -> crn,
    "knownAs" -> knownAs
  )

  val ultimateParentModelMin = UltimateParentModel(
    registeredCompanyName = companyName,
    ctutr = None,
    crn = None,
    knownAs = None,
    countryOfIncorporation = None,
    nonUkCrn = None
  )

  val ultimateParentJsonMin = Json.obj(
    "registeredCompanyName" -> companyName
  )
}

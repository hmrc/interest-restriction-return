/*
 * Copyright 2024 HM Revenue & Customs
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

package data

import play.api.libs.json.{JsObject, Json}
import v1.models.UltimateParentModel

object UltimateParentConstants extends BaseConstants {

  val otherUkTaxReference: String = "other reference"

  val ultimateParentModelMax: UltimateParentModel = UltimateParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = Some(ctutr),
    sautr = Some(sautr),
    countryOfIncorporation = Some(nonUkCountryCode),
    legalEntityIdentifier = None
  )

  val ultimateParentModelMin: UltimateParentModel = UltimateParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = None,
    sautr = None,
    countryOfIncorporation = None,
    legalEntityIdentifier = None
  )

  val ultimateParentModelUkCompany: UltimateParentModel = UltimateParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = Some(ctutr),
    sautr = None,
    countryOfIncorporation = None,
    legalEntityIdentifier = None
  )

  val ultimateParentModelNonUkCompany: UltimateParentModel = UltimateParentModel(
    companyName = companyName,
    isUk = false,
    ctutr = None,
    sautr = None,
    countryOfIncorporation = Some(nonUkCountryCode),
    legalEntityIdentifier = None
  )

  val ultimateParentModelUkPartnership: UltimateParentModel = UltimateParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = None,
    sautr = Some(sautr),
    countryOfIncorporation = None,
    legalEntityIdentifier = None
  )

  val ultimateParentJsonMax: JsObject = Json.obj(
    "companyName"            -> companyName,
    "isUk"                   -> true,
    "ctutr"                  -> ctutr,
    "sautr"                  -> sautr,
    "countryOfIncorporation" -> nonUkCountryCode
  )

  val ultimateParentJsonMin: JsObject = Json.obj(
    "companyName" -> companyName,
    "isUk"        -> true
  )

  val ultimateParentJsonUkCompany: JsObject = Json.obj(
    "companyName" -> companyName,
    "isUk"        -> true,
    "ctutr"       -> ctutr
  )

  val ultimateParentJsonNonUkCompany: JsObject = Json.obj(
    "companyName"            -> companyName,
    "isUk"                   -> true,
    "countryOfIncorporation" -> nonUkCountryCode
  )

  val ultimateParentJsonUkPartnership: JsObject = Json.obj(
    "companyName" -> companyName,
    "isUk"        -> true,
    "sautr"       -> ctutr
  )
}

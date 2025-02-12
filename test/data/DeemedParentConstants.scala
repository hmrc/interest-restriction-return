/*
 * Copyright 2025 HM Revenue & Customs
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
import v1.models.DeemedParentModel

object DeemedParentConstants extends BaseConstants {

  val deemedParentModelMax: DeemedParentModel = DeemedParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = Some(ctutr),
    sautr = Some(sautr),
    countryOfIncorporation = Some(nonUkCountryCode),
    legalEntityIdentifier = None
  )

  val deemedParentModelMin: DeemedParentModel = DeemedParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = None,
    sautr = None,
    countryOfIncorporation = None,
    legalEntityIdentifier = None
  )

  val deemedParentModelUkCompany: DeemedParentModel = DeemedParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = Some(ctutr),
    sautr = None,
    countryOfIncorporation = None,
    legalEntityIdentifier = None
  )

  val deemedParentModelNonUkCompany: DeemedParentModel = DeemedParentModel(
    companyName = companyName,
    isUk = false,
    ctutr = None,
    sautr = None,
    countryOfIncorporation = Some(nonUkCountryCode),
    legalEntityIdentifier = None
  )

  val deemedParentModelUkPartnership: DeemedParentModel = DeemedParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = None,
    sautr = Some(sautr),
    countryOfIncorporation = None,
    legalEntityIdentifier = None
  )

  val deemedParentJsonMax: JsObject = Json.obj(
    "companyName"            -> companyName,
    "isUk"                   -> true,
    "ctutr"                  -> ctutr,
    "sautr"                  -> sautr,
    "countryOfIncorporation" -> Some(nonUkCountryCode)
  )

  val deemedParentJsonMin: JsObject = Json.obj(
    "companyName" -> companyName,
    "isUk"        -> true
  )

  val deemedParentJsonUkCompany: JsObject = Json.obj(
    "companyName" -> companyName,
    "isUk"        -> true,
    "ctutr"       -> ctutr
  )

  val deemedParentJsonNonUkCompany: JsObject = Json.obj(
    "companyName"            -> companyName,
    "isUk"                   -> true,
    "countryOfIncorporation" -> nonUkCountryCode
  )

  val deemedParentJsonUkPartnership: JsObject = Json.obj(
    "companyName" -> companyName,
    "isUk"        -> true,
    "sautr"       -> sautr
  )
}

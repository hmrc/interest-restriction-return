/*
 * Copyright 2021 HM Revenue & Customs
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
import v1.models.DeemedParentModel

object DeemedParentConstants extends BaseConstants {

  val deemedParentModelMax = DeemedParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = Some(ctutr),
    sautr = Some(sautr),
    countryOfIncorporation = Some(nonUkCountryCode)
  )

  val deemedParentModelMin = DeemedParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = None,
    sautr = None,
    countryOfIncorporation = None
  )

  val deemedParentModelUkCompany = DeemedParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = Some(ctutr),
    sautr = None,
    countryOfIncorporation = None
  )

  val deemedParentModelNonUkCompany = DeemedParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = None,
    sautr = None,
    countryOfIncorporation = Some(nonUkCountryCode)
  )

  val deemedParentModelUkPartnership = DeemedParentModel(
    companyName = companyName,
    isUk = true,
    ctutr = None,
    sautr = Some(sautr),
    countryOfIncorporation = None
  )

  val deemedParentJsonMax = Json.obj(
    "companyName" -> companyName,
    "isUk" -> true,
    "ctutr" -> ctutr,
    "sautr" -> sautr,
    "countryOfIncorporation" -> Some(nonUkCountryCode)
  )

  val deemedParentJsonMin = Json.obj(
    "companyName" -> companyName,
     "isUk" -> true
  )

  val deemedParentJsonUkCompany = Json.obj(
    "companyName" -> companyName,
    "isUk" -> true,
    "ctutr" -> ctutr
  )

  val deemedParentJsonNonUkCompany= Json.obj(
    "companyName" -> companyName,
    "isUk" -> true,
    "countryOfIncorporation" -> nonUkCountryCode
  )

  val deemedParentJsonUkPartnership = Json.obj(
    "companyName" -> companyName,
    "isUk" -> true,
    "sautr" -> sautr
  )
}

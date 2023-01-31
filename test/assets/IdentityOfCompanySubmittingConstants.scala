/*
 * Copyright 2023 HM Revenue & Customs
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

import play.api.libs.json.{JsObject, Json}
import v1.models.{CountryCodeModel, IdentityOfCompanySubmittingModel}

object IdentityOfCompanySubmittingConstants extends BaseConstants {

  val countryCode = "US"

  val identityOfCompanySubmittingJsonMax: JsObject = Json.obj(
    "companyName"           -> companyName,
    "ctutr"                 -> ctutr,
    "legalEntityIdentifier" -> lei
  )

  val identityOfCompanySubmittingModelMax: IdentityOfCompanySubmittingModel = IdentityOfCompanySubmittingModel(
    companyName = companyName,
    ctutr = Some(ctutr),
    countryOfIncorporation = None,
    legalEntityIdentifier = Some(lei)
  )

  val identityOfCompanySubmittingModelNonUk: IdentityOfCompanySubmittingModel = IdentityOfCompanySubmittingModel(
    companyName = companyName,
    ctutr = None,
    countryOfIncorporation = Some(CountryCodeModel(countryCode)),
    legalEntityIdentifier = None
  )

  val identityOfCompanySubmittingJsonMin: JsObject = Json.obj(
    "companyName" -> companyName
  )

  val identityOfCompanySubmittingModelMin: IdentityOfCompanySubmittingModel = IdentityOfCompanySubmittingModel(
    companyName = companyName,
    ctutr = None,
    countryOfIncorporation = None,
    legalEntityIdentifier = None
  )
}

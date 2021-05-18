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

import v1.models.{CompanyNameModel, CountryCodeModel, LegalEntityIdentifierModel, UTRModel}

trait BaseConstants {

  val ctutr = UTRModel("1123456789")
  val sautr = UTRModel("1123456789")
  val lei = LegalEntityIdentifierModel("ABCDEFGHIJKLMNOPQR12")
  val invalidUtr = UTRModel("1999999999")
  val invalidShortUtr = UTRModel("1")
  val invalidLongUtr = UTRModel("11234567890")
  val companyName = CompanyNameModel("Company Name ltd")
  val companyNameMaxLength = 160
  val companyNameTooLong = CompanyNameModel("a" * (companyNameMaxLength + 1))
  val companyNameIsZero = CompanyNameModel("")
  val nonUkCountryCode = CountryCodeModel("US")
  val invalidCountryCode = CountryCodeModel("AA")
  val invalidEmptyCountryCode = CountryCodeModel("")
  val invalidLei = LegalEntityIdentifierModel("ABCDEFGHIJKLMNOPQRAA")

}

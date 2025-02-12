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

import v1.models.{CompanyNameModel, CountryCodeModel, LegalEntityIdentifierModel, UTRModel}

trait BaseConstants {

  val ctutr: UTRModel                           = UTRModel("1123456789")
  val sautr: UTRModel                           = UTRModel("1123456789")
  val lei: LegalEntityIdentifierModel           = LegalEntityIdentifierModel("ABCDEFGHIJKLMNOPQR12")
  val invalidUtr: UTRModel                      = UTRModel("1999999999")
  val invalidShortUtr: UTRModel                 = UTRModel("1")
  val invalidLongUtr: UTRModel                  = UTRModel("11234567890")
  val companyName: CompanyNameModel             = CompanyNameModel("Company Name ltd")
  val companyNameMaxLength: Int                 = 160
  val companyNameTooLong: CompanyNameModel      = CompanyNameModel("a" * (companyNameMaxLength + 1))
  val companyNameIsZero: CompanyNameModel       = CompanyNameModel("")
  val nonUkCountryCode: CountryCodeModel        = CountryCodeModel("US")
  val invalidCountryCode: CountryCodeModel      = CountryCodeModel("AA")
  val invalidEmptyCountryCode: CountryCodeModel = CountryCodeModel("")
  val invalidLei: LegalEntityIdentifierModel    = LegalEntityIdentifierModel("ABCDEFGHIJKLMNOPQRAA")
}

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

import models.{CRNModel, CompanyNameModel, CountryCodeModel, UTRModel}

trait BaseConstants {

  val ctutr = UTRModel("1123456789")
  val sautr = UTRModel("1234567890")
  val invalidUtr = UTRModel("1999999999")

  val crn = CRNModel("12345678")
  val companyName = CompanyNameModel("Company Name ltd")
  val companyNameMaxLength = 160
  val companyNameTooLong = CompanyNameModel("a" * (companyNameMaxLength + 1))
  val invalidCrn = CRNModel("AAAA1234")
  val nonUkCrn = "12345678901"
  val nonUkCountryCode = CountryCodeModel("US")
  val invalidCountryCode = CountryCodeModel("AA")

}

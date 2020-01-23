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

package v1.schemas

import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import utils.SchemaValidation
import v1.models.{CountryCodeModel, UTRModel}

//noinspection ScalaStyle
trait BaseSchemaSpec extends WordSpec with Matchers with GuiceOneAppPerSuite with SchemaValidation {

  val maxAgentNameLength = 160
  val maxDescriptionLength = 2000
  val maxCompanyNameLength = 160
  val utrLength = 10
  val crnLength = 8
  val electString = "elect"
  val revokeString = "revoke"
  val ctutrFake = Some(UTRModel("1111111111"))
  val sautrFake = Some(UTRModel("1111111111"))
  val nonUKCountryCode = Some(CountryCodeModel("US"))

}

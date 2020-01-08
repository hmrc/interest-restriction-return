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

package stubs

import assets.BaseITConstants
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.libs.json.JsString
import utils.WireMockMethods

object CompaniesHouseStub extends WireMockMethods with BaseITConstants {

  private val companiesHouseUrl: String = s"/companies-house-api-proxy/company/$crn"

  def checkCrn(response: Int): StubMapping =
    when(method = GET, uri = companiesHouseUrl).thenReturn(status = response, body = JsString("Response"))

}

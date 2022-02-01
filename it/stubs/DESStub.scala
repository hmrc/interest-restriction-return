/*
 * Copyright 2022 HM Revenue & Customs
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

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.Status._
import play.api.libs.json.JsValue
import utils.WireMockMethods

object DESStub extends WireMockMethods {

  private val appointReportingCompanyDesUrl = s"/organisations/interest-restrictions-return/appoint"
  private val revokeReportingCompanyDesUrl = s"/organisations/interest-restrictions-return/revoke"
  private val abbreviatedReturnDesUrl = s"/organisations/interest-restrictions-return/abbreviated"
  private val fullReturnDesUrl = s"/organisations/interest-restrictions-return/full"

  val headers = Map("Authorization" -> "Bearer dev", "Environment" -> "dev", "providerId" -> "providerId")

  def appointReportingCompanySuccess(response: JsValue): StubMapping =
    when(method = POST, uri = appointReportingCompanyDesUrl, headers = headers).thenReturn(status = CREATED, body = response)

  def appointReportingCompanyError: StubMapping =
    when(method = POST, uri = appointReportingCompanyDesUrl, headers = headers).thenReturn(status = INTERNAL_SERVER_ERROR)

  def revokeReportingCompanySuccess(response: JsValue): StubMapping =
    when(method = POST, uri = revokeReportingCompanyDesUrl, headers = headers).thenReturn(status = CREATED, body = response)

  def revokeReportingCompanyError: StubMapping =
    when(method = POST, uri = revokeReportingCompanyDesUrl, headers = headers).thenReturn(status = INTERNAL_SERVER_ERROR)

  def abbreviatedReturnSuccess(response: JsValue): StubMapping =
    when(method = POST, uri = abbreviatedReturnDesUrl, headers = headers).thenReturn(status = CREATED, body = response)

  def abbreviatedReturnError: StubMapping =
    when(method = POST, uri = abbreviatedReturnDesUrl, headers = headers).thenReturn(status = INTERNAL_SERVER_ERROR)

  def fullReturnSuccess(response: JsValue): StubMapping =
    when(method = POST, uri = fullReturnDesUrl, headers = headers).thenReturn(status = CREATED, body = response)

  def fullReturnError: StubMapping =
    when(method = POST, uri = fullReturnDesUrl, headers = headers).thenReturn(status = INTERNAL_SERVER_ERROR)

}

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

package utils

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.scalatestplus.play.BaseOneServerPerSuite

object WiremockHelper {
  val wiremockPort: Int    = 11111
  val wiremockHost: String = "localhost"
}

trait WiremockHelper {
  self: BaseOneServerPerSuite =>

  import WiremockHelper.*
  lazy val wmConfig: WireMockConfiguration = wireMockConfig().port(wiremockPort)
  val wireMockServer: WireMockServer       = new WireMockServer(wmConfig)

  def startWiremock(): Unit = {
    WireMock.configureFor(wiremockHost, wiremockPort)
    wireMockServer.start()
  }

  def stopWiremock(): Unit = wireMockServer.stop()

  def resetWiremock(): Unit = WireMock.reset()

  def verifyCalls(url: String, numberOfCalls: Int): Unit =
    WireMock.verify(numberOfCalls, postRequestedFor(urlPathEqualTo(url)))

  def verifyNoCall(url: String): Unit = WireMock.verify(0, postRequestedFor(urlPathEqualTo(url)))

}

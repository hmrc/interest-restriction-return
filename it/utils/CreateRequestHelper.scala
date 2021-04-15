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

package utils

import org.scalatestplus.play.ServerProvider
import play.api.Application
import play.api.http.HeaderNames.ACCEPT
import play.api.libs.json.JsValue
import play.api.libs.ws.{DefaultWSCookie, WSClient, WSResponse}

import scala.concurrent.Future
import scala.concurrent.duration.{Duration, FiniteDuration, SECONDS}

trait CreateRequestHelper extends ServerProvider {

  val defaultSeconds = 5
  val defaultDuration: FiniteDuration = Duration.apply(defaultSeconds, SECONDS)

  val app: Application

  lazy val ws: WSClient = app.injector.instanceOf(classOf[WSClient])

  val defaultCookie = DefaultWSCookie("CSRF-Token","123")

  def getRequest(path: String, follow: Boolean = true): Future[WSResponse] = {
    ws.url(s"http://localhost:$port$path")
      .withFollowRedirects(follow)
      .withHttpHeaders(ACCEPT -> "application/vnd.hmrc.1.0+json")
      .get()
  }

  def postRequest(path: String, formJson: JsValue, follow: Boolean = true): Future[WSResponse] = {
    ws.url(s"http://localhost:$port$path")
      .withCookies(defaultCookie)
      .withHttpHeaders(ACCEPT -> "application/vnd.hmrc.1.0+json")
      .withFollowRedirects(follow)
      .post(formJson)
  }

}

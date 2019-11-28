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

package utils

import config.AppConfig
import controllers.actions.mocks.{Authorised, Unauthorised}
import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.BodyParsers
import play.api.test.FakeRequest
import uk.gov.hmrc.auth.core.MissingBearerToken
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.ExecutionContext

trait BaseSpec extends UnitSpec with Matchers with GuiceOneAppPerSuite {

  lazy val fakeRequest = FakeRequest("GET", "/")

  lazy val injector = app.injector

  lazy val bodyParsers = injector.instanceOf[BodyParsers.Default]
  lazy val appConfig = injector.instanceOf[AppConfig]
  lazy implicit val ec = injector.instanceOf[ExecutionContext]
  lazy implicit val headerCarrier: HeaderCarrier = HeaderCarrier()

  lazy val authorisedAction = new Authorised[Option[String]](Some("id"), bodyParsers)
  lazy val unauthorisedAction = new Unauthorised(new MissingBearerToken, bodyParsers)

}

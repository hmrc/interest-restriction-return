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

import assets.BaseConstants
import assets.NrsConstants
import config.AppConfig
import org.scalatest.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.BodyParsers
import play.api.test.FakeRequest
import uk.gov.hmrc.auth.core.MissingBearerToken
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec
import v1.controllers.actions.mocks.{Authorised, Unauthorised}
import v1.models.Validation
import v1.models.Validation.ValidationResult
import v1.models.requests.IdentifierRequest
import scala.concurrent.ExecutionContext
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder

trait BaseSpec extends UnitSpec with Matchers with GuiceOneAppPerSuite with MaterializerSupport with BaseConstants {

  implicit override lazy val app: Application =
    new GuiceApplicationBuilder()
      .disable(classOf[com.kenshoo.play.metrics.PlayModule])
      .configure("metrics.jvm" -> false)
      .build()

  lazy val fakeRequest = FakeRequest("GET", "/")
  lazy implicit val identifierRequest = IdentifierRequest(fakeRequest, "id", NrsConstants.nrsRetrievalData)
  lazy val injector = app.injector
  lazy val bodyParsers = injector.instanceOf[BodyParsers.Default]
  lazy val jsonFormatters = injector.instanceOf[FeatureSwitchJsonFormatter]
  lazy val appConfig = injector.instanceOf[AppConfig]
  lazy implicit val ec = injector.instanceOf[ExecutionContext]
  lazy implicit val headerCarrier: HeaderCarrier = HeaderCarrier()

  val fakeAuthResponse = NrsConstants.fakeResponse
  object AuthorisedAction extends Authorised[NrsConstants.NrsRetrievalDataType](fakeAuthResponse, bodyParsers, appConfig)
  object UnauthorisedAction extends Unauthorised(new MissingBearerToken, bodyParsers, appConfig)

  def rightSide[A](validationResult: ValidationResult[A]): A = validationResult.toEither.right.get

  def leftSideError[A](validationResult: ValidationResult[A], position: Int = 0): Validation = {
    validationResult.toEither.left.get.toChain.toList(position)
  }

  def leftSideErrorLength[A](validationResult: ValidationResult[A]): Int = {
    validationResult.toEither.left.get.toChain.length.toInt
  }

  def errorMessages(messages: String*) = messages.mkString("|")
}

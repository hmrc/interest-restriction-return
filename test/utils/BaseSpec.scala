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
import config.AppConfig
import org.scalatest.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.BodyParsers
import play.api.test.FakeRequest
import uk.gov.hmrc.auth.core.MissingBearerToken
import uk.gov.hmrc.auth.core.retrieve.Credentials
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec
import v1.controllers.actions.{AuthActionBase, AuthActionProvider, AuthActionProviderImpl}
import v1.controllers.actions.mocks.{Authorised, Unauthorised}
import v1.models.Validation
import v1.models.Validation.ValidationResult
import v1.models.requests.IdentifierRequest

import scala.concurrent.ExecutionContext

trait BaseSpec extends UnitSpec with Matchers with GuiceOneAppPerSuite with MaterializerSupport with BaseConstants {

  lazy val fakeRequest = FakeRequest("GET", "/")
  lazy implicit val identifierRequest = IdentifierRequest(fakeRequest, "id")
  lazy val injector = app.injector
  lazy val bodyParsers = injector.instanceOf[BodyParsers.Default]
  lazy val appConfig = injector.instanceOf[AppConfig]
  lazy implicit val ec = injector.instanceOf[ExecutionContext]
  lazy implicit val headerCarrier: HeaderCarrier = HeaderCarrier()


  object AuthorisedAction extends Authorised[Option[Credentials]](Some(Credentials("id", "SCP")), bodyParsers)
  object UnauthorisedAction extends Unauthorised(new MissingBearerToken, bodyParsers)



  def rightSide[A](validationResult: ValidationResult[A]): A = validationResult.toEither.right.get

  def leftSideError[A](validationResult: ValidationResult[A], position: Int = 0): Validation = {
    validationResult.toEither.left.get.toChain.toList(position)
  }

  def leftSideErrorLength[A](validationResult: ValidationResult[A]): Int = {
    validationResult.toEither.left.get.toChain.length.toInt
  }

  def errorMessages(messages: String*) = messages.mkString("|")
}

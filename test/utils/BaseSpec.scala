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

import config.AppConfig
import data.UnitNrsConstants.NrsRetrievalDataType
import data.{BaseConstants, UnitNrsConstants}
import org.apache.pekko.actor.ActorSystem
import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.{Injector, bind}
import play.api.mvc.{AnyContentAsEmpty, BodyParsers}
import play.api.test.FakeRequest
import uk.gov.hmrc.auth.core.{AffinityGroup, MissingBearerToken}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.client.HttpClientV2
import v1.connectors.NrsConnector
import v1.connectors.mocks.MockHttpClient
import v1.controllers.actions.mocks.{Authorised, Unauthorised}
import v1.models.Validation
import v1.models.Validation.ValidationResult
import v1.models.requests.IdentifierRequest
import v1.services.{DateTimeService, NrsService}

import scala.concurrent.ExecutionContext

trait BaseSpec
    extends UnitSpec
    with Matchers
    with GuiceOneAppPerSuite
    with BaseConstants
    with EitherValues
    with MockHttpClient {

  implicit override lazy val app: Application =
    new GuiceApplicationBuilder()
      .configure(
        "metrics.jvm"      -> false,
        "metrics.enabled"  -> false,
        "auditing.enabled" -> false
      )
      .overrides(
        bind[HttpClientV2].toInstance(mockHttpClient)
      )
      .build()

  lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("GET", "/")

  lazy implicit val identifierRequest: IdentifierRequest[AnyContentAsEmpty.type] =
    IdentifierRequest(fakeRequest, "id", UnitNrsConstants.nrsRetrievalData(Some(AffinityGroup.Organisation)))

  lazy val injector: Injector                    = app.injector
  lazy val bodyParsers: BodyParsers.Default      = injector.instanceOf[BodyParsers.Default]
  lazy val appConfig: AppConfig                  = injector.instanceOf[AppConfig]
  lazy val dateTimeService: DateTimeService      = injector.instanceOf[DateTimeService]
  lazy val nrsConnector: NrsConnector            = injector.instanceOf[NrsConnector]
  lazy implicit val ec: ExecutionContext         = injector.instanceOf[ExecutionContext]
  lazy val nrsService: NrsService                =
    new NrsService(nrsConnector = nrsConnector, dateTimeService = dateTimeService)
  lazy implicit val headerCarrier: HeaderCarrier = HeaderCarrier()
  lazy implicit val system: ActorSystem          = ActorSystem("Sys")

  def fakeAuthResponse(nrsAffinityGroup: Option[AffinityGroup]): NrsRetrievalDataType =
    UnitNrsConstants.fakeResponse(nrsAffinityGroup)

  object AuthorisedAction
      extends Authorised[UnitNrsConstants.NrsRetrievalDataType](
        fakeAuthResponse(Some(AffinityGroup.Organisation)),
        bodyParsers,
        appConfig
      )

  object UnauthorisedAction extends Unauthorised(new MissingBearerToken, bodyParsers, appConfig)

  def rightSide[A](validationResult: ValidationResult[A]): A = validationResult.toOption.get

  def leftSideError[A](validationResult: ValidationResult[A], position: Int = 0): Validation =
    validationResult.toEither.left.value.toChain.toList(position)

  def leftSideErrorLength[A](validationResult: ValidationResult[A]): Int =
    validationResult.toEither.left.value.toChain.length.toInt

  def errorMessages(messages: String*): String = messages.mkString("|")
}

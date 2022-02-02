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

package v1.controllers.actions

import akka.japi.Option.Some
import assets.UnitNrsConstants
import play.api.mvc.{Action, AnyContent, Results}
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core._
import utils.BaseSpec
import v1.connectors.mocks.{FakeFailingAuthConnector, FakeSuccessAuthConnector}

class AuthActionSpec extends BaseSpec {

  class Harness(authAction: AuthAction) {
    def onPageLoad(): Action[AnyContent] = authAction { _ => Results.Ok }
  }

  "Auth Action" when {

    "the user is logged in with providerId returned" must {

      "successfully carry out a request for Organisations" in {
        val authConnector = new FakeSuccessAuthConnector[UnitNrsConstants.NrsRetrievalDataType](UnitNrsConstants.fakeResponse(Some(AffinityGroup.Organisation)))
        val authAction = new AuthAction(authConnector, bodyParsers, appConfig)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest.withHeaders("Authorization" -> "test"))

        status(result) shouldBe OK
      }

      "successfully carry out a request for Agents" in {
        val authConnector = new FakeSuccessAuthConnector[UnitNrsConstants.NrsRetrievalDataType](UnitNrsConstants.fakeResponse(Some(AffinityGroup.Agent)))
        val authAction = new AuthAction(authConnector, bodyParsers, appConfig)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest.withHeaders("Authorization" -> "test"))

        status(result) shouldBe OK
      }

      "return a 403 FORBIDDEN if they are an individual" in {
        val authConnector = new FakeSuccessAuthConnector[UnitNrsConstants.NrsRetrievalDataType](UnitNrsConstants.fakeResponse(Some(AffinityGroup.Individual)))
        val authAction = new AuthAction(authConnector, bodyParsers, appConfig)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest.withHeaders("Authorization" -> "test"))

        status(result) shouldBe FORBIDDEN
        bodyOf(result) should contain "You are unable to access this service as an individual. This service is only available to individual companies or groups of companies."
      }

      " return a 401 UNAUTHORIZED if they have not supplied an Affinity Group" in {
        val authConnector = new FakeSuccessAuthConnector[UnitNrsConstants.NrsRetrievalDataType](UnitNrsConstants.fakeResponse(None))
        val authAction = new AuthAction(authConnector, bodyParsers, appConfig)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest.withHeaders("Authorization" -> "test"))

        status(result) shouldBe UNAUTHORIZED
      }
    }

    "the user is logged in with NO providerId returned" must {

      "redirect to unauthorised" in {
        val authConnector = new FakeSuccessAuthConnector[UnitNrsConstants.NrsRetrievalDataType](
          UnitNrsConstants.fakeResponseWithoutProviderId(Some(AffinityGroup.Organisation)))
        val authAction = new AuthAction(authConnector, bodyParsers, appConfig)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest.withHeaders("Authorization" -> "test"))

        status(result) shouldBe UNAUTHORIZED
      }
    }

    "the user hasn't logged in" must {

      "redirect the user to log in " in {

        val authAction = new AuthAction(new FakeFailingAuthConnector(new MissingBearerToken), bodyParsers, appConfig)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)

        status(result) shouldBe UNAUTHORIZED
      }
    }

    "the user's session has expired" must {

      "redirect the user to log in " in {

        val authAction = new AuthAction(new FakeFailingAuthConnector(new BearerTokenExpired), bodyParsers, appConfig)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest.withHeaders("Authorization" -> "test"))

        status(result) shouldBe UNAUTHORIZED
      }
    }

    "the user doesn't have sufficient enrolments" must {

      "redirect the user to the unauthorised page" in {

        val authAction = new AuthAction(new FakeFailingAuthConnector(new InsufficientEnrolments), bodyParsers, appConfig)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest.withHeaders("Authorization" -> "test"))

        status(result) shouldBe UNAUTHORIZED
      }
    }

    "the user used an unaccepted auth provider" must {

      "redirect the user to the unauthorised page" in {

        val authAction = new AuthAction(new FakeFailingAuthConnector(new UnsupportedAuthProvider), bodyParsers, appConfig)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest.withHeaders("Authorization" -> "test"))

        status(result) shouldBe UNAUTHORIZED
      }
    }

    "the user has an unsupported affinity group" must {

      "redirect the user to the unauthorised page" in {

        val authAction = new AuthAction(new FakeFailingAuthConnector(new UnsupportedAffinityGroup), bodyParsers, appConfig)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest.withHeaders("Authorization" -> "test"))

        status(result) shouldBe UNAUTHORIZED
      }
    }

    "the user has an unsupported credential role" must {

      "redirect the user to the unauthorised page" in {

        val authAction = new AuthAction(new FakeFailingAuthConnector(new UnsupportedCredentialRole), bodyParsers, appConfig)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest.withHeaders("Authorization" -> "test"))

        status(result) shouldBe UNAUTHORIZED
      }
    }
  }
}
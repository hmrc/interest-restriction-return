/*
 * Copyright 2025 HM Revenue & Customs
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

package v1.connectors

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.http.Status.INTERNAL_SERVER_ERROR
import uk.gov.hmrc.http.HttpReads
import utils.BaseSpec
import v1.connectors.HttpHelper.NrsResponse
import v1.connectors.constants.NrsConstants.*
import v1.connectors.mocks.MockHttpClient
import v1.models.nrs.*

import scala.concurrent.Future

class NrsConnectorSpec extends MockHttpClient with BaseSpec {

  val nrsBaseUrl: String     = "http://localhost:1111"
  val apiRelativeUrl: String = "/submission"
  val fullURL: String        = s"$nrsBaseUrl$apiRelativeUrl"

  private trait ConnectorTestSetup {

    lazy val connector: NrsConnectorImpl =
      new NrsConnectorImpl(mockHttpClient, mockAppConfig)

    mockPostCall(fullURL)
  }

  "NrsConnector" when {

    ".send" should {

      "return Left NrsError" when {

        "nrs config is disabled" in new ConnectorTestSetup {

          when(mockAppConfig.nrsEnabled)
            .thenReturn(false)

          val result: Future[NrsResponse] = connector.send(nrsPayload)

          await(result) shouldBe Left(
            NrsError(
              "[NrsConnectorImpl][send] NRS URL and token need to be configured in the application.conf or NRS is disabled"
            )
          )
        }

        "one of the two needed configs are undefined" in new ConnectorTestSetup {

          when(mockAppConfig.nrsUrl)
            .thenReturn(None)

          when(mockAppConfig.nrsAuthorisationToken)
            .thenReturn(Some("fake token"))

          val result: Future[NrsResponse] = connector.send(nrsPayload)

          await(result) shouldBe Left(
            NrsError(
              "[NrsConnectorImpl][send] NRS URL and token need to be configured in the application.conf or NRS is disabled"
            )
          )
        }

        "both configs are undefined" in new ConnectorTestSetup {

          when(mockAppConfig.nrsUrl)
            .thenReturn(None)

          when(mockAppConfig.nrsAuthorisationToken)
            .thenReturn(None)

          val result: Future[NrsResponse] = connector.send(nrsPayload)

          await(result) shouldBe Left(
            NrsError(
              "[NrsConnectorImpl][send] NRS URL and token need to be configured in the application.conf or NRS is disabled"
            )
          )
        }
      }

      "return a Right NrSubmissionId" when {
        "submission is successful" in new ConnectorTestSetup {

          when(mockAppConfig.nrsUrl)
            .thenReturn(Some("http://localhost:1111"))

          when(mockAppConfig.nrsAuthorisationToken)
            .thenReturn(Some("fake token"))

          when(mockRequestBuilder.execute(using any[HttpReads[NrsResponse]], any()))
            .thenReturn(Future(Right(NrSubmissionId(submissionId))))

          val result: Future[NrsResponse] = connector.send(nrsPayload)
          await(result) shouldBe Right(NrSubmissionId(submissionId))
        }
      }

      "return a Left UnexpectedFailure" when {
        "submission fails" in new ConnectorTestSetup {
          when(mockAppConfig.nrsUrl)
            .thenReturn(Some("http://localhost:1111"))

          when(mockAppConfig.nrsAuthorisationToken)
            .thenReturn(Some("fake token"))

          when(mockRequestBuilder.execute(using any[HttpReads[NrsResponse]], any()))
            .thenReturn(Future(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))))

          val result: Future[NrsResponse] = connector.send(nrsPayload)

          await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
        }
      }
    }
  }
}

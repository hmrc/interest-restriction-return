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

package v1.connectors

import data.AppConfigConstants._
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import uk.gov.hmrc.http.{HttpReads, StringContextOps}
import utils.BaseSpec
import v1.connectors.HttpHelper.NrsResponse
import v1.connectors.constants.NrsConstants._
import v1.connectors.mocks.MockHttpClient
import v1.models.nrs._

import scala.concurrent.Future

class NrsConnectorSpec extends MockHttpClient with BaseSpec {

  val nrsBaseUrl: String = "http://localhost:1111"
  val apiRelativeUrl     = "/submission"
  val fullURL            = s"$nrsBaseUrl$apiRelativeUrl"

  private trait ConnectorTestSetup {

    lazy val connector: NrsConnectorImpl =
      new NrsConnectorImpl(mockHttpClient, appConfigWithNrs)

    mockPostCall(fullURL)
  }

  "NrsConnector" when {

    ".send" should {

      "return NrsConfigurationException" when {

        "nrs config is disabled" in {

          when(mockAppConfig.nrsEnabled)
            .thenReturn(false)

          lazy val connector: NrsConnectorImpl =
            new NrsConnectorImpl(mockHttpClient, mockAppConfig)

          val result: Future[NrsResponse] = connector.send(nrsPayload)
          await(result) shouldBe Left(
            NrsError(
              "[NrsConnectorImpl][send] Possibly errors include: NRS URL and token needs to be configured in the application.conf, " +
                "NrsConfig is disabled, or unexpected error from NRS"
            )
          )
        }
      }

      "return a Right NrSubmissionId" when {
        "submission is successful" in new ConnectorTestSetup {

          when(mockRequestBuilder.execute(any[HttpReads[NrSubmissionId]], any()))
            .thenReturn(NrSubmissionId(submissionId))

          val result: Future[NrsResponse] = connector.send(nrsPayload)
          await(result) shouldBe Right(NrSubmissionId(submissionId))
        }
      }

      "return a Left NrsError" when {

        "both configs are undefined" should {

          "submission is unsuccessful return a Left(NrsError)" in {

            when(mockAppConfig.nrsUrl)
              .thenReturn(None)

            when(mockAppConfig.nrsAuthorisationToken)
              .thenReturn(None)

            lazy val connector: NrsConnectorImpl =
              new NrsConnectorImpl(mockHttpClient, mockAppConfig)

            val result: Future[NrsResponse] = connector.send(nrsPayload)
            await(result) shouldBe Left(
              NrsError(
                "[NrsConnectorImpl][send] Possibly errors include: NRS URL and token needs to be configured in the application.conf, " +
                  "NrsConfig is disabled, or unexpected error from NRS"
              )
            )
          }
        }

        "one of the two needed configs are undefined" should {

          "submission is unsuccessful return a Left(NrsError)" in {

            when(mockAppConfig.nrsUrl)
              .thenReturn(None)

            when(mockAppConfig.nrsAuthorisationToken)
              .thenReturn(Some("fake token"))

            lazy val connector: NrsConnectorImpl =
              new NrsConnectorImpl(mockHttpClient, mockAppConfig)

            val result: Future[NrsResponse] = connector.send(nrsPayload)
            await(result) shouldBe Left(
              NrsError(
                "[NrsConnectorImpl][send] Possibly errors include: NRS URL and token needs to be configured in the application.conf, " +
                  "NrsConfig is disabled, or unexpected error from NRS"
              )
            )
          }
        }
      }
    }
  }
}

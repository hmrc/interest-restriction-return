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

import data.fullReturn.FullReturnConstants.*
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.http.Status.*
import uk.gov.hmrc.http.HttpReads
import utils.BaseSpec
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.mocks.MockHttpClient

import scala.concurrent.Future

class FullReturnConnectorSpec extends MockHttpClient with BaseSpec {

  val desBaseUrl: String     = "http://localhost:9262/cir"
  val apiRelativeUrl: String = "/return"
  val fullURL: String        = s"$desBaseUrl$apiRelativeUrl"

  private trait ConnectorTestSetup {
    val response: DesSuccessResponse = DesSuccessResponse(ackRef)

    lazy val connector: FullReturnConnector =
      new FullReturnConnector(mockHttpClient, mockAppConfig)

    mockHipURL()
    mockPostCall(fullURL)
  }

  "FullReturnConnector.submitHip" which {

    "uses fullReturnModelMax" when {

      "submission is successful" should {

        "return a Right(SuccessResponse)" in new ConnectorTestSetup {

          when(mockRequestBuilder.execute(using any[HttpReads[SubmissionResponse]], any()))
            .thenReturn(Future(Right(response)))

          val result: Future[SubmissionResponse] = connector.submitHip(fullReturnUltimateParentModel)
          await(result) shouldBe Right(response)
        }
      }

      "submission is unsuccessful" should {

        "return a Left(UnexpectedFailure)" in new ConnectorTestSetup {

          when(mockRequestBuilder.execute(using any[HttpReads[SubmissionResponse]], any()))
            .thenReturn(Future(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))))

          val result: Future[SubmissionResponse] = connector.submitHip(fullReturnUltimateParentModel)
          await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
        }
      }
    }

    "uses fullReturnModelMin" when {

      "submission is successful" should {

        "return a Right(SuccessResponse)" in new ConnectorTestSetup {

          when(mockRequestBuilder.execute(using any[HttpReads[SubmissionResponse]], any()))
            .thenReturn(Future(Right(response)))

          val result: Future[SubmissionResponse] = connector.submitHip(fullReturnModelMin)
          await(result) shouldBe Right(response)
        }
      }

      "submission is unsuccessful" should {

        "return a Left(UnexpectedFailure)" in new ConnectorTestSetup {

          when(mockRequestBuilder.execute(using any[HttpReads[SubmissionResponse]], any()))
            .thenReturn(Future(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))))

          val result: Future[SubmissionResponse] = connector.submitHip(fullReturnModelMin)
          await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
        }
      }
    }
  }
}

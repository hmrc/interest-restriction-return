/*
 * Copyright 2023 HM Revenue & Customs
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

import config.AppConfig
import v1.connectors.mocks.MockHttpClient
import utils.BaseSpec
import v1.connectors.HttpHelper.NrsResponse
import assets.AppConfigConstants._
import java.util.UUID

import v1.models.nrs._
import play.api.http.Status._
import org.scalatest.RecoverMethods._
import play.api.libs.json.Writes
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads}

import scala.concurrent.{ExecutionContext, Future}

class NrsConnectorSpec extends MockHttpClient with BaseSpec {

  private val submissionId: UUID     = UUID.randomUUID()
  private val nrsPayload: NrsPayload = mock[NrsPayload]
  private val nrsUrl: String         = "http://localhost:1111/submission"

  "NrsConnector" when {
    ".send" should {
      def setup(response: NrsResponse, appConfig: AppConfig): NrsConnector = {
        mockHttpPost[NrsPayload, Either[ErrorResponse, NrSubmissionId]](nrsUrl, nrsPayload)(response)
        new NrsConnectorImpl(mockHttpClient, appConfig)
      }

      "return NrsConfigurationException" when {
        "nrs config is not setup" in {
          val connector: NrsConnectorImpl = new NrsConnectorImpl(mockHttpClient, appConfig)
          val result: Future[NrsResponse] = connector.send(nrsPayload)

          recoverToSucceededIf[NrsConfigurationException](result)
        }
      }

      "return a Right NrSubmissionId" when {
        "submission is successful" in {
          val connector: NrsConnector     = setup(Right(NrSubmissionId(submissionId)), appConfigWithNrs)
          val result: Future[NrsResponse] = connector.send(nrsPayload)

          await(result) shouldBe Right(NrSubmissionId(submissionId))
        }
      }

      "return a Left UnexpectedFailure" when {
        "submission is unsuccessful" in {
          val connector: NrsConnector     = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")), appConfigWithNrs)
          val result: Future[NrsResponse] = connector.send(nrsPayload)

          await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
        }
      }

      "return an exception" in {
        def mockHttpPost(): Unit =
          (mockHttpClient
            .POST(_: String, _: NrsPayload, _: Seq[(String, String)])(
              _: Writes[NrsPayload],
              _: HttpReads[NrsResponse],
              _: HeaderCarrier,
              _: ExecutionContext
            ))
            .expects(nrsUrl, nrsPayload, *, *, *, *, *)
            .returns(Future.failed(new Exception()))

        mockHttpPost()

        val connector: NrsConnectorImpl = new NrsConnectorImpl(mockHttpClient, appConfigWithNrs)
        val result: Future[NrsResponse] = connector.send(nrsPayload)

        intercept[Exception] {
          await(result)
        }
      }
    }
  }
}

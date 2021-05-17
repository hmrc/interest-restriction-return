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

class NrsConnectorSpec extends MockHttpClient with BaseSpec {

  val submissionId = UUID.randomUUID()

  val nrsPayload = mock[NrsPayload]

  "NrsConnector.send" when {
    def setup(response: NrsResponse, appConfig: AppConfig): NrsConnector = {
      val nrsUrl = "http://localhost:1111"
      mockHttpPost[NrsPayload, Either[ErrorResponse, NrSubmissionId]](nrsUrl, nrsPayload)(response)
      new NrsConnector(mockHttpClient, appConfig)
    }

    "nrs config is not setup" should {
      "return a failure" in {
        val connector = new NrsConnector(mockHttpClient, appConfig)
        val result = connector.send(nrsPayload)
        
        recoverToSucceededIf[NrsConfigurationException](result)
      }
    }

    "submission is successful" should {
      "return a Right(SuccessResponse)" in {
        val connector = setup(Right(NrSubmissionId(submissionId)), appConfigWithNrs)
        val result = connector.send(nrsPayload)

        await(result) shouldBe Right(NrSubmissionId(submissionId))
      }
    }

    "update is unsuccessful" should {
      "return a Left(UnexpectedFailure)" in {
        val connector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")), appConfigWithNrs)
        val result = connector.send(nrsPayload)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }

}

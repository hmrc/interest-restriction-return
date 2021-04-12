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

import assets.abbreviatedReturn.AbbreviatedReturnConstants._
import assets.fullReturn.FullReturnConstants.ackRef
import audit.{InterestRestrictionReturnAuditEvent, InterestRestrictionReturnAuditService}
import play.api.http.Status._
import play.api.libs.json.Json
import utils.BaseSpec
import v1.audit.StubSuccessfulAuditService
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.mocks.MockHttpClient
import v1.models.abbreviatedReturn.AbbreviatedReturnModel

class AbbreviatedReturnConnectorSpec extends MockHttpClient with BaseSpec {
  val auditWrapper = new StubSuccessfulAuditService()
  val auditService = new InterestRestrictionReturnAuditService()

  "AbbreviatedReturnConnector.submitAbbreviatedReturn" when {

    def setup(response: SubmissionResponse): AbbreviatedReturnConnector = {
      val desUrl = "http://localhost:9262/organisations/interest-restrictions-return/abbreviated"
      mockHttpPost[AbbreviatedReturnModel, Either[ErrorResponse, DesSuccessResponse]](desUrl, abbreviatedReturnUltimateParentModel)(response)
      new AbbreviatedReturnConnector(mockHttpClient, auditService, auditWrapper, appConfig)
    }

    "submission is successful" should {

      auditWrapper.reset()

      "return a Right(SuccessResponse)" in {
        val connector = setup(Right(DesSuccessResponse(ackRef)))
        val result = connector.submitAbbreviatedReturn(abbreviatedReturnUltimateParentModel)

        await(result) shouldBe Right(DesSuccessResponse(ackRef))
      }

      "send audit event for successful response" in {
        val connector = setup(Right(DesSuccessResponse(ackRef)))

        await(connector.submitAbbreviatedReturn(abbreviatedReturnUltimateParentModel).map { _ =>
          auditWrapper.verifySent(InterestRestrictionReturnAuditEvent("AbbreviatedSubmission", 200, Some(Json.toJson(DesSuccessResponse(ackRef))))) shouldBe true
        })
      }

      "submission is unsuccessful" should {

        auditWrapper.reset()

        "return a Left(UnexpectedFailure)" in {

          val connector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
          val result = connector.submitAbbreviatedReturn(abbreviatedReturnUltimateParentModel)

          await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
        }

        "send audit event for error response" in {
          val connector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))

          await(connector.submitAbbreviatedReturn(abbreviatedReturnUltimateParentModel).map { _ =>
            auditWrapper.verifySent(InterestRestrictionReturnAuditEvent("AbbreviatedSubmission", 500, Some(Json.toJson("Error")))) shouldBe true
          })
        }
      }
    }
  }
}

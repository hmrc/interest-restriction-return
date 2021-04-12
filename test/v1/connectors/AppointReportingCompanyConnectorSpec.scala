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

import assets.abbreviatedReturn.AbbreviatedReturnConstants.abbreviatedReturnUltimateParentModel
import assets.appointReportingCompany.AppointReportingCompanyConstants._
import assets.fullReturn.FullReturnConstants.ackRef
import audit.{InterestRestrictionReturnAuditEvent, InterestRestrictionReturnAuditService}
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.mocks.MockHttpClient
import play.api.http.Status._
import play.api.libs.json.Json
import utils.BaseSpec
import v1.audit.StubSuccessfulAuditService
import v1.models.appointReportingCompany.AppointReportingCompanyModel

class AppointReportingCompanyConnectorSpec extends MockHttpClient with BaseSpec {
  val auditWrapper = new StubSuccessfulAuditService()
  val auditService = new InterestRestrictionReturnAuditService()

  "AppointReportingCompanyConnector.appoint" when {

    def setup(response: SubmissionResponse): AppointReportingCompanyConnector = {
      val desUrl = "http://localhost:9262/organisations/interest-restrictions-return/appoint"
      mockHttpPost[AppointReportingCompanyModel, Either[ErrorResponse, DesSuccessResponse]](desUrl, appointReportingCompanyModelMax)(response)
      new AppointReportingCompanyConnector(mockHttpClient,auditService,auditWrapper,appConfig)
    }

    "appointment is successful" should {

      auditWrapper.reset()

      "return a Right(SuccessResponse)" in {

        val connector = setup(Right(DesSuccessResponse(ackRef)))
        val result = connector.appoint(appointReportingCompanyModelMax)

        await(result) shouldBe Right(DesSuccessResponse(ackRef))
      }

      "send audit event for successful response" in {
        val connector = setup(Right(DesSuccessResponse(ackRef)))

        await(connector.appoint(appointReportingCompanyModelMax).map { _ =>
          auditWrapper.verifySent(InterestRestrictionReturnAuditEvent("AppointReportingCompany", 200, Some(Json.toJson(DesSuccessResponse(ackRef))))) shouldBe true
        })
      }
    }

    "update is unsuccessful" should {

      auditWrapper.reset()

      "return a Left(UnexpectedFailure)" in {

        val connector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result = connector.appoint(appointReportingCompanyModelMax)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }

      "send audit event for error response" in {
        val connector = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))

        await(connector.appoint(appointReportingCompanyModelMax).map { _ =>
          auditWrapper.verifySent(InterestRestrictionReturnAuditEvent("AppointReportingCompany", 500, Some(Json.toJson("Error")))) shouldBe true
        })
      }
    }
  }
}

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

package v1.services

import assets.fullReturn.FullReturnConstants
import assets.UnitNrsConstants
import com.google.common.io.BaseEncoding.base64
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.api.libs.json._
import play.api.test.{FakeHeaders, FakeRequest}
import v1.connectors.{NrsConnector, UnexpectedFailure}
import v1.models.nrs._
import v1.models.requests.IdentifierRequest
import v1.services.mocks.MockNrsConnector
import java.nio.charset.StandardCharsets.UTF_8
import java.util.UUID

import scala.concurrent.Future
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR}
import uk.gov.hmrc.auth.core.AffinityGroup
import v1.connectors.HttpHelper.NrsResponse

class NrsServiceSpec extends AsyncWordSpec with MockNrsConnector with Matchers {

  private val CLIENT_CLOSED_REQUEST: Int   = 499
  private val NETWORK_CONNECT_TIMEOUT: Int = 599

  private val formatter: DateTimeFormatter       = DateTimeFormat.forPattern("dd/MM/yyyy")
  private val dateTime: DateTime                 = formatter.parseDateTime("01/01/2021")
  private val fullRequest: FakeRequest[String]   = FakeRequest(
    "GET",
    "/",
    FakeHeaders(Seq("Authorization" -> "Bearer 123")),
    FullReturnConstants.fullReturnModelMax.toString
  )
  private val payloadAsString: String            = fullRequest.body.toString
  private val request: IdentifierRequest[String] = IdentifierRequest[String](
    request = fullRequest,
    identifier = "123",
    nrsRetrievalData = UnitNrsConstants.nrsRetrievalData(Some(AffinityGroup.Organisation))
  )
  private val expectedNrsMetadata                = new NrsMetadata(
    businessId = "irr",
    notableEvent = "interest-restriction-return",
    payloadContentType = "application/json",
    payloadSha256Checksum = UnitNrsConstants.sha256Hash(payloadAsString),
    userSubmissionTimestamp = dateTime.toString,
    userAuthToken = "Bearer 123",
    identityData = request.nrsRetrievalData,
    headerData = new JsObject(request.request.headers.toMap.map(x => x._1 -> JsString(x._2 mkString ","))),
    searchKeys = JsObject(
      Map[String, JsValue](
        "reportingCompanyCTUTR" -> JsString(FullReturnConstants.fullReturnModelMax.reportingCompany.ctutr.utr)
      )
    )
  )
  private val expectedNrsPayload: NrsPayload     =
    NrsPayload(base64().encode(payloadAsString.getBytes(UTF_8)), expectedNrsMetadata)

  object TestDateTimeService extends DateTimeService {
    def nowUtc(): DateTime = dateTime
  }

  "NrsService" when {
    ".send" should {
      "parse the metadata correctly with the auth information" in {
        val connector: NrsConnector = mockNrsConnector()
        mockNrsSubmission(expectedNrsPayload, connector)(Future.successful(Right(NrSubmissionId(UUID.randomUUID()))))
        val nrsService: NrsService  = new NrsService(connector, TestDateTimeService)

        val response: Future[NrsResponse] =
          nrsService.send(FullReturnConstants.fullReturnModelMax.reportingCompany.ctutr)(request)
        response.map(_ => succeed)
      }

      "return 400 error response without trying again" when {
        "a 400 error response is returned" in {
          val connector: NrsConnector = mockNrsConnector()
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(BAD_REQUEST, "Error occurred")))
          )
          val nrsService: NrsService  = new NrsService(connector, TestDateTimeService)

          val response: Future[NrsResponse] =
            nrsService.send(FullReturnConstants.fullReturnModelMax.reportingCompany.ctutr)(request)
          response.map(_ => succeed)
        }
      }

      "return 499 error response without trying again" when {
        "a 499 error response is returned" in {
          val connector: NrsConnector = mockNrsConnector()
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(CLIENT_CLOSED_REQUEST, "Error occurred")))
          )
          val nrsService: NrsService  = new NrsService(connector, TestDateTimeService)

          val response: Future[NrsResponse] =
            nrsService.send(FullReturnConstants.fullReturnModelMax.reportingCompany.ctutr)(request)
          response.map(_ => succeed)
        }
      }

      "try again" when {
        "a 500 error response is returned" in {
          val connector: NrsConnector = mockNrsConnector()
          val uuid: UUID              = UUID.randomUUID()
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error occurred")))
          )
          mockNrsSubmission(expectedNrsPayload, connector)(Future.successful(Right(NrSubmissionId(uuid))))
          val nrsService: NrsService  = new NrsService(connector, TestDateTimeService)

          val response: Future[NrsResponse] =
            nrsService.send(FullReturnConstants.fullReturnModelMax.reportingCompany.ctutr)(request)
          response.map(_ shouldEqual Right(NrSubmissionId(uuid)))
        }

        "a 599 error response is returned" in {
          val connector: NrsConnector = mockNrsConnector()
          val uuid: UUID              = UUID.randomUUID()
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(NETWORK_CONNECT_TIMEOUT, "Error occurred")))
          )
          mockNrsSubmission(expectedNrsPayload, connector)(Future.successful(Right(NrSubmissionId(uuid))))
          val nrsService: NrsService  = new NrsService(connector, TestDateTimeService)

          val response: Future[NrsResponse] =
            nrsService.send(FullReturnConstants.fullReturnModelMax.reportingCompany.ctutr)(request)
          response.map(_ shouldEqual Right(NrSubmissionId(uuid)))
        }

        "a 500 error response is returned twice" in {
          val connector: NrsConnector = mockNrsConnector()
          val uuid: UUID              = UUID.randomUUID()
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error occurred")))
          )
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error occurred")))
          )
          mockNrsSubmission(expectedNrsPayload, connector)(Future.successful(Right(NrSubmissionId(uuid))))
          val nrsService: NrsService  = new NrsService(connector, TestDateTimeService)

          val response: Future[NrsResponse] =
            nrsService.send(FullReturnConstants.fullReturnModelMax.reportingCompany.ctutr)(request)
          response.map(_ shouldEqual Right(NrSubmissionId(uuid)))
        }

        "a 599 error response is returned twice" in {
          val connector: NrsConnector = mockNrsConnector()
          val uuid: UUID              = UUID.randomUUID()
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(NETWORK_CONNECT_TIMEOUT, "Error occurred")))
          )
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(NETWORK_CONNECT_TIMEOUT, "Error occurred")))
          )
          mockNrsSubmission(expectedNrsPayload, connector)(Future.successful(Right(NrSubmissionId(uuid))))
          val nrsService: NrsService  = new NrsService(connector, TestDateTimeService)

          val response: Future[NrsResponse] =
            nrsService.send(FullReturnConstants.fullReturnModelMax.reportingCompany.ctutr)(request)
          response.map(_ shouldEqual Right(NrSubmissionId(uuid)))
        }
      }

      "not try again" when {
        "a 500 error response is returned thrice" in {
          val connector: NrsConnector = mockNrsConnector()
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error occurred")))
          )
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error occurred")))
          )
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error occurred")))
          )
          val nrsService: NrsService  = new NrsService(connector, TestDateTimeService)

          val response: Future[NrsResponse] =
            nrsService.send(FullReturnConstants.fullReturnModelMax.reportingCompany.ctutr)(request)
          response.map(_ shouldEqual Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error occurred")))
        }

        "a 599 error response is returned thrice" in {
          val connector: NrsConnector = mockNrsConnector()
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(NETWORK_CONNECT_TIMEOUT, "Error occurred")))
          )
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(NETWORK_CONNECT_TIMEOUT, "Error occurred")))
          )
          mockNrsSubmission(expectedNrsPayload, connector)(
            Future.successful(Left(UnexpectedFailure(NETWORK_CONNECT_TIMEOUT, "Error occurred")))
          )
          val nrsService: NrsService  = new NrsService(connector, TestDateTimeService)

          val response: Future[NrsResponse] =
            nrsService.send(FullReturnConstants.fullReturnModelMax.reportingCompany.ctutr)(request)
          response.map(_ shouldEqual Left(UnexpectedFailure(NETWORK_CONNECT_TIMEOUT, "Error occurred")))
        }

        "an exception is thrown thrice" in {
          val connector: NrsConnector = mockNrsConnector()
          mockNrsSubmission(expectedNrsPayload, connector)(Future.failed(new Exception()))
          mockNrsSubmission(expectedNrsPayload, connector)(Future.failed(new Exception()))
          mockNrsSubmission(expectedNrsPayload, connector)(Future.failed(new Exception()))
          val nrsService: NrsService  = new NrsService(connector, TestDateTimeService)

          val response: Future[NrsResponse] =
            nrsService.send(FullReturnConstants.fullReturnModelMax.reportingCompany.ctutr)(request)

          response.failed.map(_ shouldBe an[Exception])
        }
      }
    }
  }
}

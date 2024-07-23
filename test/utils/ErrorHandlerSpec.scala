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

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.when
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core.InsufficientEnrolments
import uk.gov.hmrc.http.{JsValidationException, NotFoundException}
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.http.connector.AuditResult.Success
import uk.gov.hmrc.play.audit.model.DataEvent
import uk.gov.hmrc.play.bootstrap.config.HttpAuditEvent
import v1.models.errors.ErrorResponseModel
import v1.models.errors.ErrorResponses._

import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.NoStackTrace

class ErrorHandlerSpec extends UnitSpec {

  private def versionHeader: (String, String) = ACCEPT -> "application/vnd.hmrc.1.0+json"

  private trait Test {

    val requestHeader: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withHeaders(versionHeader)

    private val auditConnector: AuditConnector = Mockito.mock(classOf[AuditConnector])
    private val httpAuditEvent: HttpAuditEvent = Mockito.mock(classOf[HttpAuditEvent])

    private val eventTags: Map[String, String] = Map("transactionName" -> "event.transactionName")

    private val dataEvent: DataEvent = DataEvent(
      auditSource = "auditSource",
      auditType = "event.auditType",
      eventId = "",
      tags = eventTags,
      detail = Map("test" -> "test"),
      generatedAt = Instant.now()
    )

    when(httpAuditEvent.dataEvent(any(), any(), any(), any(), any())(any()))
      .thenReturn(dataEvent)
    when(auditConnector.sendEvent(any())(any(), any()))
      .thenReturn(Future.successful(Success))

    private val configuration: Configuration = Configuration(
      "appName"                                         -> "myApp",
      "bootstrap.errorHandler.warnOnly.statusCodes"     -> List.empty,
      "bootstrap.errorHandler.suppress4xxErrorMessages" -> false,
      "bootstrap.errorHandler.suppress5xxErrorMessages" -> false
    )

    val handler: ErrorHandler = new ErrorHandler(configuration, auditConnector, httpAuditEvent)
  }

  "ErrorHandler" when {
    "onClientError" should {
      "return 404 with error body" when {
        "URI not found" in new Test {
          private val result = handler.onClientError(requestHeader, NOT_FOUND, "test")
          status(result) shouldBe NOT_FOUND

          contentAsJson(result) shouldBe Json.toJson(NotFoundError)
        }
      }

      "return 400 with error body" when {
        "bad request body and header is supplied" in new Test {
          private val result = handler.onClientError(requestHeader, BAD_REQUEST, "test")
          status(result) shouldBe BAD_REQUEST

          contentAsJson(result) shouldBe Json.toJson(BadRequestError)
        }
      }

      "return 401 with error body" when {
        "unauthorised and header is supplied" in new Test {
          private val result = handler.onClientError(requestHeader, UNAUTHORIZED, "test")
          status(result) shouldBe UNAUTHORIZED

          contentAsJson(result) shouldBe Json.toJson(UnauthorisedError)
        }
      }

      "return 415 with error body" when {
        "unsupported body and header is supplied" in new Test {
          private val result = handler.onClientError(requestHeader, UNSUPPORTED_MEDIA_TYPE, "test")
          status(result) shouldBe UNSUPPORTED_MEDIA_TYPE

          contentAsJson(result) shouldBe Json.toJson(InvalidBodyTypeError)
        }
      }

      "return 405 with error body" when {
        "invalid method type" in new Test {
          private val result = handler.onClientError(requestHeader, METHOD_NOT_ALLOWED, "test")
          status(result) shouldBe METHOD_NOT_ALLOWED

          contentAsJson(result) shouldBe Json.toJson(ErrorResponseModel("INVALID_REQUEST", "test"))
        }
      }
    }

    "onServerError" should {
      "return 404 with error body" when {
        "NotFoundException thrown" in new Test {
          private val result = handler.onServerError(requestHeader, new NotFoundException("test") with NoStackTrace)
          status(result) shouldBe NOT_FOUND

          contentAsJson(result) shouldBe Json.toJson(NotFoundError)
        }
      }

      "return 401 with error body" when {
        "AuthorisationException thrown" in new Test {
          private val result =
            handler.onServerError(requestHeader, new InsufficientEnrolments("test") with NoStackTrace)
          status(result) shouldBe UNAUTHORIZED

          contentAsJson(result) shouldBe Json.toJson(UnauthorisedError)
        }
      }

      "return 400 with error body" when {
        "JsValidationException thrown" in new Test {
          private val result =
            handler.onServerError(
              requestHeader,
              new JsValidationException("test", "test", classOf[String], "errs") with NoStackTrace
            )
          status(result) shouldBe BAD_REQUEST

          contentAsJson(result) shouldBe Json.toJson(BadRequestError)
        }
      }

      "return 500 with error body" when {
        "other exception thrown" in new Test {
          private val result = handler.onServerError(requestHeader, new Exception with NoStackTrace)
          status(result) shouldBe INTERNAL_SERVER_ERROR

          contentAsJson(result) shouldBe Json.toJson(DownstreamError)
        }
      }
    }
  }
}

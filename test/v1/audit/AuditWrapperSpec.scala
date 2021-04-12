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

package v1.audit

import akka.stream.Materializer
import audit.{AuditEvent, AuditWrapper}
import org.scalatest.Inside
import play.api.inject.{ApplicationLifecycle, bind}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.config.AuditingConfig
import uk.gov.hmrc.play.audit.http.connector.{AuditConnector, AuditResult}
import uk.gov.hmrc.play.audit.model.DataEvent
import utils.BaseSpec

import scala.concurrent.{ExecutionContext, Future}


class AuditWrapperSpec extends BaseSpec with Inside {
  import AuditWrapperSpec._

  "AuditServiceImpl" should {
    "construct and send the correct event" in {
      val event = TestAuditWrapperEvent("test-audit-payload")

      auditService().sendEvent(event)

      val sentEvent = FakeAuditConnector.lastSentEvent

      inside(sentEvent) {
        case DataEvent(auditSource, auditType, _, _, detail, _) =>
          auditSource shouldBe appName
          auditType shouldBe "TestAuditEvent"
          detail should contain("payload" -> "test-audit-payload")
      }
    }
  }
}

object AuditWrapperSpec {
  private val app = new GuiceApplicationBuilder()
    .overrides(
      bind[AuditConnector].toInstance(FakeAuditConnector)
    )
    .build()

  def fakeRequest(): FakeRequest[AnyContentAsEmpty.type] = FakeRequest("", "")

  def auditService(): AuditWrapper = app.injector.instanceOf[AuditWrapper]

  def appName: String = app.configuration.underlying.getString("appName")
}

object FakeAuditConnector extends AuditConnector {
  private var sentEvent: DataEvent = _

  override def auditingConfig: AuditingConfig = AuditingConfig(None, enabled = false, "test audit source")

  override def sendEvent(event: DataEvent)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[AuditResult] = {
    sentEvent = event
    super.sendEvent(event)
  }

  def lastSentEvent: DataEvent = sentEvent

  override def materializer: Materializer = ???

  override def lifecycle: ApplicationLifecycle = ???
}

case class TestAuditWrapperEvent(payload:String) extends AuditEvent {
  override def auditType: String = "TestAuditEvent"

  override def details: Map[String, String] =
    Map(
      "payload" -> payload
    )
}
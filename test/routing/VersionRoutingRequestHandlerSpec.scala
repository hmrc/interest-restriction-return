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

package routing

import com.typesafe.config.ConfigFactory
import org.mockito.Mockito.when
import org.mockito.{ArgumentMatchers, Mockito}
import org.scalatest.Inside
import play.api.Configuration
import play.api.http.{HttpConfiguration, HttpFilters}
import play.api.libs.json.Json
import play.api.mvc.*
import play.api.routing.Router
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.bootstrap.config.HttpAuditEvent
import utils.{BaseSpec, ErrorHandler}
import v1.models.errors.ErrorResponses.*

class VersionRoutingRequestHandlerSpec extends BaseSpec with Inside {

  import play.api.mvc.Handler
  import play.api.routing.sird.*

  object DefaultHandler extends Handler

  object V1Handler extends Handler

  object V2Handler extends Handler

  object V3Handler extends Handler

  private val defaultRouter = Router.from { case GET(p"") =>
    DefaultHandler
  }

  private val v1Router = Router.from { case GET(p"/v1") =>
    V1Handler
  }

  private val v2Router = Router.from { case GET(p"/v2") =>
    V2Handler
  }

  private val v3Router = Router.from { case GET(p"/v3") =>
    V3Handler
  }

  private def routingMap(router: Router) = new VersionRoutingMap {
    override val defaultRouter: Router    = router
    override val map: Map[String, Router] = Map("1.0" -> v1Router, "2.0" -> v2Router, "3.0" -> v3Router)
  }

  when(mockAppConfig.apiGatewayContext).thenReturn("gateway")
  when(mockAppConfig.featureSwitch).thenReturn(
    Some(
      Configuration(
        ConfigFactory.parseString(
          """
        |version-1.enabled = true
        |version-2.enabled = true
      """.stripMargin
        )
      )
    )
  )
  when(mockAppConfig.apiStatus(ArgumentMatchers.eq("1.0"))).thenReturn("ALPHA")
  when(mockAppConfig.endpointsEnabled).thenReturn(true)

  class Test(implicit acceptHeader: Option[String]) {
    val httpConfiguration: HttpConfiguration = HttpConfiguration("context")
    val auditConnector: AuditConnector       = Mockito.mock(classOf[AuditConnector])
    val httpAuditEvent: HttpAuditEvent       = Mockito.mock(classOf[HttpAuditEvent])
    val configuration: Configuration         = Configuration(
      "appName"                                         -> "myApp",
      "bootstrap.errorHandler.warnOnly.statusCodes"     -> Seq.empty[Int],
      "bootstrap.errorHandler.suppress4xxErrorMessages" -> false,
      "bootstrap.errorHandler.suppress5xxErrorMessages" -> false
    )

    private val errorHandler: ErrorHandler = new ErrorHandler(configuration, auditConnector, httpAuditEvent)
    private val mockFilters                = Mockito.mock(classOf[HttpFilters])

    val actionBuilder: DefaultActionBuilder = DefaultActionBuilder(stubBodyParser(AnyContentAsEmpty))

    val requestHandler: VersionRoutingRequestHandler =
      new VersionRoutingRequestHandler(
        routingMap(defaultRouter),
        errorHandler,
        httpConfiguration,
        mockAppConfig,
        mockFilters,
        actionBuilder
      )

    def buildRequest(path: String): RequestHeader =
      acceptHeader
        .foldLeft(FakeRequest("GET", path)) { (req, accept) =>
          req.withHeaders((ACCEPT, accept))
        }
  }

  "VersionRoutingRequestHandler" when {
    "Routing requests with no version" should {
      val acceptHeader: None.type = None

      handleWithDefaultRoutes(acceptHeader)
    }

    "Routing requests with valid version" should {
      val acceptHeader: Option[String] = Some("application/vnd.hmrc.1.0+json")

      handleWithDefaultRoutes(acceptHeader)
    }

    "Routing requests to non default router with no version" should {
      implicit val acceptHeader: None.type = None

      "return 406" in new Test {

        val request: RequestHeader = buildRequest("path")
        inside(requestHandler.routeRequest(request)) { case Some(action: EssentialAction) =>
          val result = action.apply(request).run()

          status(result)        shouldBe NOT_ACCEPTABLE
          contentAsJson(result) shouldBe Json.toJson(InvalidAcceptHeaderError)
        }
      }
    }

    "Routing requests with v1" should {
      implicit val acceptHeader: Option[String] = Some("application/vnd.hmrc.1.0+json")

      handleWithVersionRoutes("/v1", V1Handler)
    }

    "Routing requests with v2" should {
      implicit val acceptHeader: Option[String] = Some("application/vnd.hmrc.2.0+json")
      handleWithVersionRoutes("/v2", V2Handler)
    }

    "Routing requests with unsupported version" should {
      implicit val acceptHeader: Option[String] = Some("application/vnd.hmrc.5.0+json")

      "return 404" in new Test {
        private val request = buildRequest("path")

        inside(requestHandler.routeRequest(request)) { case Some(action: EssentialAction) =>
          val result = action.apply(request).run()

          status(result)        shouldBe NOT_FOUND
          contentAsJson(result) shouldBe Json.toJson(UnsupportedVersionError)
        }
      }
    }

    "Routing requests for supported version but not enabled" when {
      implicit val acceptHeader: Option[String] = Some("application/vnd.hmrc.3.0+json")

      "the version has a route for the resource" must {
        "return 404 Not Found" in new Test {

          private val request = buildRequest("path")
          inside(requestHandler.routeRequest(request)) { case Some(action: EssentialAction) =>
            val result = action.apply(request).run()

            status(result)        shouldBe NOT_FOUND
            contentAsJson(result) shouldBe Json.toJson(UnsupportedVersionError)

          }
        }
      }
    }
  }

  private def handleWithDefaultRoutes(implicit acceptHeader: Option[String]): Unit =
    "if the request ends with a trailing slash" when {
      "handler found" should {
        "use it" in new Test {
          requestHandler.routeRequest(buildRequest("/")) shouldBe Some(DefaultHandler)
        }
      }

      "handler not found" should {
        "try without the trailing slash" in new Test {
          requestHandler.routeRequest(buildRequest("/")) shouldBe Some(DefaultHandler)
        }
      }
    }

  private def handleWithVersionRoutes(path: String, handler: Handler)(implicit acceptHeader: Option[String]): Unit =
    "if the request ends with a trailing slash" when {
      "handler found" should {
        "use it" in new Test {
          requestHandler.routeRequest(buildRequest(s"$path/")) shouldBe Some(handler)
        }
      }

      "handler not found" should {
        "try without the trailing slash" in new Test {
          requestHandler.routeRequest(buildRequest(s"$path/")) shouldBe Some(handler)
        }
      }
    }
}

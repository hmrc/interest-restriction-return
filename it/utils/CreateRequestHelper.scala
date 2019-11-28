package utils

import org.scalatestplus.play.ServerProvider
import play.api.Application
import play.api.libs.json.JsValue
import play.api.libs.ws.{DefaultWSCookie, WSClient, WSRequest, WSResponse}

import scala.concurrent.Future
import scala.concurrent.duration.{Duration, FiniteDuration, SECONDS}

trait CreateRequestHelper extends ServerProvider {

  val defaultSeconds = 5
  val defaultDuration: FiniteDuration = Duration.apply(defaultSeconds, SECONDS)

  val app: Application

  lazy val ws: WSClient = app.injector.instanceOf(classOf[WSClient])

  val defaultCookie = DefaultWSCookie("CSRF-Token","123")

  def getRequest(path: String, follow: Boolean = true): WSRequest = {
    ws.url(s"http://localhost:$port$path").withFollowRedirects(follow)
  }

  def postRequest(path: String, formJson: JsValue, follow: Boolean = true): Future[WSResponse] = {
    ws.url(s"http://localhost:$port/interest-restriction-return$path")
      .withCookies(defaultCookie)
      .withFollowRedirects(follow)
      .post(formJson)
  }

}

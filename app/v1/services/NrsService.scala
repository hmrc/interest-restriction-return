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

package v1.services

import java.lang.String.format
import java.math.BigInteger
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest.getInstance
import v1.models.requests.IdentifierRequest
import javax.inject.{Inject, Singleton}
import com.google.common.io.BaseEncoding.base64
import play.api.libs.json.{JsObject, JsString, JsValue}
import play.api.mvc.AnyContentAsXml
import v1.connectors.NrsConnector
import uk.gov.hmrc.http._
import play.api.Logging
import v1.models.nrs._
import v1.connectors.HttpHelper.NrsResponse
import org.joda.time.{DateTime, DateTimeZone}
import java.time.Clock
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NrsService @Inject()(nrsConnector: NrsConnector)(implicit ec: ExecutionContext) extends HttpErrorFunctions with Logging {

  private val conversationIdKey = "conversationId"
  private val applicationXml = "application/xml"
  private val businessIdValue = "cds"
  private val notableEventValue = "cds-declaration"
  private val AuthorizationHeader = "Authorization"

  def send[A](implicit authRequest: IdentifierRequest[A], hc: HeaderCarrier): Future[NrsResponse] = {

    val payloadAsString = authRequest.request.body.asInstanceOf[AnyContentAsXml].xml.toString

    val conversationId = "vpr.conversationId.toString"

    val nrsMetadata = new NrsMetadata(businessId = businessIdValue,
      notableEvent = notableEventValue,
      payloadContentType = applicationXml,
      payloadSha256Checksum = sha256Hash(payloadAsString), // This should come from the end user NOT us
      userSubmissionTimestamp = nowUtc().toString,
      userAuthToken = authRequest.request.headers.get(AuthorizationHeader).getOrElse(""),
      identityData = authRequest.nrsRetrievalData,
      headerData = new JsObject(authRequest.request.headers.toMap.map(x => x._1 -> JsString(x._2 mkString ","))),
      searchKeys = JsObject(Map[String, JsValue](conversationIdKey -> JsString(conversationId)))
    )

    val nrsPayload: NrsPayload = NrsPayload(base64().encode(payloadAsString.getBytes(UTF_8)), nrsMetadata)

    nrsConnector.send(nrsPayload).recoverWith {
      case e: HttpException =>
        logger.info(s"Error occurred while submitting NRS payload got HttpException status: ${e.responseCode} error message: ${e.message}")
        Future.failed(e)
      case e: UpstreamErrorResponse =>
        logger.info(s"Error occurred while submitting NRS payload got UpstreamErrorResponse status: ${e.statusCode} error message: ${e.message}")
        Future.failed(e)
    }
  }

  private def nowUtc(): DateTime = new DateTime(Clock.systemUTC().instant().toEpochMilli, DateTimeZone.UTC)

  private def sha256Hash(text: String) : String =  {
    format("%064x", new BigInteger(1, getInstance("SHA-256").digest(text.getBytes("UTF-8"))))
  }

}

class NrsAuthenticationException extends Exception("NRS Authentication Data has not been retrieved", None.orNull)

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

package v1.services

import java.lang.String.format
import java.math.BigInteger
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest.getInstance
import v1.models.requests.IdentifierRequest

import javax.inject.{Inject, Singleton}
import com.google.common.io.BaseEncoding.base64
import play.api.libs.json.{JsObject, JsString, JsValue}
import v1.connectors.NrsConnector
import uk.gov.hmrc.http._
import play.api.Logging
import v1.models.nrs._
import v1.connectors.HttpHelper.NrsResponse
import v1.models.UTRModel

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.{Duration, MILLISECONDS}

@Singleton
class NrsService @Inject() (nrsConnector: NrsConnector, dateTimeService: DateTimeService)(implicit ec: ExecutionContext)
    extends HttpErrorFunctions
    with Logging {

  private val searchKey           = "reportingCompanyCTUTR"
  private val applicationJson     = "application/json"
  private val businessIdValue     = "irr"
  private val notableEventValue   = "interest-restriction-return"
  private val AuthorizationHeader = "Authorization"

  private val MaxRetries    = 2
  private val delayInMillis = 500

  def send[A](ctutr: UTRModel)(implicit identifierRequest: IdentifierRequest[A]): Future[NrsResponse] = {

    val payloadAsString = identifierRequest.request.body.toString

    val searchValue = ctutr.utr

    val nrsMetadata = new NrsMetadata(
      businessId = businessIdValue,
      notableEvent = notableEventValue,
      payloadContentType = applicationJson,
      payloadSha256Checksum = sha256Hash(payloadAsString), // This should come from the end user NOT us
      userSubmissionTimestamp = dateTimeService.nowUtc(),
      userAuthToken = identifierRequest.request.headers.get(AuthorizationHeader).getOrElse(""),
      identityData = identifierRequest.nrsRetrievalData,
      headerData = new JsObject(identifierRequest.request.headers.toMap.map(x => x._1 -> JsString(x._2 mkString ","))),
      searchKeys = JsObject(Map[String, JsValue](searchKey -> JsString(searchValue)))
    )

    val nrsPayload: NrsPayload = NrsPayload(base64().encode(payloadAsString.getBytes(UTF_8)), nrsMetadata)

    val delay = Duration(delayInMillis, MILLISECONDS)
    attemptSubmission(nrsPayload, delay, MaxRetries)
  }

  private def attemptSubmission(nrsPayload: NrsPayload, delay: Duration, retries: Int): Future[NrsResponse] = {
    logger.info(s"Attempting NRS submission ${MaxRetries - retries + 1} retries left: $retries")
    val result = nrsConnector.send(nrsPayload)
    result.flatMap {
      case Left(e) if e.status >= 500 && e.status < 600 && retries > 0 =>
        Thread.sleep(delay.toMillis)
        attemptSubmission(nrsPayload, delay, retries - 1)
      case response                                                    => Future.successful(response)
    } recoverWith {
      case e: Exception if retries > 0 =>
        logger.error(s"Error occurred during NRS submission: ${e.getMessage}", e)
        Thread.sleep(delay.toMillis)
        attemptSubmission(nrsPayload, delay, retries - 1)
    }
  }

  private def sha256Hash(text: String): String =
    format("%064x", new BigInteger(1, getInstance("SHA-256").digest(text.getBytes("UTF-8"))))

}

class NrsAuthenticationException extends Exception("NRS Authentication Data has not been retrieved", None.orNull)

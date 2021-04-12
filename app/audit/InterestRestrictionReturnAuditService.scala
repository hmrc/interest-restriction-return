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

package audit

import play.api.{Logger, Logging}
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.RequestHeader
import uk.gov.hmrc.http.HttpResponse
import v1.connectors.HttpHelper.SubmissionResponse

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

class InterestRestrictionReturnAuditService extends Logging{

  def sendInterestRestrictionReturnEvent(actionPerformed: String)(sendEvent: InterestRestrictionReturnAuditEvent => Unit)
                                        (implicit rh: RequestHeader, ec: ExecutionContext): PartialFunction[Try[SubmissionResponse], Unit] = {


    case Success(Right(successFulResponse)) =>
      sendEvent(
        InterestRestrictionReturnAuditEvent(
          action = actionPerformed,
          status = Status.OK,
          payload = Some(Json.toJson(successFulResponse))
        )
      )
    case Success(Left(e)) =>
      sendEvent(
        InterestRestrictionReturnAuditEvent(
          action = actionPerformed,
          status = e.status,
          payload = Some(Json.parse(e.body))
        )
      )
    case Failure(t) =>
      logger.error("Error in sending audit event for get PSA details", t)
  }
}

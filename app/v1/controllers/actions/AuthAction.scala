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

package v1.controllers.actions

import play.api.mvc.Results._
import play.api.mvc._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter
import v1.models.requests.IdentifierRequest

import scala.concurrent.{ExecutionContext, Future}

trait AuthActionBase extends ActionBuilder[IdentifierRequest, AnyContent] with ActionFunction[Request, IdentifierRequest] with AuthorisedFunctions

case class NoAuthAction(override val authConnector: AuthConnector,
                             parser: BodyParsers.Default
                            )(implicit val executionContext: ExecutionContext)
  extends AuthActionBase {

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] = {
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSessionAndRequest(request.headers, request = Some(request))
     block(IdentifierRequest(request, "interest-restriction-return-frontend"))
  }
}

case class AuthAction(override val authConnector: AuthConnector,
                           parser: BodyParsers.Default
                          )(implicit val executionContext: ExecutionContext)
  extends AuthActionBase {

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] = {
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSessionAndRequest(request.headers, request = Some(request))

    authorised().retrieve(Retrievals.credentials) {
      _.map { credential =>
        block(IdentifierRequest(request, credential.providerId))
      }.getOrElse(throw UnsupportedAuthProvider("Unable to retrieve providerId"))
    } recover {
      case _ => Unauthorized("No Active Session")
    }
  }
}
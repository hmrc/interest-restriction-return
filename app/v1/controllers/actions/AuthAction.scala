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

import com.google.inject.Inject
import play.api.mvc.Results._
import play.api.mvc._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.~
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter
import v1.models.requests.IdentifierRequest
import play.api.Logging
import v1.models.nrs.NrsRetrievalData

import scala.concurrent.{ExecutionContext, Future}

class AuthAction @Inject()(override val authConnector: AuthConnector,
                           val parser: BodyParsers.Default
                          )(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[IdentifierRequest, AnyContent] with ActionFunction[Request, IdentifierRequest] with AuthorisedFunctions with Logging {

  private val nrsRetrievals =
    Retrievals.internalId and Retrievals.externalId and Retrievals.agentCode and
    Retrievals.credentials and Retrievals.confidenceLevel and Retrievals.nino and
    Retrievals.saUtr and Retrievals.name and Retrievals.dateOfBirth and
    Retrievals.email and Retrievals.agentInformation and Retrievals.groupIdentifier and
    Retrievals.credentialRole and Retrievals.mdtpInformation and Retrievals.itmpName and
    Retrievals.itmpDateOfBirth and Retrievals.itmpAddress and Retrievals.affinityGroup and
    Retrievals.credentialStrength and Retrievals.loginTimes

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] = {
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSessionAndRequest(request.headers, request = Some(request))

    authorised().retrieve(nrsRetrievals) {
       case internalId ~ externalId ~ agentCode ~ credentials ~ confidenceLevel ~ nino ~ saUtr ~ name ~ 
            dateOfBirth ~ email ~ agentInformation ~ groupIdentifier ~ credentialRole ~ mdtpInformation ~ 
            itmpName ~ itmpDateOfBirth ~ itmpAddress ~ affinityGroup ~ credentialStrength ~ loginTimes =>

        val retrievalData = NrsRetrievalData(internalId, externalId, agentCode, credentials, confidenceLevel, nino, saUtr,
          name, dateOfBirth, email, agentInformation, groupIdentifier, credentialRole, mdtpInformation, itmpName,
          itmpDateOfBirth, itmpAddress, affinityGroup, credentialStrength, loginTimes)

        credentials.map { credential =>
          block(IdentifierRequest(request, credential.providerId, retrievalData))
        }.getOrElse(throw UnsupportedAuthProvider("Unable to retrieve providerId"))
    } recover {
      case _ => Unauthorized("No Active Session")
    }
  }

}
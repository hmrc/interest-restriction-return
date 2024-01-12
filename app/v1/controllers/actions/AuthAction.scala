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

package v1.controllers.actions

import com.google.inject.Inject
import config.AppConfig
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._
import uk.gov.hmrc.auth.core.AffinityGroup.{Agent, Individual, Organisation}
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.retrieve.{Credentials, ~}
import uk.gov.hmrc.http.{Authorization, HeaderCarrier, HeaderNames}
import uk.gov.hmrc.play.http.HeaderCarrierConverter
import v1.models.authAction.ForbiddenIndividualsError
import v1.models.nrs.NrsRetrievalData
import v1.models.requests.IdentifierRequest

import scala.concurrent.{ExecutionContext, Future}

class AuthAction @Inject() (
  override val authConnector: AuthConnector,
  val parser: BodyParsers.Default,
  appConfig: AppConfig
)(implicit val executionContext: ExecutionContext)
    extends ActionBuilder[IdentifierRequest, AnyContent]
    with ActionFunction[Request, IdentifierRequest]
    with AuthorisedFunctions
    with Logging {

  private val nrsRetrievals =
    Retrievals.internalId and Retrievals.externalId and Retrievals.agentCode and Retrievals.credentials and
      Retrievals.confidenceLevel and Retrievals.nino and Retrievals.saUtr and Retrievals.name and
      Retrievals.dateOfBirth and Retrievals.email and Retrievals.agentInformation and Retrievals.groupIdentifier and
      Retrievals.credentialRole and Retrievals.mdtpInformation and Retrievals.itmpName and Retrievals.itmpDateOfBirth and
      Retrievals.itmpAddress and Retrievals.affinityGroup and Retrievals.credentialStrength and Retrievals.loginTimes

  private val defaultRetrievals =
    Retrievals.credentials and Retrievals.confidenceLevel and Retrievals.agentInformation and
      Retrievals.loginTimes and Retrievals.affinityGroup

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] =
    request.headers.get(HeaderNames.authorisation) match {
      case Some(authHeader) =>
        implicit val hc: HeaderCarrier = HeaderCarrierConverter
          .fromRequestAndSession(request = request, session = request.session)
          .copy(authorization = Some(Authorization(authHeader)))
        retrievalData(request, block)
      case _                =>
        logger.warn("An error occurred during auth action: No Authorization header provided")
        Future.successful(Unauthorized)
    }

  private def retrievalData[A](request: Request[A], block: IdentifierRequest[A] => Future[Result])(implicit
    hc: HeaderCarrier
  ): Future[Result] = (if (appConfig.nrsEnabled) {
                         nrsRetrievalData(request, block)
                       } else {
                         defaultRetrievalData(request, block)
                       }).recover { case e =>
    logger.error(s"An error occurred during auth action: ${e.getMessage}", e)
    Unauthorized
  }

  private def credentialMap[A](
    request: Request[A],
    block: IdentifierRequest[A] => Future[Result],
    credentials: Option[Credentials],
    retrievalData: NrsRetrievalData,
    affinityGroup: Option[AffinityGroup]
  ): Future[Result] =
    affinityGroup match {
      case Some(Organisation) | Some(Agent) =>
        credentials
          .map(credential => block(IdentifierRequest(request, credential.providerId, retrievalData)))
          .getOrElse {
            logger.warn("No Active Session")
            Future.successful(Unauthorized)
          }
      case Some(Individual)                 => forbiddenIndividuals
      case _                                =>
        logger.warn("User did not provide affinity group")
        Future.successful(Unauthorized)
    }

  private def forbiddenIndividuals: Future[Result] = Future.successful {
    logger.warn("User with an affinity group type of Individual tried to access IRR")
    Forbidden(Json.toJson(ForbiddenIndividualsError()))
  }

  private def nrsRetrievalData[A](request: Request[A], block: IdentifierRequest[A] => Future[Result])(implicit
    hc: HeaderCarrier
  ): Future[Result] =
    authorised().retrieve(nrsRetrievals) {
      case internalId ~ externalId ~ agentCode ~ credentials ~ confidenceLevel ~ nino ~ saUtr ~ name ~
          dateOfBirth ~ email ~ agentInformation ~ groupIdentifier ~ credentialRole ~ mdtpInformation ~
          itmpName ~ itmpDateOfBirth ~ itmpAddress ~ affinityGroup ~ credentialStrength ~ loginTimes =>
        val data = NrsRetrievalData(
          internalId,
          externalId,
          agentCode,
          credentials,
          confidenceLevel,
          nino,
          saUtr,
          name,
          dateOfBirth,
          email,
          agentInformation,
          groupIdentifier,
          credentialRole,
          mdtpInformation,
          itmpName,
          itmpDateOfBirth,
          itmpAddress,
          affinityGroup,
          credentialStrength,
          loginTimes
        )
        credentialMap(request, block, credentials, data, affinityGroup)
    }

  private def defaultRetrievalData[A](request: Request[A], block: IdentifierRequest[A] => Future[Result])(implicit
    hc: HeaderCarrier
  ): Future[Result] =
    authorised().retrieve(defaultRetrievals) {
      case credentials ~ confidenceLevel ~ agentInformation ~ loginTimes ~ affinityGroup =>
        val data = NrsRetrievalData(
          None,
          None,
          None,
          credentials,
          confidenceLevel,
          None,
          None,
          None,
          None,
          None,
          agentInformation,
          None,
          None,
          None,
          None,
          None,
          None,
          affinityGroup,
          None,
          loginTimes
        )
        credentialMap(request, block, credentials, data, affinityGroup)
    }
}

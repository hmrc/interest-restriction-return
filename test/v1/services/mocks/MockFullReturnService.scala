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

package v1.services.mocks

import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.http.HeaderCarrier
import v1.connectors.HttpHelper.SubmissionResponse
import v1.controllers.actions.{AuthActionBase, AuthActionProvider, AuthActionProviderImp}
import v1.models.fullReturn.FullReturnModel
import v1.models.requests.IdentifierRequest
import v1.services.FullReturnService

import scala.concurrent.{ExecutionContext, Future}

trait MockFullReturnService extends MockFactory {

  lazy val mockFullReturnService: FullReturnService = mock[FullReturnService]
  lazy val mockAuthProvider: AuthActionProvider = mock[AuthActionProvider]

  def mockFullReturn(model: FullReturnModel)(response: SubmissionResponse): Unit = {
    (mockFullReturnService.submit(_: FullReturnModel)(_: HeaderCarrier, _: ExecutionContext, _: IdentifierRequest[_]))
      .expects(model, *, *, *)
      .returns(Future.successful(response))
  }

  def mockAuthProviderResponse(authAction: AuthActionBase, isInternal: Boolean) : Unit = {
    (mockAuthProvider.apply(_: Boolean)).expects(isInternal).returns(authAction)
  }
}

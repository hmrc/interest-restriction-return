/*
 * Copyright 2020 HM Revenue & Customs
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

package services.mocks

import connectors.HttpHelper.SubmissionHttpResponse
import models.fullReturn.FullReturnModel
import models.requests.IdentifierRequest
import org.scalamock.scalatest.MockFactory
import services.FullReturnService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockFullReturnService extends MockFactory {

  lazy val mockFullReturnService: FullReturnService = mock[FullReturnService]

  def mockFullReturn(model: FullReturnModel)(response: SubmissionHttpResponse): Unit = {
    (mockFullReturnService.submit(_: FullReturnModel)(_: HeaderCarrier, _: ExecutionContext, _: IdentifierRequest[_]))
      .expects(model, *, *, *)
      .returns(Future.successful(response))
  }
}

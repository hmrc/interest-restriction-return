/*
 * Copyright 2022 HM Revenue & Customs
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

import org.scalamock.scalatest.AsyncMockFactory
import v1.connectors.HttpHelper.NrsResponse
import v1.models.nrs._
import v1.connectors.NrsConnector

import scala.concurrent.Future

trait MockNrsConnector extends AsyncMockFactory {

  def mockNrsConnector(): NrsConnector = mock[NrsConnector]

  def mockNrsSubmission(nrsPayload: NrsPayload, mockConnector: NrsConnector)(response: Future[NrsResponse]): Unit =
    (mockConnector
      .send(_: NrsPayload))
      .expects(nrsPayload)
      .returns(response)
}

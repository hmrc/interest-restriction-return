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

package v1.connectors.mocks

import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import v1.connectors.FullReturnConnector
import v1.connectors.HttpHelper.SubmissionResponse
import v1.models.fullReturn.FullReturnModel

import scala.concurrent.Future

trait MockFullReturnConnector extends MockitoSugar {

  lazy val mockFullReturnConnector: FullReturnConnector = mock[FullReturnConnector]

  def mockFullReturn(model: FullReturnModel)(response: SubmissionResponse): Unit =
    when(mockFullReturnConnector.submit(ArgumentMatchers.eq(model))(any(), any(), any()))
      .thenReturn(Future.successful(response))

}

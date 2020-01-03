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

package services

import javax.inject.Inject

import connectors.FullReturnConnector
import connectors.HttpHelper.SubmissionResponse
import models.fullReturn.FullReturnModel
import models.requests.IdentifierRequest
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class FullReturnService @Inject()(fullReturnConnector: FullReturnConnector) extends Submission[FullReturnModel] {

  override def submit(fullReturn: FullReturnModel)
                     (implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[_]): Future[SubmissionResponse] =
    fullReturnConnector.submit(fullReturn)

}

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

package v1.services

import assets.abbreviatedReturn.AbbreviatedReturnConstants._
import v1.connectors.HttpHelper.SubmissionResponse
import v1.connectors.{DesSuccessResponse, UnexpectedFailure}
import v1.connectors.mocks.MockAbbreviatedReturnConnector
import play.api.http.Status._
import utils.BaseSpec

class AbbreviatedReturnServiceSpec extends MockAbbreviatedReturnConnector with BaseSpec {

  "AbbreviatedReturnService.submitsAbbreviatedReturn" when {

    def setup(response: SubmissionResponse): AbbreviatedReturnService = {
      mockAbbreviatedReturn(abbreviatedReturnUltimateParentModel)(response)
      new AbbreviatedReturnService(mockAbbreviatedReturnConnector)
    }

    "submission is successful" should {
      "return a Right(SuccessResponse)" in {

        val service = setup(Right(DesSuccessResponse("ackRef")))
        val result = service.submit(abbreviatedReturnUltimateParentModel)

        await(result) shouldBe Right(DesSuccessResponse("ackRef"))
      }
    }

    "update is unsuccessful" should {

      "return a Left(UnexpectedFailure)" in {

        val service = setup(Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error")))
        val result = service.submit(abbreviatedReturnUltimateParentModel)

        await(result) shouldBe Left(UnexpectedFailure(INTERNAL_SERVER_ERROR, "Error"))
      }
    }
  }
}

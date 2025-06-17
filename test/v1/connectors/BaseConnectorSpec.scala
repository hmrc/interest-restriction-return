/*
 * Copyright 2025 HM Revenue & Customs
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

package v1.connectors

import uk.gov.hmrc.http.{HeaderCarrier, RequestId}
import utils.BaseSpec

class BaseConnectorSpec extends BaseSpec {

  private trait Setup {
    val uuid: String                = "5847df7f-751a-9046-be4e-ffecf1bbac25"
    val connector: BaseConnector = new BaseConnector {
      override def generateNewUUID: String = uuid
    }
  }

  "DesBaseConnector" when {
    ".getCorrelationId" when {
      "requestID is present in the headerCarrier" should {
        "return new ID pre-appending the requestID when the requestID matches the format(8-4-4-4)" in new Setup {
          val requestId = "21ae120b-10a0-9675-ba5e"
          connector.getCorrelationId(HeaderCarrier(requestId = Some(RequestId(requestId)))) shouldBe
            s"$requestId-${uuid.substring(uuid.lastIndexOf('-') + 1)}"
        }

        "return new ID when the requestID does not match the format(8-4-4-4)" in new Setup {
          val requestId = "c48e-b6c0-91fb-8879"
          connector.getCorrelationId(HeaderCarrier(requestId = Some(RequestId(requestId)))) shouldBe uuid
        }
      }

      "requestID is not present in the headerCarrier should return a new ID" should {
        "return the uuid" in new Setup {
          connector.getCorrelationId(HeaderCarrier()) shouldBe uuid
        }
      }
    }
  }
}

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

class DesBaseConnectorSpec extends BaseSpec {

  private trait Setup {
    val uuid: String                = "123f4567-g89c-42c3-b456-557742330000"
    val connector: DesBaseConnector = new DesBaseConnector {
      override def generateNewUUID: String = uuid
    }
  }

  "DesBaseConnector" when {
    ".correlationIdGenerator" when {
      "requestID is present in the headerCarrier" should {
        "return new ID pre-appending the requestID when the requestID matches the format(8-4-4-4)" in new Setup {
          val requestId = "dcba0000-ij12-df34-jk56"
          connector.correlationIdGenerator(HeaderCarrier(requestId = Some(RequestId(requestId)))) shouldBe
            s"$requestId-${uuid.substring(uuid.lastIndexOf('-') + 1)}"
        }

        "return new ID when the requestID does not match the format(8-4-4-4)" in new Setup {
          val requestId = "1a2b-ij12-df34-jk56"
          connector.correlationIdGenerator(HeaderCarrier(requestId = Some(RequestId(requestId)))) shouldBe uuid
        }
      }

      "requestID is not present in the headerCarrier should return a new ID" should {
        "return the uuid" in new Setup {
          connector.correlationIdGenerator(HeaderCarrier()) shouldBe uuid
        }
      }
    }
  }
}

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

package v1.models.nrs

import data.UnitNrsConstants.{nrsCredentials, nrsRetrievalData}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.libs.json.{JsSuccess, Json}
import uk.gov.hmrc.auth.core.retrieve.{AgentInformation, Credentials, ItmpAddress, ItmpName, LoginTimes, MdtpInformation, Name}
import uk.gov.hmrc.auth.core.{AffinityGroup, ConfidenceLevel, CredentialRole}

import java.time.LocalDate

class NrsRetrievalDataSpec extends AnyWordSpecLike with Matchers {
  "NrsRetrievalData" should {
    "support round-trip serialization/deserialization" when {
      "all the fields are available and valid" in {
        Json.toJson(nrsRetrievalData(None)).validate[NrsRetrievalData] shouldBe JsSuccess(nrsRetrievalData(None))
      }
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[NrsRetrievalData] isError
      }
      "empty json" in {
        Json.obj().validate[NrsRetrievalData] isError
      }
    }
  }
}

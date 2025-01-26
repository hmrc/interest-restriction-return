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

import data.UnitNrsConstants.nrsRetrievalData
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.libs.json.{JsError, JsSuccess, Json}
import uk.gov.hmrc.auth.core.retrieve.{AgentInformation, Credentials, ItmpName, MdtpInformation, Name}
import v1.models.nrs.NrsRetrievalData.*

class NrsRetrievalDataSpec extends AnyWordSpecLike with Matchers {
  "NrsRetrievalData" should {
    "support round-trip serialization/deserialization" when {
      "all the fields are available and valid" in {
        Json.toJson(nrsRetrievalData(None)).validate[NrsRetrievalData] shouldBe JsSuccess(nrsRetrievalData(None))
      }
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[NrsRetrievalData] shouldBe a[JsError]
      }
      "empty json" in {
        Json.obj().validate[NrsRetrievalData] shouldBe a[JsError]
      }
    }
  }
  "Credentials"      should {
    "support round-trip serialization/deserialization" when {
      "all the fields are available and valid" in {
        Json.toJson(Credentials("wqs", "dw")).validate[Credentials] shouldBe JsSuccess(Credentials("wqs", "dw"))
      }
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[Credentials] shouldBe a[JsError]
      }
      "empty json" in {
        Json.obj().validate[Credentials] shouldBe a[JsError]
      }
    }
  }
  "Name"             should {
    "support round-trip serialization/deserialization" when {
      "all the fields are available and valid" in {
        Json.toJson(Name(Some("first"), Some("last"))).validate[Name] shouldBe JsSuccess(
          Name(Some("first"), Some("last"))
        )
      }
      "all the optional fields are none" in {
        Json.toJson(Name(None, None)).validate[Name] shouldBe JsSuccess(
          Name(None, None)
        )
      }
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[Name] shouldBe a[JsError]
      }
    }
  }
  "MdtpInformation"  should {
    "support round-trip serialization/deserialization" when {
      "all the fields are available and valid" in {
        Json.toJson(MdtpInformation("deviceId", "sessionId")).validate[MdtpInformation] shouldBe JsSuccess(
          MdtpInformation("deviceId", "sessionId")
        )
      }
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[MdtpInformation] shouldBe a[JsError]
      }
      "empty json" in {
        Json.obj().validate[MdtpInformation] shouldBe a[JsError]
      }
    }
  }
  "AgentInformation" should {
    "support round-trip serialization/deserialization" when {
      "all the fields are available and valid" in {
        Json
          .toJson(AgentInformation(Some("agentId"), Option("agentCode"), Some("agentFriendlyName")))
          .validate[AgentInformation] shouldBe JsSuccess(
          AgentInformation(Some("agentId"), Option("agentCode"), Some("agentFriendlyName"))
        )
      }
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[AgentInformation] shouldBe a[JsError]
      }
      "empty json" in {
        Json.obj().validate[AgentInformation] shouldBe JsSuccess(AgentInformation(None, None, None))
      }
    }
  }
  "ItmpName"         should {
    "support round-trip serialization/deserialization" when {
      "all the fields are available and valid" in {
        Json.toJson(ItmpName(Some("given"), Option("middle"), Some("family"))).validate[ItmpName] shouldBe JsSuccess(
          ItmpName(Some("given"), Option("middle"), Some("family"))
        )
      }
    }
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[ItmpName] shouldBe a[JsError]
      }
      "empty json" in {
        Json.obj().validate[ItmpName] shouldBe JsSuccess(ItmpName(None, None, None))
      }
    }
  }
}

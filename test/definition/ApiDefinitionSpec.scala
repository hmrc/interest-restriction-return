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

package definition

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.libs.json.{JsSuccess, Json}
import utils.BaseSpec

class ApiDefinitionSpec extends AnyWordSpecLike with Matchers {

  private val apiVersion: APIVersion = APIVersion(
    version = "a",
    status = "ALPHA",
    endpointsEnabled = false
  )

  private val apiDefinition: APIDefinition = APIDefinition(
    name = "b",
    description = "c",
    context = "d",
    categories = Seq("e"),
    versions = Seq(apiVersion)
  )

  "APIVersion" should {
    "support round-trip serialization/deserialization" in {
      Json.toJson(apiVersion).validate[APIVersion] shouldBe JsSuccess(apiVersion)
    }
    "version is an empty string" should {
      "throw an IllegalArgumentException" in {
        assertThrows[IllegalArgumentException](
          apiVersion.copy(version = "")
        )
      }
    }
    "fail to read from json when" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[APIVersion] isError
      }
      "there is an empty json" in {
        Json.obj().validate[APIVersion] isError
      }
    }
  }

  "Definition" when {
    "support round-trip serialization/deserialization" in {
      Json.toJson(Definition(apiDefinition)).validate[Definition] shouldBe JsSuccess(Definition(apiDefinition))
    }
    "name is an empty string" should {
      "throw an IllegalArgumentException" in {
        assertThrows[IllegalArgumentException](
          Definition(apiDefinition.copy(name = ""))
        )
      }
    }

    "description is an empty string" should {
      "throw an IllegalArgumentException" in {
        assertThrows[IllegalArgumentException](
          Definition(apiDefinition.copy(description = ""))
        )
      }
    }

    "context is an empty string" should {
      "throw an IllegalArgumentException" in {
        assertThrows[IllegalArgumentException](
          Definition(apiDefinition.copy(context = ""))
        )
      }
    }

    "versions is an empty list" should {
      "throw an IllegalArgumentException" in {
        assertThrows[IllegalArgumentException](
          Definition(apiDefinition.copy(versions = Seq.empty))
        )
      }
    }

    "version numbers for versions is not unique" should {
      "throw an IllegalArgumentException" in {
        assertThrows[IllegalArgumentException](
          Definition(apiDefinition.copy(versions = Seq(apiVersion, apiVersion)))
        )
      }
    }
    "fail to read from json when" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[Definition] isError
      }
      "there is an empty json" in {
        Json.obj().validate[Definition] isError
      }
    }
  }
  "APIDefinition" should {
    "fail to read from json when" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[APIDefinition] isError
      }
      "there is an empty json" in {
        Json.obj().validate[APIDefinition] isError
      }
    }
  }
}

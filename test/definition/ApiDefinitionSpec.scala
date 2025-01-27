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

package definition

import utils.BaseSpec

class ApiDefinitionSpec extends BaseSpec {

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
    "version is an empty string" should {
      "throw an IllegalArgumentException" in {
        assertThrows[IllegalArgumentException](
          apiVersion.copy(version = "")
        )
      }
    }
  }

  "APIDefinition" when {
    "name is an empty string" should {
      "throw an IllegalArgumentException" in {
        assertThrows[IllegalArgumentException](
          apiDefinition.copy(name = "")
        )
      }
    }

    "description is an empty string" should {
      "throw an IllegalArgumentException" in {
        assertThrows[IllegalArgumentException](
          apiDefinition.copy(description = "")
        )
      }
    }

    "context is an empty string" should {
      "throw an IllegalArgumentException" in {
        assertThrows[IllegalArgumentException](
          apiDefinition.copy(context = "")
        )
      }
    }

    "versions is an empty list" should {
      "throw an IllegalArgumentException" in {
        assertThrows[IllegalArgumentException](
          apiDefinition.copy(versions = Seq.empty)
        )
      }
    }

    "version numbers for versions is not unique" should {
      "throw an IllegalArgumentException" in {
        assertThrows[IllegalArgumentException](
          apiDefinition.copy(versions = Seq(apiVersion, apiVersion))
        )
      }
    }
  }
}

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

import definition.Versions.VERSION_1
import mocks.MockAppConfig
import utils.BaseSpec

class ApiDefinitionFactorySpec extends BaseSpec {

  private class Test extends MockAppConfig {
    val apiDefinitionFactory: ApiDefinitionFactory = new ApiDefinitionFactory(mockAppConfig)
    MockedAppConfig.apiGatewayContext returns "organisations/interest-restriction"
  }

  "ApiDefinitionFactory" when {
    ".definition" should {
      "return a valid Definition case class" in new Test {
        MockedAppConfig.apiStatus("1.0") returns "BETA"
        MockedAppConfig.endpointsEnabled returns true

        private val writeScope: String   = "write:interest-restriction-return"
        private val confidenceLevel: Int = 50

        apiDefinitionFactory.definition shouldBe
          Definition(
            scopes = Seq(
              Scope(
                key = writeScope,
                name = "Create and Update your Interest Restriction Return information",
                description = "Allow write access to Interest Restriction Return data",
                confidenceLevel
              )
            ),
            api = APIDefinition(
              name = "Interest Restriction Return (IRR)",
              description = "An API for providing Interest Restriction Return data",
              context = "organisations/interest-restriction",
              categories = Seq("CORPORATION_TAX"),
              versions = Seq(
                APIVersion(
                  version = VERSION_1,
                  status = "BETA",
                  endpointsEnabled = true
                )
              )
            )
          )
      }
    }
  }
}

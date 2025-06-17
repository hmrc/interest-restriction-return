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

package config

import utils.BaseSpec
import org.mockito.Mockito.{mock, when}
import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

class AppConfigSpec extends BaseSpec {

  private trait Test {
    val mockServicesConfig: ServicesConfig = mock(classOf[ServicesConfig])
    val mockConfiguration: Configuration   = mock(classOf[Configuration])

    when(mockConfiguration.getOptional[Boolean]("microservice.services.hip.enabled")).thenReturn(None)
    val appConfig: AppConfig = new AppConfig(mockServicesConfig, mockConfiguration)
  }

  "AppConfig" when {
    ".desUrl" should {
      "return DES URL" in new Test {
        when(mockServicesConfig.baseUrl("des")).thenReturn("http://localhost:9262")

        appConfig.desUrl shouldBe "http://localhost:9262"
      }
    }

    ".desEnvironment" should {
      "return DES Environment" in new Test {
        when(mockServicesConfig.getString("microservice.services.des.environment")).thenReturn("dev")

        appConfig.desEnvironment shouldBe ("Environment", "dev")
      }
    }

    ".desAuthorisationToken" should {
      "return DES authorisation token" in new Test {
        when(mockServicesConfig.getString("microservice.services.des.authorisation-token")).thenReturn("des token")

        appConfig.desAuthorisationToken shouldBe "Bearer des token"
      }
    }

    ".apiGatewayContext" should {
      "return API gateway context" in new Test {
        when(mockServicesConfig.getString("api.gateway.context")).thenReturn("organisations/interest-restriction")

        appConfig.apiGatewayContext shouldBe "organisations/interest-restriction"
      }
    }

    ".apiStatus" should {
      "return API status" in new Test {
        when(mockServicesConfig.getString("api.1.0.status")).thenReturn("BETA")

        appConfig.apiStatus("1.0") shouldBe "BETA"
      }
    }

    ".nrsUrl" should {
      "return None" when {
        "nrs is disabled" in new Test {
          when(mockConfiguration.getOptional[Boolean]("microservice.services.nrs.enabled")).thenReturn(Some(false))

          appConfig.nrsUrl shouldBe None
        }

        "nrs is enabled but no nrsUrl config value is added" in new Test {
          when(mockConfiguration.getOptional[Boolean]("microservice.services.nrs.enabled")).thenReturn(Some(true))
          when(mockConfiguration.getOptional[Configuration]("microservice.services.nrs")).thenReturn(None)

          appConfig.nrsUrl shouldBe None
        }
      }

      "return NRS URL when a nrsUrl config value is added and nrs is enabled" in new Test {
        when(mockConfiguration.getOptional[Boolean]("microservice.services.nrs.enabled")).thenReturn(Some(true))
        when(mockConfiguration.getOptional[Configuration]("microservice.services.nrs"))
          .thenReturn(Some(mockConfiguration))
        when(mockServicesConfig.baseUrl("nrs")).thenReturn("http://localhost:1111")

        appConfig.nrsUrl shouldBe Some("http://localhost:1111")
      }
    }

    ".nrsAuthorisationToken" should {
      "return None when no nrsAuthorisationToken config value is added" in new Test {
        when(mockConfiguration.getOptional[String]("microservice.services.nrs.apikey")).thenReturn(None)

        appConfig.nrsAuthorisationToken shouldBe None
      }

      "return NRS authorisation token when a nrsAuthorisationToken config value is added" in new Test {
        when(mockConfiguration.getOptional[String]("microservice.services.nrs.apikey")).thenReturn(Some("nrs token"))

        appConfig.nrsAuthorisationToken shouldBe Some("nrs token")
      }
    }

    ".nrsEnabled" should {
      "return false" when {
        "no nrsEnabled config value is added" in new Test {
          when(mockConfiguration.getOptional[Boolean]("microservice.services.nrs.enabled")).thenReturn(None)

          appConfig.nrsEnabled shouldBe false
        }

        "a nrsEnabled config value 'false' is added" in new Test {
          when(mockConfiguration.getOptional[Boolean]("microservice.services.nrs.enabled")).thenReturn(Some(false))

          appConfig.nrsEnabled shouldBe false
        }
      }

      "return true when a nrsEnabled config value 'true' is added" in new Test {
        when(mockConfiguration.getOptional[Boolean]("microservice.services.nrs.enabled")).thenReturn(Some(true))

        appConfig.nrsEnabled shouldBe true
      }
    }

    ".endpointsEnabled" should {
      "return false" in new Test {
        when(mockServicesConfig.getBoolean("api-definitions.endpoints.enabled")).thenReturn(false)

        appConfig.endpointsEnabled shouldBe false
      }

      "return true" in new Test {
        when(mockServicesConfig.getBoolean("api-definitions.endpoints.enabled")).thenReturn(true)

        appConfig.endpointsEnabled shouldBe true
      }
    }
  }
}

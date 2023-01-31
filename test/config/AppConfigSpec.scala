/*
 * Copyright 2023 HM Revenue & Customs
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
import assets.AppConfigConstants._

class AppConfigSpec extends BaseSpec {

  "AppConfig" when {
    ".nrsUrl" should {
      "be None when the nrs Config isn't present " in {
        val appConfig = appConfigWithoutNrs
        appConfig.nrsUrl shouldBe None
      }

      "be Some when the nrs Config is present" in {
        val appConfig = appConfigWithNrs
        appConfig.nrsUrl shouldBe Some("http://localhost:1111")
      }
    }

    ".nrsAuthorisationToken" should {
      "be None when the nrs Config isn't present " in {
        val appConfig = appConfigWithoutNrs
        appConfig.nrsAuthorisationToken shouldBe None
      }

      "be Some when the nrs Config is present" in {
        val appConfig = appConfigWithNrs
        appConfig.nrsAuthorisationToken shouldBe Some("some.token")
      }
    }

    ".nrsEnabled" should {
      "be false when the nrs Config isn't present " in {
        val appConfig = appConfigWithoutNrs
        appConfig.nrsEnabled shouldBe false
      }

      "be true when the nrs Config is present" in {
        val appConfig = appConfigWithNrs
        appConfig.nrsEnabled shouldBe true
      }
    }
  }
}

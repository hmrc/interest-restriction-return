/*
 * Copyright 2020 HM Revenue & Customs
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

package models.revokeReportingCompany
import assets.BaseConstants
import assets.revokeReportingCompany.RevokeReportingCompanyConstants._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json
import v1.models.revokeReportingCompany.RevokeReportingCompanyModel

class RevokeReportingCompanyModelSpec extends WordSpec with Matchers with BaseConstants {

  "RevokeReportingCompanyModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = revokeReportingCompanyJsonMax
        val actualValue = Json.toJson(revokeReportingCompanyModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = revokeReportingCompanyJsonMin
        val actualValue = Json.toJson(revokeReportingCompanyModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = revokeReportingCompanyModelMax
        val actualValue = revokeReportingCompanyJsonMax.as[RevokeReportingCompanyModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = revokeReportingCompanyModelMin
        val actualValue = revokeReportingCompanyJsonMin.as[RevokeReportingCompanyModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}

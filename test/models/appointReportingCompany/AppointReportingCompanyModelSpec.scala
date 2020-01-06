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

package models.appointReportingCompany

import assets.BaseConstants
import assets.appointReportingCompany.AppointReportingCompanyConstants._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class AppointReportingCompanyModelSpec extends WordSpec with Matchers with BaseConstants {

  "AppointReportingCompanyModel" must {

    "correctly write to json" when {

      "With Max values" in {

        val expectedValue = appointReportingCompanyJsonMax
        val actualValue = Json.toJson(appointReportingCompanyModelMax)

        actualValue shouldBe expectedValue
      }

      "With Min values" in {

        val expectedValue = appointReportingCompanyJsonMin
        val actualValue = Json.toJson(appointReportingCompanyModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "With Max values" in {

        val expectedValue = appointReportingCompanyModelMax
        val actualValue = appointReportingCompanyJsonMax.as[AppointReportingCompanyModel]

        actualValue shouldBe expectedValue
      }

      "With Min values" in {

        val expectedValue = appointReportingCompanyModelMin
        val actualValue = appointReportingCompanyJsonMin.as[AppointReportingCompanyModel]

        actualValue shouldBe expectedValue
      }
    }

    "correctly collect all of the ukCrns" when {

      "max crns given" in {

        val expectedValue = Seq(crn, crnLetters, crn)
        val actualValue = appointReportingCompanyModelMax.ukCrns

        actualValue shouldBe expectedValue
      }

      "min crns given" in {

        val expectedValue = Seq(crn)
        val actualValue = appointReportingCompanyModelMin.ukCrns

        actualValue shouldBe expectedValue
      }
    }
  }
}

/*
 * Copyright 2019 HM Revenue & Customs
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

package models

import assets.ReportingUltimateParentConstants._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class ReportingUltimateParentModelSpec extends WordSpec with Matchers {

  "ReportingUltimateParentModel" when {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = reportingUltimateParentJsonMax
        val actualValue = Json.toJson(reportingUltimateParentModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = reportingUltimateParentJsonMin
        val actualValue = Json.toJson(reportingUltimateParentModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = reportingUltimateParentModelMax
        val actualValue = reportingUltimateParentJsonMax.as[ReportingUltimateParentModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = reportingUltimateParentModelMin
        val actualValue = reportingUltimateParentJsonMin.as[ReportingUltimateParentModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}


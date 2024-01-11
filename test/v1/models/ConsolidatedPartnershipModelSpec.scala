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

package v1.models

import data.ConsolidatedPartnershipConstants._
import play.api.libs.json.Json
import v1.models.ConsolidatedPartnershipModel
import utils.BaseSpec

class ConsolidatedPartnershipModelSpec extends BaseSpec {

  "ConsolidatedPartnershipModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = consolidatedPartnershipsJsonMax
        val actualValue   = Json.toJson(consolidatedPartnershipsModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = consolidatedPartnershipsJsonMin
        val actualValue   = Json.toJson(consolidatedPartnershipsModelMin)

        actualValue shouldBe expectedValue
      }

      "All data is included in the Json.toJson" in {

        val expectedValue = Json.obj(
          "isElected" -> false,
          "isActive"  -> false
        )

        val actualValue = Json.toJson(consolidatedPartnershipsModelMin)
        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = consolidatedPartnershipsModelMax
        val actualValue   = consolidatedPartnershipsJsonMax.as[ConsolidatedPartnershipModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = consolidatedPartnershipsModelMin
        val actualValue   = consolidatedPartnershipsJsonMin.as[ConsolidatedPartnershipModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}

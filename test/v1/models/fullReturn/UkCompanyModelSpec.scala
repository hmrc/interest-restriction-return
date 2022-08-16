/*
 * Copyright 2022 HM Revenue & Customs
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

package models.fullReturn

import assets.fullReturn.UkCompanyConstants._
import play.api.libs.json.Json
import v1.models.fullReturn.UkCompanyModel
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UkCompanyModelSpec extends AnyWordSpec with Matchers {

  "UkCompanyModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = ukCompanyReactivationJsonMax
        val actualValue   = Json.toJson(ukCompanyModelReactivationMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = ukCompanyJsonMin
        val actualValue   = Json.toJson(ukCompanyModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = ukCompanyModelReactivationMax
        val actualValue   = ukCompanyReactivationJsonMax.as[UkCompanyModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = ukCompanyModelMin
        val actualValue   = ukCompanyJsonMin.as[UkCompanyModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}

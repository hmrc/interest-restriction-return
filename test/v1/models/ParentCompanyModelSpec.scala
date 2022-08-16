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

package models

import assets.ParentCompanyConstants._
import play.api.libs.json.Json
import v1.models.ParentCompanyModel
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ParentCompanyModelSpec extends AnyWordSpec with Matchers {

  "ParentCompanyModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = parentCompanyJsonDeemedUkCompany
        val actualValue   = Json.toJson(parentCompanyModelDeemedUkCompany)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = parentCompanyJsonMin
        val actualValue   = Json.toJson(parentCompanyModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = parentCompanyModelDeemedUkCompany
        val actualValue   = parentCompanyJsonDeemedUkCompany.as[ParentCompanyModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = parentCompanyModelMin
        val actualValue   = parentCompanyJsonDeemedMin.as[ParentCompanyModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}

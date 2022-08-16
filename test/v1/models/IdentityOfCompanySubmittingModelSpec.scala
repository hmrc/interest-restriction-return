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

import play.api.libs.json.Json
import assets.IdentityOfCompanySubmittingConstants._
import v1.models.IdentityOfCompanySubmittingModel
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class IdentityOfCompanySubmittingModelSpec extends AnyWordSpec with Matchers {

  "IdentityOfCompanySubmittingModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = identityOfCompanySubmittingJsonMax
        val actualValue   = Json.toJson(identityOfCompanySubmittingModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = identityOfCompanySubmittingJsonMin
        val actualValue   = Json.toJson(identityOfCompanySubmittingModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = identityOfCompanySubmittingModelMax
        val actualValue   = identityOfCompanySubmittingJsonMax.as[IdentityOfCompanySubmittingModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = identityOfCompanySubmittingModelMin
        val actualValue   = identityOfCompanySubmittingJsonMin.as[IdentityOfCompanySubmittingModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}

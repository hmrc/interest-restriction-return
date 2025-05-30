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

package v1.models

import play.api.libs.json.{JsError, Json}
import data.IdentityOfCompanySubmittingConstants.*
import utils.BaseSpec

class IdentityOfCompanySubmittingModelSpec extends BaseSpec {

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
    "fail to read from json" when {
      "there is type mismatch" in {
        Json.arr("a" -> "b").validate[IdentityOfCompanySubmittingModel] shouldBe a[JsError]
      }
      "empty json" in {
        Json.obj().validate[IdentityOfCompanySubmittingModel] shouldBe a[JsError]
      }
    }
  }
}

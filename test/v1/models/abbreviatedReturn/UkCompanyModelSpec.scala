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

package v1.models.abbreviatedReturn

import data.abbreviatedReturn.UkCompanyConstants.*
import play.api.libs.json.{JsError, Json}
import utils.BaseSpec

class UkCompanyModelSpec extends BaseSpec {

  "UkCompanyModel" must {

    "correctly write to json" in {

      val expectedValue = ukCompanyJson
      val actualValue   = Json.toJson(ukCompanyModel)

      actualValue shouldBe expectedValue
    }

    "correctly read from Json" in {

      val expectedValue = ukCompanyModel
      val actualValue   = ukCompanyJson.as[UkCompanyModel]

      actualValue shouldBe expectedValue
    }
    "should not deserialize wrong type" in {
      Json.arr("a" -> "b").validate[UkCompanyModel] shouldBe a[JsError]
    }
    "should not deserialize empty countryName" in {
      Json
        .obj(
          "companyName" -> Json.obj(),
          "utr"         -> ctutr,
          "consenting"  -> true,
          "qicElection" -> true
        )
        .validate[UkCompanyModel] shouldBe a[JsError]
    }
    "should not deserialize empty utr" in {
      Json
        .obj(
          "companyName" -> companyName,
          "utr"         -> Json.obj(),
          "consenting"  -> true,
          "qicElection" -> true
        )
        .validate[UkCompanyModel] shouldBe a[JsError]
    }
  }
}

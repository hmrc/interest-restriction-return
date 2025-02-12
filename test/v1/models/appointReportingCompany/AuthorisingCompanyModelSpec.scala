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

package v1.models.appointReportingCompany

import play.api.libs.json.{JsError, Json}
import data.AuthorisingCompanyConstants.*
import v1.models.AuthorisingCompanyModel
import utils.BaseSpec

class AuthorisingCompanyModelSpec extends BaseSpec {

  "AuthorisingCompanyModel" must {

    "correctly write to json" in {

      val expectedValue = authorisingCompanyJson
      val actualValue   = Json.toJson(authorisingCompanyModel)

      actualValue shouldBe expectedValue
    }

    "correctly read from Json" in {

      val expectedValue = authorisingCompanyModel
      val actualValue   = authorisingCompanyJson.as[AuthorisingCompanyModel]

      actualValue shouldBe expectedValue
    }
    "fail to read from empty json" in {
      Json.obj().validate[AuthorisingCompanyModel] shouldBe a[JsError]
    }
  }
}

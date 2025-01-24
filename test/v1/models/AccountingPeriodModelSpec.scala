/*
 * Copyright 2024 HM Revenue & Customs
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

import data.AccountingPeriodConstants.*
import play.api.libs.json.{JsError, JsPath, Json}
import utils.BaseSpec

class AccountingPeriodModelSpec extends BaseSpec {

  "AccountingPeriodModel" must {

    "correctly write to json" in {

      val expectedValue = accountingPeriodJson
      val actualValue   = Json.toJson(accountingPeriodModel)

      actualValue shouldBe expectedValue
    }

    "correctly read from Json" in {

      val expectedValue = accountingPeriodModel
      val actualValue   = accountingPeriodJson.as[AccountingPeriodModel]

      actualValue shouldBe expectedValue
    }

    "handle JSON which contains a numeric for the date by returning jsString error" in {

      val invalidJson = Json.obj(
        "startDate" -> 20190101,
        "endDate"   -> endDate
      )

      val expectedValue = JsError(JsPath \ "startDate", "error.expected.jsstring")
      val actualValue   = invalidJson.validate[AccountingPeriodModel]

      actualValue shouldBe expectedValue
    }

    "handle JSON which contains an invalid date format by returning ISO date format error" in {

      val invalidJson = Json.obj(
        "startDate" -> "2019-1-01",
        "endDate"   -> endDate
      )

      val expectedValue = JsError(JsPath \ "startDate", "Date must be in ISO Date format YYYY-MM-DD")
      val actualValue   = invalidJson.validate[AccountingPeriodModel]

      actualValue shouldBe expectedValue
    }

    "handle JSON which contains an invalid date by returning invalid date error" in {

      val invalidJson = Json.obj(
        "startDate" -> "2019-02-29",
        "endDate"   -> endDate
      )

      val expectedValue = JsError(JsPath \ "startDate", "Date must be a valid date")
      val actualValue   = invalidJson.validate[AccountingPeriodModel]

      actualValue shouldBe expectedValue
    }
  }
}

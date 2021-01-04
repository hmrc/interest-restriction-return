/*
 * Copyright 2021 HM Revenue & Customs
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

package v1.schemas.subSchemas

import play.api.libs.json.{JsValue, Json}
import v1.schemas.BaseSchemaSpec
import v1.schemas.helpers._

class AccountingPeriodSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/accountingPeriod.json", "1.0", json)

  "accountingPeriod" when {

    "return valid" when {

      "valid JSON supplied" in {
        val json = Json.toJson(AccountingPeriod())

        validate(json) shouldBe true
      }
    }

    "return invalid" when {

      "startDate is None" in {

        val json = Json.toJson(AccountingPeriod(startDate = None))

        validate(json) shouldBe false
      }

      "endDate is None" in {

        val json = Json.toJson(AccountingPeriod(endDate = None))

        validate(json) shouldBe false
      }
    }
  }
}

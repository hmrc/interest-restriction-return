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

package schemas.subSchemas

import play.api.libs.json.{JsValue, Json}
import schemas.BaseSchemaSpec
import schemas.helpers._

class GroupCompanyDetailsSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean =
    validateJson("subSchemas/groupCompanyDetails.json", json)

  "groupCompanyDetails" when {

    "totalCompanies" when {

      "is None" in {

        val json = Json.toJson(GroupCompanyDetails(
            totalCompanies = None
        ))

        validate(json) shouldBe false
      }

      "is 0" in {

        val json = Json.toJson(GroupCompanyDetails(
            totalCompanies = Some(0)
        ))

        validate(json) shouldBe false
      }
    }

    "inclusionOfNonConsentingCompanies" when {

      "is None" in {

        val json = Json.toJson(GroupCompanyDetails(
            inclusionOfNonConsentingCompanies = None
        ))

        validate(json) shouldBe false
      }
    }

    "accountingPeriod" when {

      "startDate is None" in {

        val json = Json.toJson(GroupCompanyDetails(
            accountingPeriod = Some(AccountingPeriod(startDate = None))
        ))

        validate(json) shouldBe false
      }

      "endDate is None" in {

        val json = Json.toJson(GroupCompanyDetails(
            accountingPeriod = Some(AccountingPeriod(endDate = None))
        ))

        validate(json) shouldBe false
      }

      "is None" in {

        val json = Json.toJson(GroupCompanyDetails(
            accountingPeriod = None
        ))

        validate(json) shouldBe false
      }
    }
  }
}

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
import schemas.{BaseSchemaSpec, FullSchemaBaseSpec}

class AllocatedReactivations extends FullSchemaBaseSpec{

  def validate(json: JsValue): Boolean = validateJson("subSchemas/allocatedReactivations.json", json)

  "allocatedReactivations" when {

    "ap1NetDisallowances" when {

      "is None" in {

        val json = Json.toJson(GroupCompanyDetails(
            totalCompanies = Some(0)
        ))

        validate(json) shouldBe false
      }
    }

    "currentPeriodReactivation" when {

      "is 0" in {

        val json = Json.toJson(GroupCompanyDetails(
          totalCompanies = Some(0)
        ))

        validate(json) shouldBe false
      }
    }

    "totalReactivation" when {

      "is 0" in {

        val json = Json.toJson(GroupCompanyDetails(
          totalCompanies = Some(0)
        ))

        validate(json) shouldBe false
      }
    }

    "reactivationCap" when {

      "is 0" in {

        val json = Json.toJson(GroupCompanyDetails(
          totalCompanies = Some(0)
        ))

        validate(json) shouldBe false
      }
    }
  }
}

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

package schemas

import play.api.libs.json.{JsValue, Json}
import schemas.helpers.revokeReportingCompany.RevokeReportingCompanyModel

class RevokeReportingCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("revokeReportingCompanySchema.json", json)

  "RevokeReportingCompany Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload" in {

        val json = Json.toJson(RevokeReportingCompanyModel())
        validate(json) shouldBe true
      }
    }

    "Return Invalid" when {

      "agentDetails" when {

        "is not supplied" in {

          val json = Json.toJson(RevokeReportingCompanyModel(agentDetails = None))

          validate(json) shouldBe false
        }
      }

      "reportingCompany" when {

        "is not supplied" in {

          val json = Json.toJson(RevokeReportingCompanyModel(reportingCompany = None))

          validate(json) shouldBe false
        }
      }

      "declaration" when {

        "not supplied" in {

          val json = Json.toJson(RevokeReportingCompanyModel(declaration = None))

          validate(json) shouldBe false
        }
      }
    }
  }
}

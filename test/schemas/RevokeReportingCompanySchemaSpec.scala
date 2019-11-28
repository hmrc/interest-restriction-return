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
import schemas.helpers.AccountingPeriod
import schemas.helpers.revokeReportingCompany.RevokeReportingCompanyModel

class RevokeReportingCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("revokeReportingCompanySchema.json", json)

  "RevokeReportingCompany Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload with all data items present" in {

        val json = Json.toJson(RevokeReportingCompanyModel())
        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with optional data items not present" in {

        val json = Json.toJson(RevokeReportingCompanyModel(
          companyMakingRevocation = None,
          ultimateParent = None
        ))
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

      "isReportingCompanyRevokingItself" when {

        "is not supplied" in {

          val json = Json.toJson(RevokeReportingCompanyModel(isReportingCompanyRevokingItself = None))

          validate(json) shouldBe false
        }
      }

      "accountingPeriod" when {

        "startDate is None" in {

          val json = Json.toJson(RevokeReportingCompanyModel(
            accountingPeriod = Some(AccountingPeriod(startDate = None))
          ))

          validate(json) shouldBe false
        }

        "endDate is None" in {

          val json = Json.toJson(RevokeReportingCompanyModel(
            accountingPeriod = Some(AccountingPeriod(endDate = None))
          ))

          validate(json) shouldBe false
        }

        "is None" in {

          val json = Json.toJson(RevokeReportingCompanyModel(
            accountingPeriod = None
          ))

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

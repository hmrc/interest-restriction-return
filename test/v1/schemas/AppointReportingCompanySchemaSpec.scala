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

package v1.schemas

import play.api.libs.json.{JsValue, Json}
import v1.schemas.helpers._
import v1.schemas.helpers.appointReportingCompany.AppointReportingCompanyModel

class AppointReportingCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("appointReportingCompanySchema.json", "1.0", json)

  "AppointReportingCompany Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload" in {

        val json = Json.toJson(AppointReportingCompanyModel())
        validate(json) shouldBe true
      }

      "Start Date is a valid date" in {

        val json = Json.toJson(AppointReportingCompanyModel(
          accountingPeriod = Some(AccountingPeriod(startDate = Some("2020-09-28"), endDate = Some("2020-10-01")))
        ))

        validate(json) shouldBe true
      }

      "Reporting Ultimate Parent is None" in {

        val json = Json.toJson(AppointReportingCompanyModel(
          ultimateParentCompany = None
        ))

        validate(json) shouldBe true
      }

      "Identity of Appointing Company is None" in {

        val json = Json.toJson(AppointReportingCompanyModel(
          identityOfAppointingCompany = None
        ))

        validate(json) shouldBe true
      }
    }

    "Return Invalid" when {

      "agentName" when {

        "is not supplied" in {

          val json = Json.toJson(AppointReportingCompanyModel(agentDetails = None))

          validate(json) shouldBe false
        }
      }

      "reportingCompany" when {

        "is not supplied" in {

          val json = Json.toJson(AppointReportingCompanyModel(reportingCompany = None))

          validate(json) shouldBe false
        }
      }

      "authorisingCompanies" when {

        "empty sequence" in {

          val json = Json.toJson(AppointReportingCompanyModel(authorisingCompanies = Some(Seq())))

          validate(json) shouldBe false
        }

        "not supplied" in {

          val json = Json.toJson(AppointReportingCompanyModel(authorisingCompanies = None))

          validate(json) shouldBe false
        }
      }

      "declaration" when {

        "not supplied" in {

          val json = Json.toJson(AppointReportingCompanyModel(declaration = None))

          validate(json) shouldBe false
        }
      }

      "Accounting Period" when {

        "Start Date is None" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            accountingPeriod = Some(AccountingPeriod(startDate = None))
          ))

          validate(json) shouldBe false
        }

        "End Date is None" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            accountingPeriod = Some(AccountingPeriod(endDate = None))
          ))

          validate(json) shouldBe false
        }
      }
    }
  }
}


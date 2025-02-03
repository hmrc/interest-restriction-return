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

import data.appointReportingCompany.AppointReportingCompanyConstants.*
import play.api.libs.json.{JsError, JsObject, Json}
import utils.BaseSpec

class AppointReportingCompanyModelSpec extends BaseSpec {

  "AppointReportingCompanyModel" must {

    "correctly write to json" when {

      "With Max values" in {

        val expectedValue = appointReportingCompanyJsonMax
        val actualValue   = Json.toJson(appointReportingCompanyModelMax)

        actualValue shouldBe expectedValue
      }

      "With Min values" in {

        val expectedValue = appointReportingCompanyJsonMin
        val actualValue   = Json.toJson(appointReportingCompanyModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "With Max values" in {

        val expectedValue = appointReportingCompanyModelMax
        val actualValue   = appointReportingCompanyJsonMax.as[AppointReportingCompanyModel]

        actualValue shouldBe expectedValue
      }

      "With Min values" in {

        val expectedValue = appointReportingCompanyModelMin
        val actualValue   = appointReportingCompanyJsonMin.as[AppointReportingCompanyModel]

        actualValue shouldBe expectedValue
      }
      "fail to read from empty json" in {
        Json.obj().validate[AppointReportingCompanyModel] shouldBe a[JsError]
      }
      "fail to read when there is type mismatch" in {
        val mismatched: JsObject = Json.obj(
          "agentDetails"                       -> "agentDetailsJsonMax",
          "reportingCompany"                   -> "reportingCompanyJson",
          "authorisingCompanies"               -> 1,
          "isReportingCompanyAppointingItself" -> "kjll",
          "identityOfAppointingCompany"        -> "identityOfCompanySubmittingJsonMax",
          "ultimateParentCompany"              -> "ultimateParentJsonUkCompany",
          "accountingPeriod"                   -> "accountingPeriodJson",
          "declaration"                        -> "ljn"
        )
        mismatched.validate[AppointReportingCompanyModel] shouldBe a[JsError]
      }
    }
  }
}

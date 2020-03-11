/*
 * Copyright 2020 HM Revenue & Customs
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
import v1.models.UTRModel
import v1.schemas.BaseSchemaSpec
import v1.schemas.helpers._

class ReportingCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/reportingCompany.json", "1.0", json)

  "ReportingCompany Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload" in {

        val json = Json.toJson(ReportingCompany())

        validate(json) shouldBe true
      }

    }

    "Return Invalid" when {

      "companyName" when {

        "companyName is empty" in {

          val json = Json.toJson(ReportingCompany(companyName = Some("")))

          validate(json) shouldBe false
        }

        "companyName is not applied" in {

          val json = Json.toJson(ReportingCompany(companyName = None))

          validate(json) shouldBe false
        }

        "companyName exceeds 160 characters" in {

          val json = Json.toJson(ReportingCompany(companyName = Some("A" * (maxCompanyNameLength + 1))))

          validate(json) shouldBe false
        }
      }

      "ctutr" when {

        s"below $utrLength" in {

          val json = Json.toJson(ReportingCompany(ctutr = Some(UTRModel("1" * (utrLength - 1)))))

          validate(json) shouldBe false
        }

        s"above $utrLength" in {

          val json = Json.toJson(ReportingCompany(ctutr = Some(UTRModel("1" * (utrLength + 1)))))

          validate(json) shouldBe false
        }

        "is non numeric" in {

          val json = Json.toJson(ReportingCompany(ctutr = Some(UTRModel("a" * (utrLength)))))

          validate(json) shouldBe false
        }

        "is a symbol" in {

          val json = Json.toJson(ReportingCompany(ctutr = Some(UTRModel("@"))))

          validate(json) shouldBe false
        }

        "is not applied" in {

          val json = Json.toJson(ReportingCompany(ctutr = None))

          validate(json) shouldBe false
        }
      }

      "sameAsUltimateParent" when {

        "is not applied" in {

          val json = Json.toJson(ReportingCompany(sameAsUltimateParent = None))

          validate(json) shouldBe false
        }
      }
    }
  }
}
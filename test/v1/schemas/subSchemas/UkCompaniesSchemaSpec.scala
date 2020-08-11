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
import v1.schemas.BaseSchemaSpec
import v1.schemas.helpers._
import v1.schemas.helpers.abbreviatedReturn.UKCompanies
import v1.models.UTRModel

class UkCompaniesSchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean =
    validateJson("subSchemas/ukCompanies.json", "1.0", json)

  "ukCompanies JSON schema" should {

    "return valid" when {

      "valid JSON data is submitted" in {

        val json = Json.toJson(Seq(UKCompanies()))

        validate(json) shouldBe true
      }
    }

    "return invalid" when {

      "companyName" when {

        "is empty" in {

          val json = Json.toJson(Seq(UKCompanies(companyName = Some(""))))

          validate(json) shouldBe false
        }

        s"is over $maxCompanyNameLength" in {

          val json = Json.toJson(Seq(UKCompanies(companyName = Some("A" * (maxCompanyNameLength + 1)))))

          validate(json) shouldBe false
        }

        "is None" in {

          val json = Json.toJson(Seq(UKCompanies(companyName = None)))

          validate(json) shouldBe false
        }
      }

      "utr" when {

        "is empty" in {

          val json = Json.toJson(Seq(UKCompanies(utr = None)))

          validate(json) shouldBe false
        }

        s"below $utrLength" in {

          val json = Json.toJson(Seq(UKCompanies(utr = Some(UTRModel("1" * (utrLength - 1))))))

          validate(json) shouldBe false
        }

        s"above $utrLength" in {

          val json = Json.toJson(Seq(UKCompanies(utr = Some(UTRModel("1" * (utrLength + 1))))))

          validate(json) shouldBe false
        }

        "is non numeric" in {

          val json = Json.toJson(Seq(UKCompanies(utr = Some(UTRModel("a" * utrLength)))))

          validate(json) shouldBe false
        }

        "is a symbol" in {

          val json = Json.toJson(Seq(UKCompanies(utr = Some(UTRModel("@")))))

          validate(json) shouldBe false
        }
      }

      "consenting" when {

        "is empty" in {

          val json = Json.toJson(Seq(UKCompanies(consenting = None)))

          validate(json) shouldBe false
        }
      }

      "qicElection" when {

        "is empty" in {

          val json = Json.toJson(Seq(UKCompanies(qicElection = None)))

          validate(json) shouldBe false
        }
      }
    }
  }

}

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
import v1.schemas.helpers.fullReturn.UkCompanyFull
import v1.models.UTRModel

class UkCompanyFullSchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean =
    validateJson("subSchemas/ukCompanyFull.json", "1.0", json)

  "UkCompanyFull JSON schema" should {

    "return valid" when {

      "valid JSON data is submitted" in {

        val json = Json.toJson(Seq(UkCompanyFull()))

        validate(json) shouldBe true
      }

      "allocated restrictions" when {

        "is None" in {

          val json = Json.toJson(Seq(UkCompanyFull(allocatedRestrictions = None)))

          validate(json) shouldBe true
        }
      }

      "allocated reactivations" when {

        "is None" in {

          val json = Json.toJson(Seq(UkCompanyFull(allocatedReactivations = None)))

          validate(json) shouldBe true
        }


      }
    }

    "return invalid" when {

      "companyName" when {

        "is empty" in {

          val json = Json.toJson(Seq(UkCompanyFull(companyName = Some(""))))

          validate(json) shouldBe false
        }

        s"is over $maxCompanyNameLength" in {

          val json = Json.toJson(Seq(UkCompanyFull(companyName = Some("A" * (maxCompanyNameLength + 1)))))

          validate(json) shouldBe false
        }

        "is None" in {

          val json = Json.toJson(Seq(UkCompanyFull(companyName = None)))

          validate(json) shouldBe false
        }
      }

      "ctutr" when {

        s"below $utrLength" in {

          val json = Json.toJson(Seq(UkCompanyFull(ctutr = Some(UTRModel("1" * (utrLength - 1))))))

          validate(json) shouldBe false
        }

        s"above $utrLength" in {

          val json = Json.toJson(Seq(UkCompanyFull(ctutr = Some(UTRModel("1" * (utrLength + 1))))))

          validate(json) shouldBe false
        }

        "is non numeric" in {

          val json = Json.toJson(Seq(UkCompanyFull(ctutr = Some(UTRModel("a" * utrLength)))))

          validate(json) shouldBe false
        }

        "is a symbol" in {

          val json = Json.toJson(Seq(UkCompanyFull(ctutr = Some(UTRModel("@")))))

          validate(json) shouldBe false
        }
      }

      "consenting" when {

        "is blank" in {

          val json = Json.toJson(Seq(UkCompanyFull(consenting = None)))

          validate(json) shouldBe false
        }
      }

      "netTaxInterestExpense" when {

        "is negative" in {

          val json = Json.toJson(Seq(UkCompanyFull(netTaxInterestExpense = Some(-1))))

          validate(json) shouldBe false
        }
      }

      "netTaxInterestIncome" when {

        "is negative" in {

          val json = Json.toJson(Seq(UkCompanyFull(netTaxInterestIncome = Some(-1))))

          validate(json) shouldBe false
        }
      }

      "taxEBITDA" when {

        "is None" in {

          val json = Json.toJson(Seq(UkCompanyFull(taxEBITDA = None)))

          validate(json) shouldBe false
        }
      }
    }
  }
}

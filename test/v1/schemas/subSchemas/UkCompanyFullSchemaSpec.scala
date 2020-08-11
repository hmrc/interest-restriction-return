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
import v1.schemas.helpers.fullReturn._

class UkCompanyFullSchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean =
    validateJson("subSchemas/ukCompanyFull.json", "1.0", json)

  val validRestrictionModelIncomeZero = UkCompanyFull(netTaxInterestIncome = Some(0), allocatedReactivations = None)
  val invalidRestrictionModelIncomeNone = UkCompanyFull(netTaxInterestIncome = None, allocatedReactivations = None)
  val validReactivationsUkCompanyModel = UkCompanyFull(netTaxInterestExpense = Some(0), netTaxInterestIncome = Some(1000000), allocatedRestrictions = None)


  "UkCompanyFull JSON schema" should {

    "return valid" when {

      "valid JSON data is submitted" in {

        val json = Json.toJson(Seq(validRestrictionModelIncomeZero))

        validate(json) shouldBe true
      }

      "valid Json data for allocated restrictions" in {

        val json = Json.toJson(Seq(validRestrictionModelIncomeZero))

        validate(json) shouldBe true
      }

      "valid Json data for allocated reactivations" in {

        val json = Json.toJson(Seq(validReactivationsUkCompanyModel))

        validate(json) shouldBe true

      }

      "valid Json data for allocated restrictions when netTaxIncome = 0" in {

        val json = Json.toJson(Seq(invalidRestrictionModelIncomeNone.copy(netTaxInterestIncome = Some(0))))

        validate(json) shouldBe true
      }
    }

    "return invalid" when {

      "companyName" when {

        "is empty" in {

          val json = Json.toJson(Seq(validRestrictionModelIncomeZero.copy(companyName = Some(""))))

          validate(json) shouldBe false
        }

        s"is over $maxCompanyNameLength" in {

          val json = Json.toJson(Seq(validRestrictionModelIncomeZero.copy(companyName = Some("A" * (maxCompanyNameLength + 1)))))

          validate(json) shouldBe false
        }

        "is None" in {

          val json = Json.toJson(Seq(validRestrictionModelIncomeZero.copy(companyName = None)))

          validate(json) shouldBe false
        }
      }

      "utr" when {

        s"below $utrLength" in {

          val json = Json.toJson(Seq(validRestrictionModelIncomeZero.copy(utr = Some(UTRModel("1" * (utrLength - 1))))))

          validate(json) shouldBe false
        }

        s"above $utrLength" in {

          val json = Json.toJson(Seq(validRestrictionModelIncomeZero.copy(utr = Some(UTRModel("1" * (utrLength + 1))))))

          validate(json) shouldBe false
        }

        "is non numeric" in {

          val json = Json.toJson(Seq(validRestrictionModelIncomeZero.copy(utr = Some(UTRModel("a" * utrLength)))))

          validate(json) shouldBe false
        }

        "is a symbol" in {

          val json = Json.toJson(Seq(validRestrictionModelIncomeZero.copy(utr = Some(UTRModel("@")))))

          validate(json) shouldBe false
        }
      }

      "consenting" when {

        "is blank" in {

          val json = Json.toJson(Seq(validRestrictionModelIncomeZero.copy(consenting = None)))

          validate(json) shouldBe false
        }
      }

      "qicElection" when {

        "is blank" in {

          val json = Json.toJson(Seq(validRestrictionModelIncomeZero.copy(qicElection = None)))

          validate(json) shouldBe false
        }
      }

      "netTaxInterestExpense" when {

        "is negative" in {

          val json = Json.toJson(Seq(validRestrictionModelIncomeZero.copy(netTaxInterestExpense = Some(-1))))

          validate(json) shouldBe false
        }
      }

      "netTaxInterestIncome" when {

        "is negative" in {

          val json = Json.toJson(Seq(validReactivationsUkCompanyModel.copy(netTaxInterestIncome = Some(-1))))

          validate(json) shouldBe false
        }
      }

      "taxEBITDA" when {

        "is None" in {

          val json = Json.toJson(Seq(validRestrictionModelIncomeZero.copy(taxEBITDA = None)))

          validate(json) shouldBe false
        }
      }
    }
  }
}


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

package schemas.subSchemas

import play.api.libs.json.{JsValue, Json}
import schemas.BaseSchemaSpec
import schemas.helpers._
import v1.models.{CRNModel, UTRModel}

class DeemedCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/deemedParent.json", json)

  "DeemedCompany Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload with one deemed parent" in {

        val json = Json.toJson(Seq(DeemedParent()))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with two deemed parents" in {

        val json = Json.toJson(Seq(DeemedParent(), DeemedParent()))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with three deemed parents" in {

        val json = Json.toJson(Seq(DeemedParent(), DeemedParent(), DeemedParent()))

        validate(json) shouldBe true
      }
    }

    "Return invalid" when {

      "company name" when {

        "is None" in {

          val json = Json.toJson(Seq(DeemedParent(companyName = None
          )))

          validate(json) shouldBe false
        }

        s"is empty" in {

          val json = Json.toJson(Seq(DeemedParent(
            companyName = Some("")
          )))

          validate(json) shouldBe false
        }

        s"is longer than $maxCompanyNameLength characters" in {

          val json = Json.toJson(Seq(DeemedParent(
            companyName = Some("A" * (maxCompanyNameLength + 1))
          )))

          validate(json) shouldBe false
        }
      }

      "ctutr" when {

        s"below $utrLength" in {

          val json = Json.toJson(Seq(DeemedParent(
            ctutr = Some(UTRModel("1" * (utrLength - 1)))
          )))

          validate(json) shouldBe false
        }

        s"above $utrLength" in {

          val json = Json.toJson(Seq(DeemedParent(
            ctutr = Some(UTRModel("1" * (utrLength + 1)))
          )))

          validate(json) shouldBe false
        }

        "is non numeric" in {

          val json = Json.toJson(Seq(DeemedParent(
            ctutr = Some(UTRModel("a" * utrLength))
          )))

          validate(json) shouldBe false
        }

        "is a symbol" in {

          val json = Json.toJson(Seq(DeemedParent(
            ctutr = Some(UTRModel("@"))
          )))

          validate(json) shouldBe false
        }
      }

      "crn" when {

        s"below $crnLength" in {

          val json = Json.toJson(Seq(DeemedParent(
            crn = Some(CRNModel("1" * (crnLength - 1)))
          )))

          validate(json) shouldBe false
        }

        s"above $crnLength" in {

          val json = Json.toJson(Seq(DeemedParent(
            crn = Some(CRNModel("1" * (crnLength + 1)))
          )))

          validate(json) shouldBe false
        }
      }

      "knownAs" when {

        "knownAs is empty" in {

          val json = Json.toJson(Seq(DeemedParent(
            knownAs = Some("")
          )))

          validate(json) shouldBe false
        }

        s"is longer than $maxCompanyNameLength characters" in {

          val json = Json.toJson(Seq(DeemedParent(
            knownAs = Some("A" * (maxCompanyNameLength + 1))
          )))

          validate(json) shouldBe false
        }
      }

      "countryOfIncorporation" when {

        "is only one letter" in {

          val json = Json.toJson(Seq(DeemedParent(
            countryOfIncorporation = Some("A")
          )))

          validate(json) shouldBe false
        }

        "is three letters" in {

          val json = Json.toJson(Seq(DeemedParent(
            countryOfIncorporation = Some("AAA")
          )))

          validate(json) shouldBe false
        }

        "contains a number" in {

          val json = Json.toJson(Seq(DeemedParent(
            countryOfIncorporation = Some("A1")
          )))

          validate(json) shouldBe false
        }

        "contains a symbol" in {

          val json = Json.toJson(Seq(DeemedParent(
            countryOfIncorporation = Some("A@")
          )))

          validate(json) shouldBe false
        }
      }
    }
  }
}
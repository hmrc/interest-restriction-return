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
import schemas.BaseSchemaSpec
import schemas.helpers._

class UltimateParentCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/ultimateParent.json", json)

  "UltimateParent Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload with UK Ultimate company" in {

        val json = Json.toJson(UkUltimateParent())

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with NonUK Ultimate company" in {

        val json = Json.toJson(NonUkUltimateParent())

        validate(json) shouldBe true
      }
    }

    "Return invalid" when {

      "Uk Company" when {

        "company name" when {

          "is None" in {

            val json = Json.toJson(UkUltimateParent(registeredCompanyName = None))

            validate(json) shouldBe false
          }

          s"is empty" in {

            val json = Json.toJson(UkUltimateParent(registeredCompanyName = Some("")))

            validate(json) shouldBe false
          }

          s"is longer than $maxCompanyNameLength characters" in {

            val json = Json.toJson(UkUltimateParent(registeredCompanyName = Some("A" * (maxCompanyNameLength + 1))))

            validate(json) shouldBe false
          }
        }

        "ctutr" when {

          s"below $utrLength" in {

            val json = Json.toJson(UkUltimateParent(ctutr = Some("1" * (utrLength - 1))))

            validate(json) shouldBe false
          }

          s"above $utrLength" in {

            val json = Json.toJson(UkUltimateParent(ctutr = Some("1" * (utrLength + 1))))

            validate(json) shouldBe false
          }

          "is non numeric" in {

            val json = Json.toJson(UkUltimateParent(ctutr = Some("a" * utrLength)))

            validate(json) shouldBe false
          }

          "is a symbol" in {

            val json = Json.toJson(UkUltimateParent(ctutr = Some("@")))

            validate(json) shouldBe false
          }
        }

        "crn" when {

          s"below $crnLength" in {

            val json = Json.toJson(UkUltimateParent(crn = Some("1" * (crnLength - 1))))

            validate(json) shouldBe false
          }

          s"above $crnLength" in {

            val json = Json.toJson(UkUltimateParent(crn = Some("1" * (crnLength + 1))))

            validate(json) shouldBe false
          }

          "starts with 1 letter" in {
            val json = Json.toJson(UkUltimateParent(crn = Some("A" + ("1" * (crnLength - 1)))))

            validate(json) shouldBe false
          }

          "starts with 3 letters" in {
            val json = Json.toJson(UkUltimateParent(crn = Some("AAA" + ("1" * (crnLength - 3)))))

            validate(json) shouldBe false

          }
        }

        "sautr" when {

          s"below $utrLength" in {

            val json = Json.toJson(UkUltimateParent(sautr = Some("1" * (utrLength - 1))))

            validate(json) shouldBe false
          }

          s"above $utrLength" in {

            val json = Json.toJson(UkUltimateParent(sautr = Some("1" * (utrLength + 1))))

            validate(json) shouldBe false
          }

          "is non numeric" in {

            val json = Json.toJson(UkUltimateParent(sautr = Some("a" * utrLength)))

            validate(json) shouldBe false
          }

          "is a symbol" in {

            val json = Json.toJson(UkUltimateParent(sautr = Some("@")))

            validate(json) shouldBe false
          }
        }

        "knownAs" when {

          "knownAs is empty" in {

            val json = Json.toJson(UkUltimateParent(knownAs = Some("")))

            validate(json) shouldBe false
          }

          s"is longer than $maxCompanyNameLength characters" in {

            val json = Json.toJson(UkUltimateParent(knownAs = Some("A" * (maxCompanyNameLength + 1))))

            validate(json) shouldBe false
          }
        }
      }

      "non-uk company" when {

        "company name" when {

          "is None" in {

            val json = Json.toJson(NonUkUltimateParent(registeredCompanyName = None))

            validate(json) shouldBe false
          }

          s"is empty" in {

            val json = Json.toJson(NonUkUltimateParent(registeredCompanyName = Some("")))

            validate(json) shouldBe false
          }

          s"is longer than $maxCompanyNameLength characters" in {

            val json = Json.toJson(NonUkUltimateParent(registeredCompanyName = Some("A" * (maxCompanyNameLength + 1))))

            validate(json) shouldBe false
          }
        }

        "countryOfIncorporation" when {

          "is only one letter" in {

            val json = Json.toJson(NonUkUltimateParent(countryOfIncorporation = Some("A")))
            validate(json) shouldBe false
          }

          "is three letters" in {

            val json = Json.toJson(NonUkUltimateParent(countryOfIncorporation = Some("AAA")))
            validate(json) shouldBe false
          }

          "contains a number" in {

            val json = Json.toJson(NonUkUltimateParent(countryOfIncorporation = Some("A1")))
            validate(json) shouldBe false
          }

          "contains a symbol" in {

            val json = Json.toJson(NonUkUltimateParent(countryOfIncorporation = Some("A@")))
            validate(json) shouldBe false
          }
        }

        "non-uk crn" when {

          "is None" in {

            val json = Json.toJson(NonUkUltimateParent(crn = None))

            validate(json) shouldBe false
          }

          s"is empty" in {

            val json = Json.toJson(NonUkUltimateParent(crn = Some("")))

            validate(json) shouldBe false
          }
        }

        "knownAs" when {

          "knownAs is empty" in {

            val json = Json.toJson(NonUkUltimateParent(knownAs = Some("")))

            validate(json) shouldBe false
          }
        }

        s"is longer than $maxCompanyNameLength characters" in {

          val json = Json.toJson(NonUkUltimateParent(knownAs = Some("A" * (maxCompanyNameLength + 1))))
          validate(json) shouldBe false
        }
      }
    }
  }
}
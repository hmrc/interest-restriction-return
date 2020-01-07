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

import models.{CRNModel, UTRModel}
import play.api.libs.json.{JsValue, Json}
import schemas.BaseSchemaSpec
import schemas.helpers._

class UltimateParentCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/ultimateParent.json", json)

  "UltimateParent Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload with Ultimate Parent Company" in {

        val json = Json.toJson(UltimateParent())

        validate(json) shouldBe true
      }

      "crn is None" in {

        val json = Json.toJson(UltimateParent(crn = None))

        validate(json) shouldBe true
      }

      "Non Uk crn is None" in {

        val json = Json.toJson(UltimateParent(nonUkCrn = None))

        validate(json) shouldBe true
      }

      "KnownAs is None" in {

        val json = Json.toJson(UltimateParent(knownAs = None))

        validate(json) shouldBe true
      }

      "Country Of Incorporation is None" in {

        val json = Json.toJson(UltimateParent(countryOfIncorporation = None))

        validate(json) shouldBe true
      }

      "ctutr is None" in {

        val json = Json.toJson(UltimateParent(ctutr = None))

        validate(json) shouldBe true
      }
    }

    "Return invalid" when {

      "company name" when {

        "is None" in {

          val json = Json.toJson(UltimateParent(registeredCompanyName = None))

          validate(json) shouldBe false
        }

        s"is empty" in {

          val json = Json.toJson(UltimateParent(registeredCompanyName = Some("")))

          validate(json) shouldBe false
        }

        s"is longer than $maxCompanyNameLength characters" in {

          val json = Json.toJson(UltimateParent(registeredCompanyName = Some("A" * (maxCompanyNameLength + 1))))

          validate(json) shouldBe false
        }
      }

      "ctutr" when {

        s"below $utrLength" in {

          val json = Json.toJson(UltimateParent(ctutr = Some(UTRModel("1" * (utrLength - 1)))))

          validate(json) shouldBe false
        }

        s"above $utrLength" in {

          val json = Json.toJson(UltimateParent(ctutr = Some(UTRModel("1" * (utrLength + 1)))))

          validate(json) shouldBe false
        }

        "is non numeric" in {

          val json = Json.toJson(UltimateParent(ctutr = Some(UTRModel("a" * utrLength))))

          validate(json) shouldBe false
        }

        "is a symbol" in {

          val json = Json.toJson(UltimateParent(ctutr = Some(UTRModel("@" * utrLength))))

          validate(json) shouldBe false
        }
      }

      "crn" when {

        s"below $crnLength" in {

          val json = Json.toJson(UltimateParent(crn = Some(CRNModel(("1" * (crnLength - 1))))))

          validate(json) shouldBe false
        }

        s"above $crnLength" in {

          val json = Json.toJson(UltimateParent(crn = Some(CRNModel(("1" * (crnLength + 1))))))

          validate(json) shouldBe false
        }

        "starts with 1 letter" in {
          val json = Json.toJson(UltimateParent(crn = Some(CRNModel(("A" + ("1" * (crnLength - 1)))))))

          validate(json) shouldBe false
        }

        "starts with 3 letters" in {
          val json = Json.toJson(UltimateParent(crn = Some(CRNModel(("AAA" + ("1" * (crnLength - 3)))))))

          validate(json) shouldBe false

        }
      }

      "knownAs" when {

        "knownAs is empty" in {

          val json = Json.toJson(UltimateParent(knownAs = Some("")))

          validate(json) shouldBe false
        }

        s"is longer than $maxCompanyNameLength characters" in {

          val json = Json.toJson(UltimateParent(knownAs = Some("A" * (maxCompanyNameLength + 1))))

          validate(json) shouldBe false
        }
      }

      "countryOfIncorporation" when {

        "is only one letter" in {

          val json = Json.toJson(UltimateParent(countryOfIncorporation = Some("A")))
          validate(json) shouldBe false
        }

        "is three letters" in {

          val json = Json.toJson(UltimateParent(countryOfIncorporation = Some("AAA")))
          validate(json) shouldBe false
        }

        "contains a number" in {

          val json = Json.toJson(UltimateParent(countryOfIncorporation = Some("A1")))
          validate(json) shouldBe false
        }

        "contains a symbol" in {

          val json = Json.toJson(UltimateParent(countryOfIncorporation = Some("A@")))
          validate(json) shouldBe false
        }
      }

      "Non Uk crn" when {

        s"is empty" in {

          val json = Json.toJson(UltimateParent(nonUkCrn = Some((""))))

          validate(json) shouldBe false
        }
      }
    }
  }
}
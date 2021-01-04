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

package v1.schemas.subSchemas

import play.api.libs.json.{JsValue, Json}
import v1.models.UTRModel
import v1.schemas.BaseSchemaSpec
import v1.schemas.helpers._

class DeemedCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/deemedParent.json", "1.0", json)

  val ukDeemedParent = DeemedParent(sautr = None, countryOfIncorporation = None)
  val ukPartnershipDeemedParent = DeemedParent(ctutr = None, countryOfIncorporation = None)
  val nonUkDeemedParent = DeemedParent(ctutr = None, sautr = None)

  "DeemedCompany Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload with min two deemed parents" in {

        val json = Json.toJson(Seq(ukDeemedParent, ukDeemedParent))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with three deemed parents" in {

        val json = Json.toJson(Seq(ukDeemedParent, ukPartnershipDeemedParent, nonUkDeemedParent))

        validate(json) shouldBe true
      }
    }

    "Return invalid" when {

      "company name" when {

        "is None" in {

          val json = Json.toJson(Seq(ukDeemedParent.copy(companyName = None), nonUkDeemedParent.copy(companyName = None)
          ))

          validate(json) shouldBe false
        }

        s"is empty" in {

          val json = Json.toJson(Seq(ukPartnershipDeemedParent.copy(companyName = Some("")), nonUkDeemedParent.copy(companyName = Some(""))
          ))

          validate(json) shouldBe false
        }

        s"is longer than $maxCompanyNameLength characters" in {

          val json = Json.toJson(Seq(nonUkDeemedParent.copy(companyName = Some("A" * (maxCompanyNameLength + 1))),
                                     nonUkDeemedParent.copy(companyName = Some("A" * (maxCompanyNameLength + 1)))
          ))

          validate(json) shouldBe false
        }
      }

      "ctutr" when {

        s"below $utrLength" in {

          val json = Json.toJson(Seq(ukDeemedParent.copy(ctutr = Some(UTRModel("1" * (utrLength - 1)))),
                                     ukDeemedParent.copy(ctutr = Some(UTRModel("1" * (utrLength - 1))))
          ))

          validate(json) shouldBe false
        }

        s"above $utrLength" in {

          val json = Json.toJson(Seq(ukDeemedParent.copy(ctutr = Some(UTRModel("1" * (utrLength + 1)))),
                                     ukDeemedParent.copy(ctutr = Some(UTRModel("1" * (utrLength + 1))))
          ))

          validate(json) shouldBe false
        }

        "is non numeric" in {

          val json = Json.toJson(Seq(ukDeemedParent.copy(ctutr = Some(UTRModel("a" * utrLength))),
                                     ukDeemedParent.copy(ctutr = Some(UTRModel("a" * utrLength)))
          ))

          validate(json) shouldBe false
        }

        "is a symbol" in {

          val json = Json.toJson(Seq(ukDeemedParent.copy(ctutr = Some(UTRModel("@"))),
                                     ukDeemedParent.copy(ctutr = Some(UTRModel("@")))
          ))

          validate(json) shouldBe false
        }
      }

      "countryOfIncorporation" when {

        "is only one letter" in {

          val json = Json.toJson(Seq(ukDeemedParent.copy(countryOfIncorporation = Some("A")),
                                     ukDeemedParent.copy(countryOfIncorporation = Some("A"))
          ))

          validate(json) shouldBe false
        }

        "is three letters" in {

          val json = Json.toJson(Seq(ukDeemedParent.copy(countryOfIncorporation = Some("AAA")),
                                     ukDeemedParent.copy(countryOfIncorporation = Some("AAA"))
          ))

          validate(json) shouldBe false
        }

        "contains a number" in {

          val json = Json.toJson(Seq(ukDeemedParent.copy(countryOfIncorporation = Some("A1")),
                                     ukDeemedParent.copy(countryOfIncorporation = Some("A1"))
          ))

          validate(json) shouldBe false
        }

        "contains a symbol" in {

          val json = Json.toJson(Seq(ukDeemedParent.copy(countryOfIncorporation = Some("A@")),
                                     ukDeemedParent.copy(countryOfIncorporation = Some("A@"))
          ))

          validate(json) shouldBe false
        }
      }

      "Validated a unsuccessful JSON payload with only 1 deemed parent" in {

        val json = Json.toJson(Seq(ukDeemedParent))

        validate(json) shouldBe false
      }

      "Validated a unsuccessful JSON payload with more than 3 deemed parent" in {

        val json = Json.toJson(Seq(ukDeemedParent, ukDeemedParent, ukDeemedParent, ukDeemedParent))

        validate(json) shouldBe false
      }

    }
  }
}
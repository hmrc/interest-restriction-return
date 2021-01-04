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
import v1.schemas.BaseSchemaSpec
import v1.schemas.helpers._

class ParentCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/parentCompany.json", "1.0", json)

  val ukDeemedParent = DeemedParent(sautr = None, countryOfIncorporation = None)
  val ukPartnershipDeemedParent = DeemedParent(ctutr = None, countryOfIncorporation = None)
  val nonUkDeemedParent = DeemedParent(ctutr = None, sautr = None)

  "ParentCompany Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload with a Uk Ultimate Parent company" in {

        val json = Json.toJson(ParentCompany())

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with minimum of 2 Uk Deemed Parent company" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq(ukDeemedParent, nonUkDeemedParent))
        ))

        validate(json) shouldBe true
      }


      "Validated a successful JSON payload with Non Uk Deemed Parent company" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq(nonUkDeemedParent, nonUkDeemedParent))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with 3 Uk Deemed Parent companies" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq(ukDeemedParent, ukPartnershipDeemedParent, nonUkDeemedParent))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON with 3 Non-Uk Deemed Parent Company" in {

        val json = Json.toJson(ParentCompany(ultimateParent = None, Some(Seq(nonUkDeemedParent, nonUkDeemedParent, nonUkDeemedParent))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON with 3 Uk Deemed Parent Partnerships" in {

        val json = Json.toJson(ParentCompany(ultimateParent = None, Some(Seq(ukPartnershipDeemedParent, ukPartnershipDeemedParent, ukPartnershipDeemedParent))
        ))

        validate(json) shouldBe true
      }
    }

    "Return invalid" when {

      "Deemed Parents and Ultimate Parent are None" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = None
        ))

        validate(json) shouldBe false
      }

      "Deemed parents is an empty list and Ultimate Parent are None" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq.empty))
        )

        validate(json) shouldBe false
      }

      "more than 3 Deemed Parents and Ultimate Parent are None" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None,
          Some(Seq(ukDeemedParent, ukDeemedParent, ukDeemedParent, ukDeemedParent)
          )))

        validate(json) shouldBe false
      }

      "less than 2 Deemed Parents and Ultimate Parent is None" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None,
          Some(Seq(ukDeemedParent)
          )))

        validate(json) shouldBe false
      }
    }
  }
}
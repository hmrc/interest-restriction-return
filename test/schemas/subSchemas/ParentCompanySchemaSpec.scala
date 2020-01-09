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

class ParentCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/parentCompany.json", json)

  "ParentCompany Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload with a Uk Ultimate Parent company" in {

        val json = Json.toJson(ParentCompany())

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with NonUK Ultimate Parent company" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = Some(UltimateParent(isUk = Some(false)))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with Uk Deemed Parent company" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq(DeemedParent()))
        ))

        validate(json) shouldBe true
      }


      "Validated a successful JSON payload with Non Uk Deemed Parent company" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq(DeemedParent(isUk = Some(false))))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with 3 Uk Deemed Parent companies" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq(
            DeemedParent(isUk = Some(true)), DeemedParent(isUk = Some(true)), DeemedParent(isUk = Some(true))))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with a mix of Uk and Non Uk Deemed Parent companies" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq(
            DeemedParent(isUk = Some(false)), DeemedParent(isUk = Some(true)), DeemedParent(isUk = Some(false))))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON with a Deemed Parent company with no ctutr" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, Some(Seq(
            DeemedParent(ctutr = None)))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON with a Deemed Parent company with no sautr" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, Some(Seq(DeemedParent(sautr = None)))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload when Deemed Parent knownAs is none" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None,
          deemedParent = Some(Seq(DeemedParent(knownAs = None)))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload when Ultimate Parent ctutr is none" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = Some(UltimateParent(ctutr = None))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload when Ultimate Parent sautr is none" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = Some(UltimateParent(sautr = None))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload when Ultimate Parent knownAs is none" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = Some(UltimateParent(knownAs = None))
        ))

        validate(json) shouldBe true
      }

    }

    "Return invalid" when {

      "deemed parents and ultimate parent is None" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = None
        ))

        validate(json) shouldBe false
      }

      "deemed parents is an empty list and ultimate parent is None" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq.empty))
        )

        validate(json) shouldBe false
      }

      "more than 3 deemed parents and ultimate parent is None" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None,
          Some(Seq(DeemedParent(), DeemedParent(), DeemedParent(), DeemedParent())))
        )

        validate(json) shouldBe false
      }
    }
  }
}
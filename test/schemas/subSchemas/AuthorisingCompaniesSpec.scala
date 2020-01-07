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

import models.UTRModel
import play.api.libs.json.{JsValue, Json}
import schemas.BaseSchemaSpec
import schemas.helpers._

class AuthorisingCompaniesSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("subSchemas/authorisingCompanies.json", json)

  "authorisingCompanies" when {

    "return valid" when {

      "valid JSON supplied" in {
        val json = Json.toJson(Seq(AuthorisingCompanies()))

        validate(json) shouldBe true
      }

      "consent is not supplied" in {
        val json = Json.toJson(Seq(AuthorisingCompanies(
          consenting = None
        )))
        validate(json) shouldBe true
      }
    }

    "return invalid" when {

      "companyName" when {

        "exceeds 160" in {

          val json = Json.toJson(Seq(AuthorisingCompanies(
            companyName = Some("A" * (maxCompanyNameLength + 1))
          )))

          validate(json) shouldBe false
        }

        "is empty" in {

          val json = Json.toJson(Seq(AuthorisingCompanies(
            companyName = Some("")
          )))

          validate(json) shouldBe false
        }

        "is not applied" in {

          val json = Json.toJson(Seq(AuthorisingCompanies(
            companyName = None
          )))

          validate(json) shouldBe false
        }
      }

      "utr" when {

        s"below $utrLength" in {

          val json = Json.toJson(Seq(AuthorisingCompanies(
            utr = Some(UTRModel("1" * (utrLength - 1)))
          )))

          validate(json) shouldBe false
        }

        s"above $utrLength" in {

          val json = Json.toJson(Seq(AuthorisingCompanies(
            utr = Some(UTRModel("1" * (utrLength + 1)))
          )))

          validate(json) shouldBe false
        }

        "is non numeric" in {

          val json = Json.toJson(Seq(AuthorisingCompanies(
            utr = Some(UTRModel("a" * (utrLength)))
          )))

          validate(json) shouldBe false
        }

        "is a symbol" in {

          val json = Json.toJson(Seq(AuthorisingCompanies(
            utr = Some(UTRModel("@"))
          )))

          validate(json) shouldBe false
        }

        "is not applied" in {

          val json = Json.toJson(Seq(AuthorisingCompanies(
            utr = None
          )))

          validate(json) shouldBe false
        }
      }
      "consenting" when {

      }
    }
  }

}

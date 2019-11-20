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

package schemas

import helpers._
import play.api.libs.json.{JsValue, Json}

class FullSchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("fullReturnSchema.json", json)

  "FullReturn Json Schema" should {

    "Return valid" when {

//      TODO: Add Valid Tests for Full Return
//      "Validated a successful JSON payload with UK Parent company" in {
//
//        val json = Json.toJson(FullReturnModel())
//
//        validate(json) shouldBe true
//      }
    }

    "Return invalid" when {

//      TODO: Add Invalid Tests for Full Return

    }
  }
}

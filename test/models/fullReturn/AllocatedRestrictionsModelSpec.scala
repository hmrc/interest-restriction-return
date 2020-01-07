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

package models.fullReturn

import assets.fullReturn.AllocatedRestrictionsConstants._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class AllocatedRestrictionsModelSpec extends WordSpec with Matchers {

  "AllocatedRestrictionsModel" must {

    "correctly write to json" in {

      val expectedValue = allocatedRestrictionsJson
      val actualValue = Json.toJson(allocatedRestrictionsModel)

      actualValue shouldBe expectedValue
    }

    "correctly read from Json" in {

      val expectedValue = allocatedRestrictionsModel
      val actualValue = allocatedRestrictionsJson.as[AllocatedRestrictionsModel]

      actualValue shouldBe expectedValue
    }
  }
}
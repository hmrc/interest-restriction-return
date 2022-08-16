/*
 * Copyright 2022 HM Revenue & Customs
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

import assets.fullReturn.AllocatedReactivationsConstants._
import play.api.libs.json.Json
import v1.models.fullReturn.AllocatedReactivationsModel
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class AllocatedReactivationsModelSpec extends AnyWordSpec with Matchers {

  "AllocatedReactivationsModel" must {

    "correctly write to json" in {

      val expectedValue = allocatedReactivationsJson
      val actualValue   = Json.toJson(allocatedReactivationsModel)

      actualValue shouldBe expectedValue
    }

    "correctly read from Json" in {

      val expectedValue = allocatedReactivationsModel
      val actualValue   = allocatedReactivationsJson.as[AllocatedReactivationsModel]

      actualValue shouldBe expectedValue
    }
  }
}

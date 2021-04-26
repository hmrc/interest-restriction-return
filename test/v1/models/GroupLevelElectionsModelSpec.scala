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

package v1.models

import assets.GroupLevelElectionsConstants._
import com.typesafe.config.ConfigFactory
import config.FeatureSwitch
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json
import play.api.Configuration
import utils.TestJsonFormatter._

class GroupLevelElectionsModelSpec extends WordSpec with Matchers {

  "GroupLevelElectionsModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = groupLevelElectionsJsonMax
        val actualValue = Json.toJson(groupLevelElectionsModelMax)(cr008EnabledJsonFormatter.groupLevelElectionWrites)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = groupLevelElectionsModelMax
        val actualValue = groupLevelElectionsJsonMax.as[GroupLevelElectionsModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}

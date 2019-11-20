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

package models.abbreviatedReturn

import assets.ParentCompanyConstants._
import models.ParentCompanyModel
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class ParentCompanyModelSpec extends WordSpec with Matchers {

  "ParentCompanyModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = parentCompanyJsonMax
        val actualValue = Json.toJson(parentCompanyModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = Json.obj()
        val actualValue = Json.toJson(parentCompanyModelMin)

        actualValue shouldBe expectedValue
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = parentCompanyModelMax
        val actualValue = parentCompanyJsonMax.as[ParentCompanyModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = parentCompanyModelMin
        val actualValue = Json.obj().as[ParentCompanyModel]

        actualValue shouldBe expectedValue
      }
    }
  }
}

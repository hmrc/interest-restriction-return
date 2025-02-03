/*
 * Copyright 2025 HM Revenue & Customs
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

package v1.validation

import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.UTRModel

class UTRValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "UTRValidator" when {

    "UTR is supplied and fails Check Sum" in {
      val model = UTRModel("1234567890")
      leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(model).errorMessage
    }

    "UTR is supplied and passes Check Sum" in {
      val model = UTRModel("9534668155")
      rightSide(model.validate) shouldBe model
    }
  }
}

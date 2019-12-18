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

package validation

import models.CRNModel
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.JsPath

class CRNValidatorSpec extends WordSpec with Matchers{

  implicit val path = JsPath \ "some" \ "path"

  "CRN Validation" when {

    "CRN is supplied and is made up of 8 numbers" in {
      val model  = CRNModel("12345678")
      model.validate.toEither.right.get shouldBe model
    }

    "CRN is supplied and contains 2 letters and 6 numbers" in {
      val model  = CRNModel("AZ123456")
      model.validate.toEither.right.get shouldBe model
    }
  }
}

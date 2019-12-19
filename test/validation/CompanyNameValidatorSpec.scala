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

import models.CompanyNameModel
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.JsPath

class CompanyNameValidatorSpec extends WordSpec with Matchers {

  implicit val path = JsPath \ "some" \ "path"

  "Company Name Validation" when {

    "invalid" when {

      "company name is below the minimum length" in {

        val model = CompanyNameModel(
          name = ""
        )
        model.validate.toEither.left.get.head.errorMessage shouldBe CompanyNameLengthError(model.name).errorMessage
      }

      "company name is above the maximum length" in {

        val model = CompanyNameModel(
          name = "a" * 161
        )
        model.validate.toEither.left.get.head.errorMessage shouldBe CompanyNameLengthError(model.name).errorMessage
      }
    }
  }
}
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

package v1.validation

import assets.PartnershipsConstants._
import play.api.libs.json.JsPath
import v1.models.{CompanyNameModel, UTRModel}

class PartnershipValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Partnership" when {

    "Return valid" when {

      "isElected is true and no partnership names are given" in {
        val model = partnershipModel.copy(CompanyNameModel(partnerName))
        model.validate.toEither.right.get shouldBe model
      }
    }

    "Return invalid" when {

      "Partnership Name" when {
        "is greater than 160" in {
          val model = partnershipModel.copy(partnershipName = CompanyNameModel("a" * (32767 + 1)))

          model.validate.toEither.left.get.head.errorMessage shouldBe CompanyNameLengthError("a" * (32767 + 1)).errorMessage
        }

        "isElected is true and partnership names are given" in {
          val model = partnershipModel.copy(partnershipName = CompanyNameModel(""))
          model.validate.toEither.left.get.head.errorMessage shouldBe CompanyNameLengthError("").errorMessage
        }

      }

      "sautr" when {
        "is populated and invalid" in {
          val utr = UTRModel("11234567890")
          val model = partnershipModel.copy(sautr = Some(utr))
          model.validate.toEither.left.get.head.errorMessage shouldBe UTRLengthError(utr).errorMessage
        }
      }
    }
  }
}

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

import models.{ConsolidatedPartnershipModel, PartnershipModel}
import play.api.libs.json.JsPath

class ConsolidatedPartnershipValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Consolidated Partnership" when {
    "Return valid" when {

      "isElected is true and a Seq of partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = true, consolidatedPartnerships = Some(Seq(PartnershipModel("Partner 1"))))
        rightSide(model.validate) shouldBe model
      }

      "isElected is false and no partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = false, consolidatedPartnerships = None)
        rightSide(model.validate) shouldBe model
      }
    }

    "Return invalid" when {

      "isElected is true and no partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = true, consolidatedPartnerships = None)
        leftSideError(model.validate).errorMessage shouldBe ConsolidatedPartnershipsNotSupplied(model).errorMessage
      }

      "isElected is false and some partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = false, consolidatedPartnerships = Some(Seq(PartnershipModel("Partner 1"))))
        leftSideError(model.validate).errorMessage shouldBe ConsolidatedPartnershipsSupplied(model).errorMessage
      }

      "consolidatedPartnerships is invalid" in {
        val model = ConsolidatedPartnershipModel(isElected = true, consolidatedPartnerships = Some(Seq(PartnershipModel(""))))
        leftSideError(model.validate).errorMessage shouldBe PartnershipNameError("").errorMessage
      }
    }
  }
}

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

import play.api.libs.json.JsPath
import v1.models.{ConsolidatedPartnershipModel, PartnershipModel, UTRModel}

class ConsolidatedPartnershipValidatorSpec extends BaseValidationSpec {

  val sautrFake = UTRModel("1234567890")

  implicit val path = JsPath \ "some" \ "path"

  "Consolidated Partnership" when {
    "Return valid" when {

      "isElected is true and a Seq of partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = true, consolidatedPartnerships = Some(Seq(PartnershipModel(partnershipName = "Partner 1",sautr = Some(sautrFake)))))
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
        leftSideError(model.validate).message shouldBe ConsolidatedPartnershipsNotSupplied(model).message
      }

      "isElected is false and some partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = false, consolidatedPartnerships = Some(Seq(PartnershipModel(partnershipName = "Partner 1", sautr = Some(sautrFake)))))
        leftSideError(model.validate).message shouldBe ConsolidatedPartnershipsSupplied(model).message
      }

      "consolidatedPartnerships is invalid" in {
        val model = ConsolidatedPartnershipModel(isElected = true, consolidatedPartnerships = Some(Seq(PartnershipModel(partnershipName = "", sautr = Some(sautrFake)))))
        leftSideError(model.validate).message shouldBe PartnershipNameError("").message
      }
    }
  }
}

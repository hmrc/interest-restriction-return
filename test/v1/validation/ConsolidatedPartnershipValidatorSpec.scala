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

package v1.validation

import play.api.libs.json.JsPath
import v1.models.{ConsolidatedPartnershipModel, PartnershipModel, UTRModel, CompanyNameModel}

class ConsolidatedPartnershipValidatorSpec extends BaseValidationSpec {

  val sautrFake = UTRModel("1123456789")

  implicit val path = JsPath \ "some" \ "path"

  "Consolidated Partnership" when {
    "Return valid" when {

      "isElected is true and a Seq of partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = true, isActive = true,
          consolidatedPartnerships = Some(Seq(PartnershipModel(partnershipName = CompanyNameModel("Partner 1"), sautr = Some(sautrFake)))))
        rightSide(model.validate) shouldBe model
      }

      "isElected is false and some partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = false, isActive = true,
          consolidatedPartnerships = Some(Seq(PartnershipModel(partnershipName = CompanyNameModel("Partner 1"), sautr = Some(sautrFake)))))
        rightSide(model.validate) shouldBe model
      }

      "isActive is false and some partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = true, isActive = false,
          consolidatedPartnerships = Some(Seq(PartnershipModel(partnershipName = CompanyNameModel("Partner 1"), sautr = Some(sautrFake)))))
        rightSide(model.validate) shouldBe model
      }
    }

    "Return invalid" when {

      "IsActive & IsElected are true and no partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = true, isActive = true, consolidatedPartnerships = None)
        leftSideError(model.validate).errorMessage shouldBe ConsolidatedPartnershipsNotSupplied(model).errorMessage
      }

      "isElected is false and no partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = false, isActive = true, consolidatedPartnerships = None)
        leftSideError(model.validate).errorMessage shouldBe ConsolidatedPartnershipsNotSupplied(model).errorMessage
      }

      "IsActive is false and no partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = true, isActive = false, consolidatedPartnerships = None)
        leftSideError(model.validate).errorMessage shouldBe ConsolidatedPartnershipsNotSupplied(model).errorMessage
      }

      "isElected is false & isActive is false and some partnerships are given" in {
        val model = ConsolidatedPartnershipModel(isElected = false, isActive = false,
          consolidatedPartnerships = Some(Seq(PartnershipModel(partnershipName = CompanyNameModel(""), sautr = Some(sautrFake)))))
        leftSideError(model.validate).errorMessage shouldBe ConsolidatedPartnershipsSupplied(model).errorMessage
      }

      "consolidatedPartnerships is invalid due to no name" in {
        val model = ConsolidatedPartnershipModel(isElected = true, isActive = true,
          consolidatedPartnerships = Some(Seq(PartnershipModel(partnershipName = CompanyNameModel(""), sautr = Some(sautrFake)))))
        leftSideError(model.validate).errorMessage shouldBe CompanyNameLengthError("").errorMessage
      }

      "consolidatedPartnerships is invalid due to invalid characters" in {
        val model = ConsolidatedPartnershipModel(isElected = true, isActive = true,
          consolidatedPartnerships = Some(Seq(PartnershipModel(partnershipName = CompanyNameModel("ʰʲʺ£$%˦˫qw"), sautr = Some(sautrFake)))))
        leftSideError(model.validate).errorMessage shouldBe CompanyNameCharactersError("ʰʲʺ£$%˦˫qw").errorMessage
      }

      "consolidatedPartnerships is empty" in {
        val model = ConsolidatedPartnershipModel(isElected = true, isActive = true, consolidatedPartnerships = Some(Nil))
        leftSideError(model.validate).errorMessage shouldBe ConsolidatedPartnershipsEmpty().errorMessage
      }

      "SAUTR contains invalid characters" in {

        val model = ConsolidatedPartnershipModel(isElected = true, isActive = true,
          consolidatedPartnerships = Some(Seq(PartnershipModel(partnershipName = CompanyNameModel("Partner 1"), sautr = Some(UTRModel("ʰʲʺ£$%˦˫qw"))))))

        leftSideError(model.validate).errorMessage shouldBe UTRChecksumError(UTRModel("ʰʲʺ£$%˦˫qw")).errorMessage
      }

      "SAUTR is to short" in {

        val model = ConsolidatedPartnershipModel(isElected = true, isActive = true,
          consolidatedPartnerships = Some(Seq(PartnershipModel(partnershipName = CompanyNameModel("Partner 1"), sautr = Some(UTRModel("1"))))))

        leftSideError(model.validate).errorMessage shouldBe UTRLengthError(invalidShortUtr).errorMessage
      }

      "SAUTR is to long" in {

        val model = ConsolidatedPartnershipModel(isElected = true, isActive = true,
          consolidatedPartnerships = Some(Seq(PartnershipModel(partnershipName = CompanyNameModel("Partner 1"), sautr = Some(UTRModel("11234567890"))))))

        leftSideError(model.validate).errorMessage shouldBe UTRLengthError(invalidLongUtr).errorMessage
      }
    }
  }
}

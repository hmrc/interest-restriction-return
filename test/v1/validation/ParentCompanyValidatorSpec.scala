/*
 * Copyright 2023 HM Revenue & Customs
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

import assets.DeemedParentConstants.deemedParentModelUkCompany
import assets.ParentCompanyConstants._
import play.api.libs.json.JsPath

class ParentCompanyValidatorSpec extends BaseValidationSpec {

  implicit val path = JsPath \ "some" \ "path"

  "Parent Company Validation" when {

    "Ultimate and Deemed fields are populated" should {

      "Return invalid" in {

        val model = parentCompanyModelMax

        leftSideError(model.validate).errorMessage shouldBe ParentCompanyCanNotBeUltimateAndDeemed(model).errorMessage
      }
    }

    "Ultimate and Deemed field are both not populated" should {
      "Return invalid" in {
        val model = parentCompanyModelNone
        leftSideError(model.validate).errorMessage shouldBe ParentCompanyBothUltimateAndDeemedEmtpty(model).errorMessage
      }
    }

    "Ultimate fields are populated" should {

      "Return valid" in {
        val model = parentCompanyModelUltMax

        rightSide(model.validate) shouldBe model
      }
    }

    "Deemed fields are populated" should {

      "Return valid" in {
        val model = parentCompanyModelDeemedMin

        rightSide(model.validate) shouldBe model
      }
    }

    "Deemed fields are populated" should {

      "Return invalid if more than 3 deemed Parents" in {

        val model = parentCompanyModelDeemedMin.copy(deemedParent =
          Some(
            Seq(
              deemedParentModelUkCompany,
              deemedParentModelUkCompany,
              deemedParentModelUkCompany,
              deemedParentModelUkCompany
            )
          )
        )

        leftSideError(model.parentCompanyModel.validate).errorMessage shouldBe MaxThreeDeemedParents(model).errorMessage
      }

      "Return invalid if deemed Parents empty" in {

        val model = parentCompanyModelDeemedMin.copy(deemedParent = Some(Nil))
        leftSideError(model.parentCompanyModel.validate).errorMessage shouldBe DeemedParentsEmpty().errorMessage
      }

      "Return invalid if less than 2 deemed Parents" in {

        val model = parentCompanyModelDeemedMin.copy(deemedParent = Some(Seq(deemedParentModelUkCompany)))
        leftSideError(model.parentCompanyModel.validate).errorMessage shouldBe MinTwoDeemedParents(model).errorMessage
      }
    }
  }
}

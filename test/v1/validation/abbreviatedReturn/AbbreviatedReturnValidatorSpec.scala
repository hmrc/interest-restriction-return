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

package v1.validation.abbreviatedReturn

import assets.AgentDetailsConstants._
import assets.GroupCompanyDetailsConstants._
import assets.GroupLevelElectionsConstants._
import assets.GroupRatioConstants._
import assets.ParentCompanyConstants._
import assets.ReportingCompanyConstants._
import assets.abbreviatedReturn.AbbreviatedReturnConstants._
import assets.abbreviatedReturn.UkCompanyConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.{Original, Revised}
import v1.validation._

class AbbreviatedReturnValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "AbbreviatedReturnValidator" should {

    "Return valid" when {

      "a valid Abbreviated Return is supplied" in {
        rightSide(abbreviatedReturnUltimateParentModel.validate) shouldBe abbreviatedReturnUltimateParentModel
      }
    }

    "Return invalid" when {

      "Return type is Original and some details for a revision are supplied" in {

        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          submissionType = Original,
          revisedReturnDetails = Some("Revision")
        ).validate).errorMessage shouldBe RevisedReturnDetailsSupplied("Revision").errorMessage
      }

      "Return type is Revised and no details for a revision are supplied" in {

        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          submissionType = Revised,
          revisedReturnDetails = None
        ).validate).errorMessage shouldBe RevisedReturnDetailsNotSupplied.errorMessage
      }

      "Reporting Company is the same as UPC but Parent Details are supplied" in {

        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = true),
          parentCompany = Some(parentCompanyModelUltUkCompany)
        ).validate).errorMessage shouldBe ParentCompanyDetailsSupplied(parentCompanyModelUltUkCompany).errorMessage
      }

      "Reporting Company is not the same as UPC and UPC is not supplied" in {

        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          reportingCompany = reportingCompanyModel,
          parentCompany = None
        ).validate).errorMessage shouldBe ParentCompanyDetailsNotSupplied.errorMessage
      }

      "Agent details are invalid" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(agentDetails = agentDetailsModelMax.copy(agentName = None)).validate).errorMessage shouldBe
          AgentNameNotSuppliedError().errorMessage
      }

      "Reporting Company details are invalid" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          reportingCompany = reportingCompanyModel.copy(
            companyName = companyNameTooLong)).validate).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }

      "Parent Company is invalid" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(parentCompany = Some(parentCompanyModelMax)).validate).errorMessage shouldBe
          ParentCompanyCanNotBeUltimateAndDeemed(parentCompanyModelMax).errorMessage
      }

      "Group Company Details are invalid" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(groupCompanyDetails = groupCompanyDetailsModel.copy(totalCompanies = 0)).validate).errorMessage shouldBe
          GroupCompanyDetailsTotalCompaniesError(0).errorMessage
      }

      "Group Level Elections are invalid" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          groupLevelElections = Some(groupLevelElectionsModelMax.copy(
            groupRatio = groupRatioModelMin.copy(groupEBITDAChargeableGains = Some(true))
          )
        )).validate).errorMessage shouldBe GroupEBITDASupplied(Some(true)).errorMessage
      }

      "Uk Company details are invalid" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          ukCompanies = Seq(ukCompanyModel.copy(companyName = companyNameTooLong))
        ).validate).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }
    }
  }
}

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
        ).validate).message shouldBe RevisedReturnDetailsSupplied("Revision").message
      }

      "Return type is Revised and no details for a revision are supplied" in {

        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          submissionType = Revised,
          revisedReturnDetails = None
        ).validate).message shouldBe RevisedReturnDetailsNotSupplied.message
      }

      "Reporting Company is the same as UPC but Parent Details are supplied" in {

        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = true),
          parentCompany = Some(parentCompanyModelUltUkCompany)
        ).validate).message shouldBe ParentCompanyDetailsSupplied(parentCompanyModelUltUkCompany).message
      }

      "Reporting Company is not the same as UPC and UPC is not supplied" in {

        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          reportingCompany = reportingCompanyModel,
          parentCompany = None
        ).validate).message shouldBe ParentCompanyDetailsNotSupplied.message
      }

      "Agent details are invalid" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(agentDetails = agentDetailsModelMax.copy(agentName = None)).validate).message shouldBe
          AgentNameNotSuppliedError().message
      }

      "Reporting Company details are invalid" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          reportingCompany = reportingCompanyModel.copy(
            companyName = companyNameTooLong)).validate).message shouldBe CompanyNameLengthError(companyNameTooLong.name).message
      }

      "Parent Company is invalid" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(parentCompany = Some(parentCompanyModelMax)).validate).message shouldBe
          ParentCompanyCanNotBeUltimateAndDeemed(parentCompanyModelMax).message
      }

      "Group Company Details are invalid" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(groupCompanyDetails = groupCompanyDetailsModel.copy(totalCompanies = 0)).validate).message shouldBe
          GroupCompanyDetailsTotalCompaniesError(0).message
      }

      "it is not the appointed reporting company" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          appointedReportingCompany = false
        ).validate).message shouldBe ReportingCompanyNotAppointed.message
      }

      "Group Level Elections are invalid" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          groupLevelElections = Some(groupLevelElectionsModelMax.copy(
            groupRatio = groupRatioModelMin.copy(groupEBITDAChargeableGains = Some(true))
          )
        )).validate).message shouldBe GroupEBITDASupplied(Some(true)).message
      }

      "Uk Company details are invalid" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(
          ukCompanies = Seq(ukCompanyModel.copy(companyName = companyNameTooLong))
        ).validate).message shouldBe CompanyNameLengthError(companyNameTooLong.name).message
      }

      "Angie is negative" in {
        leftSideError(abbreviatedReturnUltimateParentModel.copy(angie = Some(-0.01)).validate).message shouldBe NegativeAngieError(-0.01).message
      }
    }
  }
}

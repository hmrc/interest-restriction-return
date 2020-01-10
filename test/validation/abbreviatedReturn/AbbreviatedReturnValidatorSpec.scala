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

package validation.abbreviatedReturn

import assets.AgentDetailsConstants._
import assets.GroupCompanyDetailsConstants._
import assets.GroupLevelElectionsConstants._
import assets.GroupRatioConstants._
import assets.ParentCompanyConstants._
import assets.ReportingCompanyConstants._
import assets.abbreviatedReturn.AbbreviatedReturnConstants._
import assets.abbreviatedReturn.UkCompanyConstants._
import models.{Original, Revised}
import play.api.libs.json.JsPath
import utils.BaseSpec
import validation._

class AbbreviatedReturnValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "AbbreviatedReturnValidator" should {

    "Return valid" when {

      "a valid Abbreviated Return is supplied" in {
        rightSide(abbreviatedReturnModelMax.validate) shouldBe abbreviatedReturnModelMax
      }
    }

    "Return invalid" when {

      "Return type is Original and some details for a revision are supplied" in {

        leftSideError(abbreviatedReturnModelMax.copy(
          submissionType = Original,
          revisedReturnDetails = Some("Revision")
        ).validate).errorMessage shouldBe RevisedReturnDetailsSupplied("Revision").errorMessage
      }

      "Return type is Revised and no details for a revision are supplied" in {

        leftSideError(abbreviatedReturnModelMax.copy(
          submissionType = Revised,
          revisedReturnDetails = None
        ).validate).errorMessage shouldBe RevisedReturnDetailsNotSupplied.errorMessage
      }

      "Reporting Company is the same as UPC but Parent Details are supplied" in {

        leftSideError(abbreviatedReturnModelMax.copy(
          reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = true),
          parentCompany = Some(parentCompanyModelUltUkCompany)
        ).validate).errorMessage shouldBe ParentCompanyDetailsSupplied(parentCompanyModelUltUkCompany).errorMessage
      }

      "Reporting Company is not the same as UPC and UPC is not supplied" in {

        leftSideError(abbreviatedReturnModelMax.copy(
          reportingCompany = reportingCompanyModel,
          parentCompany = None
        ).validate).errorMessage shouldBe ParentCompanyDetailsNotSupplied.errorMessage
      }

      "Agent details are invalid" in {
        leftSideError(abbreviatedReturnModelMax.copy(agentDetails = agentDetailsModelMax.copy(agentName = None)).validate).errorMessage shouldBe
          AgentNameNotSuppliedError().errorMessage
      }

      "Reporting Company details are invalid" in {
        leftSideError(abbreviatedReturnModelMax.copy(
          reportingCompany = reportingCompanyModel.copy(
            companyName = companyNameTooLong)).validate).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }

      "Parent Company is invalid" in {
        leftSideError(abbreviatedReturnModelMax.copy(parentCompany = Some(parentCompanyModelMax)).validate).errorMessage shouldBe
          ParentCompanyCanNotBeUltimateAndDeemed(parentCompanyModelMax).errorMessage
      }

      "Group Company Details are invalid" in {
        leftSideError(abbreviatedReturnModelMax.copy(groupCompanyDetails = groupCompanyDetailsModel.copy(totalCompanies = 0)).validate).errorMessage shouldBe
          GroupCompanyDetailsTotalCompaniesError(0).errorMessage
      }

      "Group Level Elections are invalid" in {
        leftSideError(abbreviatedReturnModelMax.copy(
          groupLevelElections = Some(groupLevelElectionsModelMax.copy(
            groupRatio = groupRatioModelMin.copy(groupEBITDAChargeableGains = Some(true))
          )
        )).validate).errorMessage shouldBe GroupEBITDASupplied(Some(true)).errorMessage
      }

      "Uk Company details are invalid" in {
        leftSideError(abbreviatedReturnModelMax.copy(
          ukCompanies = Seq(ukCompanyModel.copy(companyName = companyNameTooLong))
        ).validate).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }
    }
  }
}

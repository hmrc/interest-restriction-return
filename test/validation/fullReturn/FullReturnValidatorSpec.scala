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

package validation.fullReturn

import assets.AgentDetailsConstants._
import assets.GroupCompanyDetailsConstants._
import assets.GroupLevelElectionsConstants._
import assets.GroupRatioConstants._
import assets.ParentCompanyConstants._
import assets.ReportingCompanyConstants._
import assets.fullReturn.FullReturnConstants._
import assets.fullReturn.UkCompanyConstants._
import assets.fullReturn.GroupLevelAmountConstants._
import assets.fullReturn.AllocatedReactivationsConstants._
import assets.fullReturn.AllocatedRestrictionsConstants._
import assets.fullReturn.AdjustedGroupInterestConstants._
import models.{Original, Revised}
import play.api.libs.json.JsPath
import utils.BaseSpec
import validation._

class FullReturnValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "FullReturnValidator" should {

    "Return valid" when {

      "a valid Full Return is supplied" in {
        rightSide(fullReturnModelMax.validate) shouldBe fullReturnModelMax
      }
    }

    "Return invalid" when {

      "Return type is Original and some details for a revision are supplied" in {

        leftSideError(fullReturnModelMax.copy(
          submissionType = Original,
          revisedReturnDetails = Some("Revision")
        ).validate).errorMessage shouldBe RevisedReturnDetailsSupplied("Revision").errorMessage
      }

      "Return type is Revised and no details for a revision are supplied" in {

        leftSideError(fullReturnModelMax.copy(
          submissionType = Revised,
          revisedReturnDetails = None
        ).validate).errorMessage shouldBe RevisedReturnDetailsNotSupplied.errorMessage
      }

      "Reporting Company is the same as UPC but Parent Details are supplied" in {

        leftSideError(fullReturnModelMax.copy(
          reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = true),
          parentCompany = Some(parentCompanyModelUltUkCompany)
        ).validate).errorMessage shouldBe ParentCompanyDetailsSupplied(parentCompanyModelMax).errorMessage
      }

      "Reporting Company is not the same as UPC and UPC is not supplied" in {

        leftSideError(fullReturnModelMax.copy(
          reportingCompany = reportingCompanyModel,
          parentCompany = None
        ).validate).errorMessage shouldBe ParentCompanyDetailsNotSupplied.errorMessage
      }

      "Angie is negative" in {
        leftSideError(fullReturnModelMax.copy(angie = Some(-0.01)).validate).errorMessage shouldBe NegativeAngieError(-0.01).errorMessage
      }

      "Group is subject to interest reactivations and no reactivation cap is supplied" in {
        leftSideError(fullReturnModelMax.copy(
          groupSubjectToInterestReactivation = true,
          groupLevelAmount = groupLevelAmountModel.copy(interestReactivationCap = None)
        ).validate).errorMessage shouldBe InterestReactivationCapNotSupplied.errorMessage
      }

      "Total Reactivations does not match the sum of the total reactivations for each company" in {
        leftSideError(fullReturnModelMax.copy(
          totalReactivation = 10,
          ukCompanies = Seq(ukCompanyModelMax, ukCompanyModelMax)
        ).validate).errorMessage shouldBe TotalReactivationsDoesNotMatch(10, 13.32).errorMessage
      }

      "Group is not subject to interest reactivations but has allocated reactivations supplied" in {

        val model = fullReturnModelMax.copy(
          groupSubjectToInterestReactivation = false,
          ukCompanies = Seq(
            ukCompanyModelMax.copy(
              allocatedReactivations = Some(allocatedReactivationsModel)
            ),
            ukCompanyModelMax.copy(
              allocatedReactivations = None
            ),
            ukCompanyModelMax.copy(
              allocatedReactivations = Some(allocatedReactivationsModel)
            )
          )
        )

        leftSideError(model.validate).errorMessage shouldBe CompaniesContainedAllocatedReactivations(ukCompanyModelMax, 0).errorMessage
        leftSideError(model.validate, 1).errorMessage shouldBe CompaniesContainedAllocatedReactivations(ukCompanyModelMax, 1).errorMessage
      }

      "Group is subject to interest reactivations but has allocated reactivations missing" in {

        val model = fullReturnModelMax.copy(
          groupSubjectToInterestReactivation = true,
          ukCompanies = Seq(
            ukCompanyModelMax.copy(
              allocatedReactivations = Some(allocatedReactivationsModel)
            ),
            ukCompanyModelMax.copy(
              allocatedReactivations = None
            ),
            ukCompanyModelMax.copy(
              allocatedReactivations = Some(allocatedReactivationsModel)
            ),
            ukCompanyModelMax.copy(
              allocatedReactivations = None
            )
          )
        )

        leftSideError(model.validate).errorMessage shouldBe MissingAllocatedReactivationsForCompanies(ukCompanyModelMax, 1).errorMessage
        leftSideError(model.validate, 1).errorMessage shouldBe MissingAllocatedReactivationsForCompanies(ukCompanyModelMax, 3).errorMessage
      }

      "Group is not subject to interest restrictions but has allocated restrictions supplied" in {

        val model = fullReturnModelMax.copy(
          groupSubjectToInterestRestrictions = false,
          ukCompanies = Seq(
            ukCompanyModelMax.copy(
              allocatedRestrictions = Some(allocatedRestrictionsModel)
            ),
            ukCompanyModelMax.copy(
              allocatedRestrictions = None
            ),
            ukCompanyModelMax.copy(
              allocatedRestrictions = Some(allocatedRestrictionsModel)
            )
          )
        )

        leftSideError(model.validate).errorMessage shouldBe CompaniesContainedAllocatedRestrictions(ukCompanyModelMax, 0).errorMessage
        leftSideError(model.validate, 1).errorMessage shouldBe CompaniesContainedAllocatedRestrictions(ukCompanyModelMax, 1).errorMessage
      }

      "Group is subject to interest restrictions but has allocated restrictions missing" in {

        val model = fullReturnModelMax.copy(
          groupSubjectToInterestRestrictions = true,
          ukCompanies = Seq(
            ukCompanyModelMax.copy(
              allocatedRestrictions = Some(allocatedRestrictionsModel)
            ),
            ukCompanyModelMax.copy(
              allocatedRestrictions = None
            ),
            ukCompanyModelMax.copy(
              allocatedRestrictions = Some(allocatedRestrictionsModel)
            ),
            ukCompanyModelMax.copy(
              allocatedRestrictions = None
            )
          )
        )

        leftSideError(model.validate).errorMessage shouldBe MissingAllocatedRestrictionsForCompanies(ukCompanyModelMax, 1).errorMessage
        leftSideError(model.validate, 1).errorMessage shouldBe MissingAllocatedRestrictionsForCompanies(ukCompanyModelMax, 3).errorMessage
      }

      "Agent details are invalid" in {
        leftSideError(fullReturnModelMax.copy(agentDetails = agentDetailsModelMax.copy(agentName = None)).validate).errorMessage shouldBe
          AgentNameNotSuppliedError().errorMessage
      }

      "Reporting Company details are invalid" in {
        leftSideError(fullReturnModelMax.copy(
          reportingCompany = reportingCompanyModel.copy(
            companyName = companyNameTooLong)).validate).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }

      "Parent Company is invalid" in {
        leftSideError(fullReturnModelMax.copy(parentCompany = Some(parentCompanyModelMax)).validate).errorMessage shouldBe
          ParentCompanyCanNotBeUltimateAndDeemed(parentCompanyModelMax).errorMessage
      }

      "Group Company Details are invalid" in {
        leftSideError(fullReturnModelMax.copy(groupCompanyDetails = groupCompanyDetailsModel.copy(totalCompanies = 0)).validate).errorMessage shouldBe
          GroupCompanyDetailsTotalCompaniesError(0).errorMessage
      }

      "Group Level Elections are invalid" in {
        leftSideError(fullReturnModelMax.copy(
          groupLevelElections = groupLevelElectionsModelMax.copy(
            groupRatio = groupRatioModelMin.copy(groupEBITDAChargeableGains = Some(true))
          )).validate).errorMessage shouldBe GroupEBITDASupplied(Some(true)).errorMessage
      }

      "Uk Company details are invalid" in {
        leftSideError(fullReturnModelMax.copy(
          ukCompanies = Seq(ukCompanyModelMax.copy(companyName = companyNameTooLong))
        ).validate).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }

      "Group Level Amount details are invalid" in {
        leftSideError(fullReturnModelMax.copy(
          groupLevelAmount = groupLevelAmountModel.copy(interestAllowanceForPeriod = -1)
        ).validate).errorMessage shouldBe GroupLevelAmountCannotBeNegative("interestAllowanceForPeriod", -1).errorMessage
      }

      "Group Ratio is Elected and Adjusted Group Interest details are invalid" in {
        val adjustedGroupInterestValue = adjustedGroupInterestModel.copy(
          qngie = 100,
          groupEBITDA = 200,
          groupRatio = 60
        )
        leftSideError(fullReturnModelMax.copy(
          adjustedGroupInterest = Some(adjustedGroupInterestValue)
        ).validate).errorMessage shouldBe GroupRatioCalculationError(adjustedGroupInterestValue).errorMessage
      }

      "Group Ratio is Elected and Adjusted Group Interest details are not supplied" in {
        leftSideError(fullReturnModelMax.copy(
          adjustedGroupInterest = None
        ).validate).errorMessage shouldBe AdjustedNetGroupInterestNotSupplied.errorMessage
      }

      "Group Ratio is not Elected and Adjusted Group Interest details are supplied" in {
        leftSideError(fullReturnModelMax.copy(
          groupLevelElections = groupLevelElectionsModelMax.copy(
            groupRatio = groupRatioModelMin
          ),
          adjustedGroupInterest = Some(adjustedGroupInterestModel)
        ).validate).errorMessage shouldBe AdjustedNetGroupInterestSupplied(adjustedGroupInterestModel).errorMessage
      }
    }
  }
}

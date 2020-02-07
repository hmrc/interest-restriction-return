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

package v1.validation.fullReturn

import assets.AgentDetailsConstants._
import assets.GroupCompanyDetailsConstants._
import assets.GroupLevelElectionsConstants._
import assets.GroupRatioConstants._
import assets.ParentCompanyConstants._
import assets.ReportingCompanyConstants._
import assets.fullReturn.AdjustedGroupInterestConstants._
import assets.fullReturn.AllocatedReactivationsConstants._
import assets.fullReturn.AllocatedRestrictionsConstants._
import assets.fullReturn.FullReturnConstants._
import assets.fullReturn.GroupLevelAmountConstants._
import assets.fullReturn.UkCompanyConstants._
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.{Original, Revised}
import v1.validation._

class FullReturnValidatorSpec extends BaseSpec {

  implicit val path = JsPath \ "some" \ "path"

  "FullReturnValidator" should {

    "Return valid" when {

      "a valid Full Return is supplied" in {
        rightSide(fullReturnUltimateParentModel.validate) shouldBe fullReturnUltimateParentModel
      }
    }

    "Return invalid" when {

      "Return type is Original and some details for a revision are supplied" in {

        leftSideError(fullReturnUltimateParentModel.copy(
          submissionType = Original,
          revisedReturnDetails = Some("Revision")
        ).validate).errorMessage shouldBe RevisedReturnDetailsSupplied("Revision").errorMessage
      }

      "Return type is Revised and no details for a revision are supplied" in {

        leftSideError(fullReturnUltimateParentModel.copy(
          submissionType = Revised,
          revisedReturnDetails = None
        ).validate).errorMessage shouldBe RevisedReturnDetailsNotSupplied.errorMessage
      }

      "Reporting Company is the same as UPC but Parent Details are supplied" in {

        leftSideError(fullReturnUltimateParentModel.copy(
          reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = true),
          parentCompany = Some(parentCompanyModelUltUkCompany)
        ).validate).errorMessage shouldBe ParentCompanyDetailsSupplied(parentCompanyModelMax).errorMessage
      }

      "Reporting Company is not the same as UPC and UPC is not supplied" in {

        leftSideError(fullReturnUltimateParentModel.copy(
          reportingCompany = reportingCompanyModel,
          parentCompany = None
        ).validate).errorMessage shouldBe ParentCompanyDetailsNotSupplied.errorMessage
      }

      "Angie is negative" in {
        leftSideError(fullReturnUltimateParentModel.copy(angie = Some(-0.01)).validate).errorMessage shouldBe NegativeAngieError(-0.01).errorMessage
      }

      "Both group level interest restrictions and group level reactivations are supplied" in {
        leftSideError(fullReturnUltimateParentModel.copy(
          groupSubjectToInterestReactivation = true,
          groupSubjectToInterestRestrictions = true
        ).validate).errorMessage shouldBe GroupLevelInterestRestrictionsAndReactivationSupplied(
          fullReturnUltimateParentModel.groupSubjectToInterestReactivation, fullReturnUltimateParentModel.groupSubjectToInterestRestrictions).errorMessage
      }

      "Group is subject to interest reactivations and no reactivation cap is supplied" in {
        leftSideError(fullReturnUltimateParentModel.copy(
          groupSubjectToInterestReactivation = true,
          groupSubjectToInterestRestrictions = false,
          groupLevelAmount = groupLevelAmountModel.copy(interestReactivationCap = None)
        ).validate).errorMessage shouldBe InterestReactivationCapNotSupplied.errorMessage
      }

      "Total Reactivations does not match the sum of the total reactivations for each company" in {
        leftSideError(fullReturnUltimateParentModel.copy(
          totalReactivation = incorrectTotalReactivation,
          ukCompanies = Seq(ukCompanyModelReactivationMax, ukCompanyModelReactivationMax) //4.44
        ).validate).errorMessage shouldBe TotalReactivationsDoesNotMatch(incorrectTotalReactivation, currentPeriodReactivation + currentPeriodReactivation).errorMessage
      }

      "Total Restrictions does not match the sum of the total restrictions for each company" in {
        leftSideError(fullReturnUltimateParentModel.copy(
          groupSubjectToInterestReactivation = false,
          groupSubjectToInterestRestrictions = true,
          totalRestrictions = incorrectDisallowances,
          totalReactivation = 0,
          ukCompanies = Seq(ukCompanyModelRestrictionMax, ukCompanyModelRestrictionMax) //4.44
        ).validate).errorMessage shouldBe TotalRestrictionsDoesNotMatch(incorrectDisallowances, totalDisallowances + totalDisallowances).errorMessage
      }

      "Group is not subject to interest reactivations but has allocated reactivations supplied" in {

        val model = fullReturnUltimateParentModel.copy(
          groupSubjectToInterestReactivation = false,
          ukCompanies = Seq(
            ukCompanyModelReactivationMax.copy(
              allocatedReactivations = Some(allocatedReactivationsModel)
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedReactivations = None
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedReactivations = Some(allocatedReactivationsModel)
            )
          )
        )

        leftSideError(model.validate).errorMessage shouldBe CompaniesContainedAllocatedReactivations(ukCompanyModelReactivationMax, 0).errorMessage
        leftSideError(model.validate, 1).errorMessage shouldBe CompaniesContainedAllocatedReactivations(ukCompanyModelReactivationMax, 1).errorMessage
      }

      "Group is subject to interest reactivations but has allocated reactivations missing" in {

        val model = fullReturnUltimateParentModel.copy(
          groupSubjectToInterestReactivation = true,
          groupSubjectToInterestRestrictions = false,
          ukCompanies = Seq(
            ukCompanyModelReactivationMax.copy(
              allocatedReactivations = Some(allocatedReactivationsModel)
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedReactivations = None
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedReactivations = Some(allocatedReactivationsModel)
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedReactivations = None
            )
          )
        )

        leftSideError(model.validate).errorMessage shouldBe MissingAllocatedReactivationsForCompanies(ukCompanyModelReactivationMax, 1).errorMessage
        leftSideError(model.validate, 1).errorMessage shouldBe MissingAllocatedReactivationsForCompanies(ukCompanyModelReactivationMax, 3).errorMessage
      }

      "Group is not subject to interest restrictions but has allocated restrictions supplied" in {

        val model = fullReturnUltimateParentModel.copy(
          groupSubjectToInterestRestrictions = false,

          ukCompanies = Seq(
            ukCompanyModelReactivationMax.copy(
              allocatedRestrictions = Some(allocatedRestrictionsModel)
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedRestrictions = None
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedRestrictions = Some(allocatedRestrictionsModel)
            )
          )
        )

        leftSideError(model.validate).errorMessage shouldBe CompaniesContainedAllocatedRestrictions(ukCompanyModelReactivationMax, 0).errorMessage
        leftSideError(model.validate, 1).errorMessage shouldBe CompaniesContainedAllocatedRestrictions(ukCompanyModelReactivationMax, 1).errorMessage
      }

      "Group is subject to interest restrictions but has allocated restrictions missing" in {

        val model = fullReturnUltimateParentModel.copy(
          groupSubjectToInterestRestrictions = true,
          groupSubjectToInterestReactivation = false,
          ukCompanies = Seq(
            ukCompanyModelReactivationMax.copy(
              allocatedRestrictions = Some(allocatedRestrictionsModel)
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedRestrictions = None
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedRestrictions = Some(allocatedRestrictionsModel)
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedRestrictions = None
            )
          )
        )

        leftSideError(model.validate).errorMessage shouldBe MissingAllocatedRestrictionsForCompanies(ukCompanyModelReactivationMax, 1).errorMessage
        leftSideError(model.validate, 1).errorMessage shouldBe MissingAllocatedRestrictionsForCompanies(ukCompanyModelReactivationMax, 3).errorMessage
      }

      "Agent details are invalid" in {
        leftSideError(fullReturnUltimateParentModel.copy(agentDetails = agentDetailsModelMax.copy(agentName = None)).validate).errorMessage shouldBe
          AgentNameNotSuppliedError().errorMessage
      }

      "Reporting Company details are invalid" in {
        leftSideError(fullReturnUltimateParentModel.copy(
          reportingCompany = reportingCompanyModel.copy(
            companyName = companyNameTooLong)).validate).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }

      "Parent Company is invalid" in {
        leftSideError(fullReturnUltimateParentModel.copy(parentCompany = Some(parentCompanyModelMax)).validate).errorMessage shouldBe
          ParentCompanyCanNotBeUltimateAndDeemed(parentCompanyModelMax).errorMessage
      }

      "Group Company Details are invalid" in {
        leftSideError(fullReturnUltimateParentModel.copy(groupCompanyDetails = groupCompanyDetailsModel.copy(totalCompanies = 0)).validate).errorMessage shouldBe
          GroupCompanyDetailsTotalCompaniesError(0).errorMessage
      }

      "Group Level Elections are invalid" in {
        leftSideError(fullReturnUltimateParentModel.copy(
          groupLevelElections = groupLevelElectionsModelMax.copy(
            groupRatio = groupRatioModelMin.copy(groupEBITDAChargeableGains = Some(true))
          )).validate).errorMessage shouldBe GroupEBITDASupplied(Some(true)).errorMessage
      }

      "Uk Company details are invalid" in {
        leftSideError(fullReturnUltimateParentModel.copy(
          ukCompanies = Seq(ukCompanyModelReactivationMax.copy(companyName = companyNameTooLong))
        ).validate).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }

      "Group Level Amount details are invalid" in {
        leftSideError(fullReturnUltimateParentModel.copy(
          groupLevelAmount = groupLevelAmountModel.copy(interestAllowanceForPeriod = -1)
        ).validate).errorMessage shouldBe GroupLevelAmountCannotBeNegative("interestAllowanceForPeriod", -1).errorMessage
      }

      "Group Ratio is Elected and Adjusted Group Interest details are invalid" in {
        val adjustedGroupInterestValue = adjustedGroupInterestModel.copy(
          qngie = 100,
          groupEBITDA = 200,
          groupRatio = 60
        )
        leftSideError(fullReturnUltimateParentModel.copy(
          adjustedGroupInterest = Some(adjustedGroupInterestValue)
        ).validate).errorMessage shouldBe GroupRatioCalculationError(adjustedGroupInterestValue).errorMessage
      }

      "Group Ratio is Elected and Adjusted Group Interest details are not supplied" in {
        leftSideError(fullReturnUltimateParentModel.copy(
          adjustedGroupInterest = None
        ).validate).errorMessage shouldBe AdjustedNetGroupInterestNotSupplied.errorMessage
      }

      "it is not the appointed reporting company" in {
        leftSideError(fullReturnUltimateParentModel.copy(
          appointedReportingCompany = false
        ).validate).errorMessage shouldBe ReportingCompanyNotAppointed.errorMessage
      }

      "Group Ratio is not Elected and Adjusted Group Interest details are supplied" in {
        leftSideError(fullReturnUltimateParentModel.copy(
          groupLevelElections = groupLevelElectionsModelMax.copy(
            groupRatio = groupRatioModelMin
          ),
          adjustedGroupInterest = Some(adjustedGroupInterestModel)
        ).validate).errorMessage shouldBe AdjustedNetGroupInterestSupplied(adjustedGroupInterestModel).errorMessage
      }
    }
  }
}

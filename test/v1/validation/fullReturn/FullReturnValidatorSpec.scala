/*
 * Copyright 2025 HM Revenue & Customs
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

import data.AgentDetailsConstants.*
import data.GroupLevelElectionsConstants.*
import data.GroupRatioConstants.*
import data.ParentCompanyConstants.*
import data.ReportingCompanyConstants.*
import data.GroupRatioBlendedConstants.*
import data.fullReturn.AdjustedGroupInterestConstants.*
import data.fullReturn.AllocatedReactivationsConstants.*
import data.fullReturn.AllocatedRestrictionsConstants.*
import data.fullReturn.FullReturnConstants.*
import data.fullReturn.GroupLevelAmountConstants.*
import data.fullReturn.UkCompanyConstants.*
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.{Original, Revised, RevisedReturnDetailsModel}
import v1.validation.*
import v1.validation.errors.*

class FullReturnValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "FullReturnValidator" should {

    "Return valid" when {

      "a valid Full Return is supplied" in {
        val model = fullReturnUltimateParentModel
        rightSide(model.validate) shouldBe model
      }

      "aggregate net tax interest income and exceeds cap but not subject to reactivations" in {
        val groupLevelAmount  = fullReturnUltimateParentModel.groupLevelAmount.copy(
          interestReactivationCap = 0
        )
        val numberOfCompanies = 7
        val model             = fullReturnUltimateParentModel
          .copy(
            groupSubjectToInterestReactivation = false,
            groupLevelAmount = groupLevelAmount,
            ukCompanies = Seq.fill(numberOfCompanies)(ukCompanyModelMin)
          )
        rightSide(model.validate) shouldBe model
      }

      "Group Ratio is Elected, Blended is elected and Group EBITDA is not supplied" in {
        val model = fullReturnUltimateParentModel.copy(
          adjustedGroupInterest = fullReturnUltimateParentModel.adjustedGroupInterest.map(_.copy(groupEBITDA = None))
        )
        rightSide(model.validate) shouldBe model
      }
    }

    "Return invalid" when {

      "Return type is Original and some details for a revision are supplied" in {

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              submissionType = Original,
              revisedReturnDetails = Some(RevisedReturnDetailsModel("Revision"))
            )
            .validate
        ).errorMessage shouldBe RevisedReturnDetailsSupplied(RevisedReturnDetailsModel("Revision")).errorMessage
      }

      "Return type is Revised and no details for a revision are supplied" in {

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              submissionType = Revised,
              revisedReturnDetails = None
            )
            .validate
        ).errorMessage shouldBe RevisedReturnDetailsNotSupplied.errorMessage
      }

      "Reporting Company is the same as UPC but Parent Details are supplied" in {

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = true),
              parentCompany = Some(parentCompanyModelUltUkCompany)
            )
            .validate
        ).errorMessage shouldBe ParentCompanyDetailsSupplied(parentCompanyModelMax).errorMessage
      }

      "Reporting Company is the same as Deemed Parent but Parent Details are supplied as deemed" in {

        leftSideError(
          fullReturnDeemedParentModel
            .copy(
              reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = true),
              parentCompany = Some(parentCompanyModelUltUkCompany)
            )
            .validate
        ).errorMessage shouldBe ParentCompanyDetailsSupplied(parentCompanyModelMax).errorMessage
      }

      "Reporting Company is not the same as UPC and UPC is not supplied" in {

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              reportingCompany = reportingCompanyModel,
              parentCompany = None
            )
            .validate
        ).errorMessage shouldBe ParentCompanyDetailsNotSupplied.errorMessage
      }

      "Angie is negative" in {
        leftSideError(
          fullReturnUltimateParentModel.copy(angie = -0.01).validate
        ).errorMessage shouldBe NegativeAngieError(-0.01).errorMessage
      }

      "Angie is greater than 2 decimal places" in {
        leftSideError(
          fullReturnUltimateParentModel.copy(angie = 1.111).validate
        ).errorMessage shouldBe AngieDecimalError(1.111).errorMessage
      }

      "Both group level interest restrictions and group level reactivations are supplied" in {
        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              groupSubjectToInterestReactivation = true,
              groupSubjectToInterestRestrictions = true
            )
            .validate
        ).errorMessage shouldBe GroupLevelInterestRestrictionsAndReactivationSupplied(
          fullReturnUltimateParentModel.groupSubjectToInterestReactivation,
          fullReturnUltimateParentModel.groupSubjectToInterestRestrictions
        ).errorMessage
      }

      "Total Restrictions greater than 2 decimal places" in {
        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              groupSubjectToInterestReactivation = false,
              groupSubjectToInterestRestrictions = true,
              totalRestrictions = 1.111,
              ukCompanies = Seq(ukCompanyModelRestrictionMax, ukCompanyModelRestrictionMax) // 4.44
            )
            .validate
        ).errorMessage shouldBe TotalRestrictionsDecimalError(incorrectDisallowances).errorMessage
      }

      "Calculated Restrictions does not match supplied ammount" in {
        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              groupSubjectToInterestReactivation = false,
              groupSubjectToInterestRestrictions = true,
              totalRestrictions = 6.0,
              ukCompanies = Seq(ukCompanyModelRestrictionMax, ukCompanyModelRestrictionMax)
            )
            .validate
        ).errorMessage shouldBe TotalRestrictionsDoesNotMatch(totalRestrictions, 12.0).errorMessage
      }

      "Group has empty UK companies supplied" in {

        val model = fullReturnUltimateParentModel.copy(
          ukCompanies = Nil
        )

        leftSideError(model.validate).errorMessage shouldBe UkCompaniesEmpty.errorMessage
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

        leftSideErrorLength(model.validate)        shouldBe 3
        leftSideError(model.validate).errorMessage shouldBe CompaniesContainedAllocatedReactivations(
          ukCompanyModelReactivationMax,
          1
        ).errorMessage
      }

      "Group is subject to interest reactivations but total reactivation is greater than the reactivations capacity" in {

        val model = fullReturnUltimateParentModel.copy(
          groupSubjectToInterestReactivation = true,
          groupSubjectToInterestRestrictions = false,
          ukCompanies = Seq(
            ukCompanyModelReactivationMax.copy(
              allocatedReactivations = Some(allocatedReactivationsModel.copy(currentPeriodReactivation = 9001.00))
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedReactivations = Some(allocatedReactivationsModel.copy(currentPeriodReactivation = 9001.00))
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedReactivations = Some(allocatedReactivationsModel.copy(currentPeriodReactivation = 9001.00))
            ),
            ukCompanyModelReactivationMax.copy(
              allocatedReactivations = Some(allocatedReactivationsModel.copy(currentPeriodReactivation = 9001.00))
            )
          ),
          groupLevelAmount = groupLevelAmountModel
        )

        leftSideError(model.validate).errorMessage shouldBe
          TotalReactivationsNotGreaterThanCapacity(
            36004.0,
            fullReturnUltimateParentModel.groupLevelAmount.interestReactivationCap
          ).errorMessage

      }

      "Group is not subject to interest restrictions but has allocated restrictions supplied" in {

        val model = fullReturnUltimateParentModel.copy(
          groupSubjectToInterestRestrictions = false,
          ukCompanies = Seq(
            ukCompanyModelRestrictionMax.copy(
              allocatedRestrictions = Some(allocatedRestrictionsModel)
            ),
            ukCompanyModelRestrictionMax.copy(
              allocatedRestrictions = None
            ),
            ukCompanyModelRestrictionMax.copy(
              allocatedRestrictions = Some(allocatedRestrictionsModel)
            )
          )
        )

        leftSideError(model.validate).errorMessage    shouldBe CompaniesContainedAllocatedRestrictions(
          ukCompanyModelReactivationMax,
          0
        ).errorMessage
        leftSideError(model.validate, 1).errorMessage shouldBe CompaniesContainedAllocatedRestrictions(
          ukCompanyModelReactivationMax,
          1
        ).errorMessage
      }

      "Fails when income and expense both exist" in {

        val model = fullReturnUltimateParentModel.copy(
          ukCompanies = Seq(
            ukCompanyModelRestrictionMax.copy(
              netTaxInterestIncome = 1,
              allocatedRestrictions = Some(allocatedRestrictionsModel)
            )
          )
        )

        leftSideError(model.validate).errorMessage shouldBe
          ExpenseAndIncomeBothNotGreaterThanZero(
            model.ukCompanies.head.netTaxInterestExpense,
            model.ukCompanies.head.netTaxInterestIncome
          ).errorMessage
      }

      "Total Net Tax Income is invalid" in {
        val netTaxInterestIncomeInvalid  = 10
        val netTaxInterestExpenseInvalid = 9

        val model = fullReturnUltimateParentModel.copy(
          groupSubjectToInterestReactivation = false,
          groupSubjectToInterestRestrictions = true,
          totalRestrictions = 1,
          ukCompanies = Seq(
            ukCompanyModelRestrictionMax.copy(
              netTaxInterestIncome = netTaxInterestIncomeInvalid,
              netTaxInterestExpense = netTaxInterestExpenseInvalid,
              allocatedRestrictions = Some(allocatedRestrictionsModel)
            )
          )
        )
        leftSideError(model.validate, 1).errorMessage shouldBe NoTotalNetTaxInterestIncomeDuringRestriction(
          incorrectDisallowances
        ).errorMessage
      }

      "Agent details are invalid" in {
        leftSideError(
          fullReturnUltimateParentModel.copy(agentDetails = agentDetailsModelMax.copy(agentName = None)).validate
        ).errorMessage shouldBe
          AgentNameNotSuppliedError().errorMessage
      }

      "Reporting Company details are invalid" in {
        leftSideError(
          fullReturnUltimateParentModel
            .copy(reportingCompany = reportingCompanyModel.copy(companyName = companyNameTooLong))
            .validate
        ).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }

      "Parent Company is invalid" in {
        leftSideError(
          fullReturnUltimateParentModel.copy(parentCompany = Some(parentCompanyModelMax)).validate
        ).errorMessage shouldBe
          ParentCompanyCanNotBeUltimateAndDeemed(parentCompanyModelMax).errorMessage
      }

      "Uk Company details are invalid" in {
        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              ukCompanies = Seq(ukCompanyModelReactivationMax.copy(companyName = companyNameTooLong))
            )
            .validate
        ).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
      }

      "Group Level Amount details are invalid" in {
        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              groupLevelAmount = groupLevelAmountModel.copy(interestAllowanceForPeriod = -1)
            )
            .validate
        ).errorMessage shouldBe InterestAllowanceForPeriodCannotBeNegative(-1).errorMessage
      }

      "Group Ratio is Elected and Adjusted Group Interest details are not supplied" in {
        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              adjustedGroupInterest = None
            )
            .validate
        ).errorMessage shouldBe AdjustedNetGroupInterestNotSupplied.errorMessage
      }

      "it is not the appointed reporting company" in {
        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              appointedReportingCompany = false
            )
            .validate
        ).errorMessage shouldBe ReportingCompanyNotAppointed.errorMessage
      }

      "Group Ratio is not Elected and Adjusted Group Interest details are supplied" in {
        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              groupLevelElections = groupLevelElectionsModelMax.copy(
                groupRatio = groupRatioModelMin
              ),
              adjustedGroupInterest = Some(adjustedGroupInterestModel)
            )
            .validate
        ).errorMessage shouldBe AdjustedNetGroupInterestSupplied(adjustedGroupInterestModel).errorMessage
      }

      "Return type is Revised and the revised return details are less than 1 character long" in {

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              submissionType = Revised,
              revisedReturnDetails = Some(RevisedReturnDetailsModel(""))
            )
            .validate
        ).errorMessage shouldBe RevisedReturnDetailsLengthError("").errorMessage
      }

      "Return type is Revised and the revised return details are more than 5000 character longs" in {

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              submissionType = Revised,
              revisedReturnDetails = Some(RevisedReturnDetailsModel("a" * 5001))
            )
            .validate
        ).errorMessage shouldBe RevisedReturnDetailsLengthError("a" * 5001).errorMessage
      }

      "Return type is Revised and the revised return details contains invalid characters" in {
        val returnDetails = "ʰʲʺ£$%˦˫qwNew!£$%^&*()_ComPan\n with spacs Ā to ʯ, Ḁ to ỿ :' ₠ to ₿ Å and K lenth is" +
          " 160 no numbers allowed New!£$%^&*()_ComPany with spaces Ā to ʯ, Ḁ to ỿ"

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              submissionType = Revised,
              revisedReturnDetails = Some(RevisedReturnDetailsModel(returnDetails))
            )
            .validate
        ).errorMessage shouldBe RevisedReturnDetailsCharacterError(returnDetails).errorMessage
      }

      "Return type is Revised and the return details contains alternative invalid characters" in {

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              submissionType = Revised,
              revisedReturnDetails = Some(RevisedReturnDetailsModel("ʰʲʺ˦˫˥ʺ˦˫˥"))
            )
            .validate
        ).errorMessage shouldBe RevisedReturnDetailsCharacterError("ʰʲʺ˦˫˥ʺ˦˫˥").errorMessage
      }

      "ReturnContainsEstimates is false but groupEstimateReason is populated" in {

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              returnContainsEstimates = false,
              groupEstimateReason = Some("Some reason")
            )
            .validate
        ).errorMessage shouldBe EstimateReasonSupplied("Some reason").errorMessage
      }

      "ReturnContainsEstimates is true and groupEstimateReason contains more than 10,000 characters" in {

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              returnContainsEstimates = true,
              groupEstimateReason = Some("a" * 10001)
            )
            .validate
        ).errorMessage shouldBe EstimateReasonLengthError("a" * 10001).errorMessage
      }

      "ReturnContainsEstimates is true and groupEstimateReason contains 0 characters" in {

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              returnContainsEstimates = true,
              groupEstimateReason = Some("")
            )
            .validate
        ).errorMessage shouldBe EstimateReasonLengthError("").errorMessage
      }

      "ReturnContainsEstimates is true and groupEstimateReason contains invalid characters" in {
        val estimateReason = "ʰʲʺ£$%˦˫qwNew!£$%^&*()_ComPan\n with spacs Ā to ʯ, Ḁ to ỿ :' ₠ to ₿ Å and K lenth is" +
          "160 no numbers allowed New!£$%^&*()_ComPany with spaces Ā to ʯ, Ḁ to ỿ"

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              returnContainsEstimates = true,
              groupEstimateReason = Some(estimateReason)
            )
            .validate
        ).errorMessage shouldBe EstimateReasonCharacterError(estimateReason).errorMessage
      }

      "ReturnContainsEstimates equals true and groupEstimateReason contains alternative invalid characters" in {

        leftSideError(
          fullReturnUltimateParentModel
            .copy(
              returnContainsEstimates = true,
              groupEstimateReason = Some("ʰʲʺ˦˫˥ʺ˦˫˥")
            )
            .validate
        ).errorMessage shouldBe EstimateReasonCharacterError("ʰʲʺ˦˫˥ʺ˦˫˥").errorMessage
      }

      "ReturnContainsEstimates is false but companies have companyEstimatedReason populated" in {

        val model = fullReturnUltimateParentModel.copy(
          returnContainsEstimates = false,
          groupEstimateReason = None,
          ukCompanies = Seq(
            ukCompanyModelMin.copy(
              companyEstimateReason = Some("Some reason")
            ),
            ukCompanyModelMin.copy(
              companyEstimateReason = None
            ),
            ukCompanyModelMin.copy(
              companyEstimateReason = Some("Some reason 2")
            )
          )
        )

        leftSideError(model.validate).errorMessage    shouldBe CompaniesContainedEstimateReason(
          "Some reason",
          0
        ).errorMessage
        leftSideError(model.validate, 1).errorMessage shouldBe CompaniesContainedEstimateReason(
          "Some reason 2",
          2
        ).errorMessage
      }

      "ReturnContainsEstimates is true and no estimate reason is populated" in {

        val model = fullReturnUltimateParentModel.copy(
          returnContainsEstimates = true,
          groupEstimateReason = None,
          ukCompanies = Seq(
            ukCompanyModelMin.copy(
              companyEstimateReason = None
            ),
            ukCompanyModelMin.copy(
              companyEstimateReason = None
            ),
            ukCompanyModelMin.copy(
              companyEstimateReason = None
            )
          )
        )

        leftSideError(model.validate).errorMessage shouldBe NoEstimatesSupplied(true).errorMessage
      }

      "Ulti parent declaration is false" in {
        val model = fullReturnUltimateParentModel.copy(declaration = false)
        leftSideError(model.validate).errorMessage shouldBe ReturnDeclarationError(false).errorMessage
      }

      "deemed parent declaration is false" in {
        val model =
          fullReturnDeemedParentModel.copy(declaration = false, parentCompany = Some(parentCompanyModelDeemedMin))
        leftSideError(model.validate).errorMessage shouldBe ReturnDeclarationError(false).errorMessage
      }

      "aggregate net tax interest income exceeds the cap" in {
        val numberOfCompanies = 7
        val model             =
          fullReturnUltimateParentModel.copy(ukCompanies = Seq.fill(numberOfCompanies)(ukCompanyModelReactivationMax))
        leftSideError(model.validate).errorMessage shouldBe AggregateNetTaxInterestIncomeExceedsCap(
          fullReturnUltimateParentModel.groupLevelAmount.interestReactivationCap
        ).errorMessage
      }

      "total restriction exceeds aggregate net tax interest expense" in {
        val model = fullReturnUltimateParentModel
          .copy(
            groupSubjectToInterestRestrictions = true,
            groupSubjectToInterestReactivation = false,
            totalRestrictions = 12.0,
            ukCompanies = Seq(
              ukCompanyModelRestrictionMax.copy(netTaxInterestIncome = 0, netTaxInterestExpense = 6.0)
            ),
            groupLevelAmount = groupLevelAmountNoCapModel
          )
        leftSideErrorLength(model.validate)           shouldBe 2
        leftSideError(model.validate, 1).errorMessage shouldBe TotalRestrictionExceedsAggregateNetTaxInterestExpense(
          10.0
        ).errorMessage
      }

      "aggregate net tax interest income and subject to restrictions" in {
        val model = fullReturnUltimateParentModel
          .copy(
            groupSubjectToInterestRestrictions = true,
            groupSubjectToInterestReactivation = false,
            totalRestrictions = 6.0,
            ukCompanies = Seq(
              ukCompanyModelRestrictionMax.copy(netTaxInterestIncome = 0, netTaxInterestExpense = 6.0),
              ukCompanyModelRestrictionMax
                .copy(netTaxInterestIncome = 200.0, netTaxInterestExpense = 0, allocatedRestrictions = None)
            ),
            groupLevelAmount = groupLevelAmountNoCapModel
          )
        leftSideErrorLength(model.validate)        shouldBe 1
        leftSideError(model.validate).errorMessage shouldBe AggregateNetTaxInterestIncomeSubjectToRestrictions(
          true
        ).errorMessage
      }

      "reactivation cap cannot be supplied where the group is not subject to reactivations" in {
        val model = fullReturnModelMin.copy(groupSubjectToInterestReactivation = false)
        leftSideError(model.validate).errorMessage shouldBe ReactivationCapNotSubjectToReactivations(
          fullReturnUltimateParentModel.groupLevelAmount.interestReactivationCap
        ).errorMessage
      }

      "Group Ratio is Elected, Blended is not elected and Group EBITDA is not supplied" in {

        val groupLevelElections = fullReturnUltimateParentModel.groupLevelElections.copy(
          groupRatio = fullReturnUltimateParentModel.groupLevelElections.groupRatio.copy(
            groupRatioBlended = Some(groupRatioBlendedModelMin)
          )
        )

        val model = fullReturnUltimateParentModel.copy(
          adjustedGroupInterest = fullReturnUltimateParentModel.adjustedGroupInterest.map(_.copy(groupEBITDA = None)),
          groupLevelElections = groupLevelElections
        )
        leftSideError(model.validate).errorMessage shouldBe GroupEBITDANotSupplied.errorMessage
      }
    }
  }
}

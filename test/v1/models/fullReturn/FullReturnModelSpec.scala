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

package models.fullReturn

import assets.BaseConstants
import play.api.libs.json.{JsObject, Json, Writes}
import v1.models.fullReturn.FullReturnModel
import assets.fullReturn.FullReturnConstants._
import assets.AgentDetailsConstants._
import assets.GroupCompanyDetailsConstants._
import assets.ReportingCompanyConstants._
import assets.fullReturn.GroupLevelAmountConstants._
import assets.fullReturn.UkCompanyConstants._
import assets.NonConsolidatedInvestmentElectionConstants._
import v1.models.{SubmissionType, Original}
import utils.JsonFormatters
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FullReturnModelSpec extends AnyWordSpec with Matchers with BaseConstants with JsonFormatters {

  implicit val fullReturnFormatter: Writes[FullReturnModel] = fullReturnWrites

  "FullReturnModel" must {

    "correctly write to json" when {

      "max values given with an net expense" in {

        val expectedValue = withoutAppointedReportingCompany(fullReturnNetTaxExpenseJsonMax)
        val actualValue = Json.toJson(fullReturnNetTaxExpenseModelMax)

        actualValue shouldBe expectedValue
      }

      "max values given with an a net income" in {

        val expectedValue = withoutAppointedReportingCompany(fullReturnNetTaxIncomeJsonMax)
        val actualValue = Json.toJson(fullReturnNetTaxIncomeModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = withoutAppointedReportingCompany(fullReturnJsonMin)
        val actualValue = Json.toJson(fullReturnModelMin)

        actualValue shouldBe expectedValue
      }

      "All data is included in the Json.toJson" in {

        val expectedValue = Json.obj(
          "declaration" -> true,
          "agentDetails" -> agentDetailsJsonMin,
          "reportingCompany" -> reportingCompanySameJson,
          "publicInfrastructure" -> true,
          "groupCompanyDetails" -> groupCompanyDetailsJson,
          "submissionType" -> Json.toJson[SubmissionType](Original),
          "groupLevelElections" -> Json.obj(
            "groupRatio" -> Json.obj(
              "isElected" -> false,
              "isElected" -> false,
              "groupEBITDAChargeableGains" -> false,
              "activeGroupEBITDAChargeableGains" -> false,
            ),
            "interestAllowanceAlternativeCalculation" -> true,
            "activeInterestAllowanceAlternativeCalculation" -> true,
            "interestAllowanceNonConsolidatedInvestment" -> nonConsolidatedInvestmentJsonMin,
            "interestAllowanceConsolidatedPartnership" -> Json.obj(
              "isElected" -> false,
              "isActive" -> false
            )
          ),
          "ukCompanies" -> Seq(ukCompanyJsonMin),
          "numberOfUkCompanies" -> 1,
          "angie" -> 0,
          "returnContainsEstimates" -> true,
          "groupEstimateReason" -> "Some reason",
          "groupSubjectToInterestRestrictions" -> false,
          "groupSubjectToInterestReactivation" -> true,
          "totalReactivation" -> 0,
          "totalRestrictions" -> 0,
          "groupLevelAmount" -> groupLevelAmountJson,
          "aggregateNetTaxInterest" -> 50,
          "aggregateAllocatedRestrictions" -> 0,
          "aggregateTaxEBITDA" -> 5,
          "aggregateAllocatedReactivations" -> 0
        )

        val actualValue = Json.toJson(fullReturnModelMin)
        actualValue shouldBe expectedValue
      }
    }

    "not pass the appointedReportingCompany boolean" when {
      "writing json" in {
        val parsedJson: JsObject = Json.toJson(fullReturnModelMin).as[JsObject]
        val appointedReportingCompanyOption: Option[Boolean] = (parsedJson \ "appointedReportingCompany").asOpt[Boolean]
        appointedReportingCompanyOption shouldBe None
      }
    }

    "correctly read from Json" when {

      "max values given without a net expense or income" in {

        val expectedValue = fullReturnModelMax
        val actualValue = fullReturnJsonMax.as[FullReturnModel]

        actualValue shouldBe expectedValue
      }

      "max values given with a net expense" in {

        val expectedValue = fullReturnNetTaxExpenseModelMax
        val actualValue = fullReturnNetTaxExpenseJsonMax.as[FullReturnModel]

        actualValue shouldBe expectedValue
      }

      "max values given with a net income" in {

        val expectedValue = fullReturnNetTaxIncomeModelMax
        val actualValue = fullReturnNetTaxIncomeJsonMax.as[FullReturnModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = fullReturnModelMin
        val actualValue = fullReturnJsonMin.as[FullReturnModel]

        actualValue shouldBe expectedValue
      }
    }

    "deriving the aggregateNetTaxInterest" when {

      "income is bigger" in {
        fullReturnNetTaxIncomeModelMax.aggregateNetTaxInterest shouldBe ((3 * netTaxInterestIncome) - netTaxInterestExpense)
      }

      "expense is bigger" in {
        fullReturnNetTaxExpenseModelMax.aggregateNetTaxInterest shouldBe (netTaxInterestIncome - (3 * netTaxInterestExpense))
      }

      "income and expense are equal" in {

        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(ukCompanyModelMax, ukCompanyModelMin.copy(netTaxInterestIncome = 20.00)))

        fullReturnModel.aggregateNetTaxInterest shouldBe 0
      }
    }

    "deriving the totalReactivation" when {

      "there is a single company without a reactivation" in {

        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(ukCompanyModelRestrictionMax))

        fullReturnModel.totalReactivation shouldBe 0
      }

      "there is a single company with a reactivation" in {

        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(ukCompanyModelReactivationMax))

        fullReturnModel.totalReactivation shouldBe 2.00
      }

      "there is a mixture of companies with reactivations and restrictions" in {

        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(ukCompanyModelRestrictionMax, ukCompanyModelReactivationMax))

        fullReturnModel.totalReactivation shouldBe 2.00
      }

      "there are multiple companies with reactivations" in {

        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(ukCompanyModelReactivationMax, ukCompanyModelReactivationMax, ukCompanyModelReactivationMax))

        fullReturnModel.totalReactivation shouldBe 6.00
      }
    }

    "deriving the publicInfrastructure" when {

      "there is a single company with qic set to true" in {
        val company = ukCompanyModelReactivationMax.copy(qicElection = true)
        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(company))

        fullReturnModel.publicInfrastructure shouldBe true
      }

      "there is a single company with qic set to false" in {
        val company = ukCompanyModelReactivationMax.copy(qicElection = false)
        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(company))

        fullReturnModel.publicInfrastructure shouldBe false
      }

      "there are multiple companies with qic set to true" in {
        val company = ukCompanyModelReactivationMax.copy(qicElection = true)
        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(company, company, company))

        fullReturnModel.publicInfrastructure shouldBe true
      }

      "there are multiple companies with qic set to false" in {
        val company = ukCompanyModelReactivationMax.copy(qicElection = false)
        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(company, company, company))

        fullReturnModel.publicInfrastructure shouldBe false
      }

      "there are companies with a mixture of qic to set to true and false" in {
        val trueCompany = ukCompanyModelReactivationMax.copy(qicElection = true)
        val falseCompany = ukCompanyModelReactivationMax.copy(qicElection = false)
        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(trueCompany, falseCompany, falseCompany))

        fullReturnModel.publicInfrastructure shouldBe true
      }
    }

    "deriving the taxEBITDA" when {

      "there is multiple companies with tax EBITDA" in {
        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(ukCompanyModelReactivationMax, ukCompanyModelReactivationMax))

        fullReturnModel.aggregateTaxEBITDA shouldBe 10
      }

      "the tax EBIDTA adds up to a negative number the aggregate is 0" in {
        val company = ukCompanyModelReactivationMax.copy(taxEBITDA = -1)
        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(company, company))

        fullReturnModel.aggregateTaxEBITDA shouldBe 0
      }

    }

  }

  def withoutAppointedReportingCompany(json: JsObject): JsObject = json - "appointedReportingCompany"
  def withoutDeclaration(json: JsObject): JsObject = json - "declaration"
}
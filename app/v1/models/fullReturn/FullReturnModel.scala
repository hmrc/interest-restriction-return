/*
 * Copyright 2022 HM Revenue & Customs
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

package v1.models.fullReturn

import play.api.Logging
import play.api.libs.json._
import v1.models._
import v1.validation.fullReturn.FullReturnValidator

case class FullReturnModel(
  declaration: Boolean,
  appointedReportingCompany: Boolean,
  agentDetails: AgentDetailsModel,
  reportingCompany: ReportingCompanyModel,
  parentCompany: Option[ParentCompanyModel],
  groupCompanyDetails: GroupCompanyDetailsModel,
  submissionType: SubmissionType,
  revisedReturnDetails: Option[RevisedReturnDetailsModel],
  groupLevelElections: GroupLevelElectionsModel,
  ukCompanies: Seq[UkCompanyModel],
  angie: BigDecimal,
  returnContainsEstimates: Boolean,
  groupEstimateReason: Option[String],
  groupSubjectToInterestRestrictions: Boolean,
  groupSubjectToInterestReactivation: Boolean,
  totalRestrictions: BigDecimal,
  groupLevelAmount: GroupLevelAmountModel,
  adjustedGroupInterest: Option[AdjustedGroupInterestModel]
) extends FullReturnValidator
    with ReturnModel {

  override val fullReturnModel: FullReturnModel   = this
  val totalReactivation: BigDecimal               = ukCompanies.flatMap(_.allocatedReactivations.map(_.currentPeriodReactivation)).sum
  val publicInfrastructure: Boolean               = ukCompanies.map(_.qicElection).exists(identity)
  val totalTaxInterestIncome: BigDecimal          = ukCompanies.map(_.netTaxInterestIncome).sum
  val totalTaxInterestExpense: BigDecimal         = ukCompanies.map(_.netTaxInterestExpense).sum
  val aggregateNetTaxInterest: BigDecimal         = totalTaxInterestIncome - totalTaxInterestExpense
  val numberOfUkCompanies: Int                    = ukCompanies.size
  val aggregateAllocatedRestrictions: BigDecimal  =
    ukCompanies.flatMap(_.allocatedRestrictions.map(_.totalDisallowances)).sum
  val aggregateAllocatedReactivations: BigDecimal = totalReactivation
  val aggregateTaxEBITDA: BigDecimal = {
    val totalTaxEBITDA = ukCompanies.map(_.taxEBITDA).sum
    if (totalTaxEBITDA < 0) {
      0
    } else {
      totalTaxEBITDA
    }
  }
}

object FullReturnModel extends Logging {
  implicit val readsFormat: Reads[FullReturnModel] = Json.reads[FullReturnModel]
}

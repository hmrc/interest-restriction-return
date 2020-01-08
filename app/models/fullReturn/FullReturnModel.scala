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

package models.fullReturn

import models._
import play.api.libs.json.{JsPath, Json}
import validation.fullReturn.FullReturnValidator

case class FullReturnModel(agentDetails: AgentDetailsModel,
                           reportingCompany: ReportingCompanyModel,
                           parentCompany: Option[ParentCompanyModel],
                           publicInfrastructure: Boolean,
                           groupCompanyDetails: GroupCompanyDetailsModel,
                           submissionType: SubmissionType,
                           revisedReturnDetails: Option[String],
                           groupLevelElections: GroupLevelElectionsModel,
                           ukCompanies: Seq[UkCompanyModel],
                           angie: Option[BigDecimal],
                           returnContainsEstimates: Boolean,
                           groupSubjectToInterestRestrictions: Boolean,
                           groupSubjectToInterestReactivation: Boolean,
                           totalReactivation: BigDecimal,
                           groupLevelAmount: GroupLevelAmountModel,
                           adjustedGroupInterest: Option[AdjustedGroupInterestModel]) extends FullReturnValidator {

  override val fullReturnModel: FullReturnModel = this

  val ukCrns: Seq[(JsPath, CRNModel)] = Seq(
    Some(FullReturnModel.reportingCompanyCrnPath -> reportingCompany.crn),
    parentCompany.flatMap(_.ukCrn.map(crn => FullReturnModel.ultimateParentCrnPath -> crn))
  ).flatten
}

object FullReturnModel{
  implicit val format = Json.format[FullReturnModel]

  val reportingCompanyCrnPath: JsPath = JsPath \ "reportingCompany" \ "crn"
  val ultimateParentCrnPath: JsPath = JsPath \ "parentCompany" \ "ultimateParent" \ "crn"
}
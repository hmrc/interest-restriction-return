/*
 * Copyright 2024 HM Revenue & Customs
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

package v1.models.abbreviatedReturn

import play.api.libs.json.*
import v1.models.*
import v1.validation.abbreviatedReturn.AbbreviatedReturnValidator

case class AbbreviatedReturnModel(
  declaration: Boolean,
  appointedReportingCompany: Boolean,
  agentDetails: AgentDetailsModel,
  reportingCompany: ReportingCompanyModel,
  parentCompany: Option[ParentCompanyModel],
  groupCompanyDetails: GroupCompanyDetailsModel,
  submissionType: SubmissionType,
  revisedReturnDetails: Option[RevisedReturnDetailsModel],
  groupLevelElections: Option[GroupLevelElectionsModel],
  ukCompanies: Seq[UkCompanyModel]
) extends AbbreviatedReturnValidator
    with ReturnModel {

  override val abbreviatedReturnModel: AbbreviatedReturnModel = this
  val publicInfrastructure: Boolean                           = ukCompanies.map(_.qicElection).exists(identity)
  val numberOfUkCompanies: Int                                = ukCompanies.size
}

object AbbreviatedReturnModel {
  implicit val abbreviatedReturnReads: Reads[AbbreviatedReturnModel] = Json.reads[AbbreviatedReturnModel]
}

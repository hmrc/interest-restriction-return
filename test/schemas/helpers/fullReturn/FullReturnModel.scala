/*
 * Copyright 2019 HM Revenue & Customs
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

package schemas.helpers.fullReturn

import models.SubmissionType
import play.api.libs.json.Json
import schemas.helpers._

case class FullReturnModel(agentDetails: AgentDetails,
                           reportingCompany: ReportingCompany,
                           parentCompany: ParentCompany,
                           publicInfrastructure: Boolean,
                           groupCompanyDetails: GroupCompanyDetails,
                           submissionType: SubmissionType,
                           revisedReturnDetails: Option[String],
                           groupLevelElections: Option[GroupLevelElections],
                           ukCompanies: Seq[UkCompanyFull],
                           angie: Option[BigDecimal],
                           groupSubjectToInterestRestrictions: Boolean,
                           groupSubjectToInterestReactivation: Boolean,
                           groupLevelAmount: GroupLevelAmount,
                           adjustedGroupInterest: AdjustedGroupInterest)

  object FullReturnModel {
    implicit val writes = Json.writes[FullReturnModel]
  }
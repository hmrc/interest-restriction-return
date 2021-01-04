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

package v1.schemas.helpers.fullReturn
import play.api.libs.json.Json
import v1.schemas.helpers._

case class FullReturnModel(appointedReportingCompany: Option[Boolean] = Some(true),
                           agentDetails: Option[AgentDetails] = Some(AgentDetails()),
                           reportingCompany: Option[ReportingCompany] = Some(ReportingCompany()),
                           parentCompany: Option[ParentCompany] = Some(ParentCompany()),
                           publicInfrastructure: Option[Boolean] = Some(true),
                           groupCompanyDetails: Option[GroupCompanyDetails] = Some(GroupCompanyDetails()),
                           submissionType: Option[String] = Some("original"),
                           revisedReturnDetails: Option[String] = Some("Example details of a Revised Return"),
                           groupLevelElections: Option[GroupLevelElections] = Some(GroupLevelElections()),
                           ukCompanies: Option[Seq[UkCompanyFull]] = Some(Seq(UkCompanyFull())),
                           angie: Option[BigDecimal] = Some(10000000000.87),
                           returnContainsEstimates: Option[Boolean] = Some(true),
                           groupSubjectToInterestRestrictions: Option[Boolean] = Some(true),
                           groupSubjectToInterestReactivation: Option[Boolean] = Some(true),
                           totalReactivation: Option[BigDecimal] = Some(20000000.87),
                           totalRestrictions: Option[BigDecimal] = Some(6.66),
                           groupLevelAmount: Option[GroupLevelAmount] = Some(GroupLevelAmount()),
                           adjustedGroupInterest: Option[AdjustedGroupInterest] = Some(AdjustedGroupInterest()))

  object FullReturnModel {
    implicit val format = Json.writes[FullReturnModel]
  }
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

package models.abbreviatedReturn

import models._
import play.api.libs.json.Json
import validation.abbreviatedReturn.AbbreviatedReturnValidator

case class AbbreviatedReturnModel(agentDetails: AgentDetailsModel,
                                  reportingCompany: ReportingCompanyModel,
                                  parentCompany: Option[ParentCompanyModel],
                                  publicInfrastructure: Boolean,
                                  groupCompanyDetails: GroupCompanyDetailsModel,
                                  submissionType: SubmissionType,
                                  revisedReturnDetails: Option[String],
                                  groupLevelElections: Option[GroupLevelElectionsModel],
                                  ukCompanies: Seq[UkCompanyModel]) extends AbbreviatedReturnValidator {
  override val abbreviatedReturnModel: AbbreviatedReturnModel = this
}

object AbbreviatedReturnModel{

  implicit val format = Json.format[AbbreviatedReturnModel]

}
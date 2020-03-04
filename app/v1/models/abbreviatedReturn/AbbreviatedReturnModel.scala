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

package v1.models.abbreviatedReturn

import play.api.libs.json.{JsPath, Json}
import v1.models._
import v1.validation.abbreviatedReturn.AbbreviatedReturnValidator

case class AbbreviatedReturnModel(appointedReportingCompany: Boolean,
                                  agentDetails: AgentDetailsModel,
                                  reportingCompany: ReportingCompanyModel,
                                  parentCompany: Option[ParentCompanyModel],
                                  publicInfrastructure: Boolean,
                                  groupCompanyDetails: GroupCompanyDetailsModel,
                                  submissionType: SubmissionType,
                                  revisedReturnDetails: Option[String],
                                  groupLevelElections: Option[GroupLevelElectionsModel],
                                  angie: Option[BigDecimal],
                                  ukCompanies: Seq[UkCompanyModel]) extends AbbreviatedReturnValidator {

  override val abbreviatedReturnModel: AbbreviatedReturnModel = this
}

object AbbreviatedReturnModel{

  implicit val format = Json.format[AbbreviatedReturnModel]

  val ultimateParentCrnPath: JsPath = JsPath \ "parentCompany" \ "ultimateParent" \ "crn"
  def deemedParentCrnPath(i: Int): JsPath = JsPath \ "parentCompany" \ s"deemedParent[$i]" \ "crn"
}
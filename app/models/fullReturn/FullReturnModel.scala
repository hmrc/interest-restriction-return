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


  private val reportingCompanyCrnWithPath: (JsPath, CRNModel) = FullReturnModel.reportingCompanyCrnPath -> reportingCompany.crn

  private val ultimateParentCrnWithPath: Seq[(JsPath, CRNModel)] = parentCompany.flatMap(_.ultimateUkCrns).fold[Seq[(JsPath, CRNModel)]](Seq()){
    crn => Seq(FullReturnModel.ultimateParentCrnPath -> crn)
  }

  private val deemedParentCrnsWithPath: Seq[(JsPath, CRNModel)] = parentCompany.flatMap(_.deemedUkCrns).fold[Seq[(JsPath, CRNModel)]](Seq()){
    _.zipWithIndex.map(x => FullReturnModel.deemedParentCrnPath(x._2) -> x._1)
  }

  val ukCrns: Seq[(JsPath, CRNModel)] = ultimateParentCrnWithPath ++ deemedParentCrnsWithPath :+ reportingCompanyCrnWithPath
}

object FullReturnModel{
  implicit val format = Json.format[FullReturnModel]

  val reportingCompanyCrnPath: JsPath = JsPath \ "reportingCompany" \ "crn"
  val ultimateParentCrnPath: JsPath = JsPath \ "parentCompany" \ "ultimateParent" \ "crn"
  def deemedParentCrnPath(i: Int): JsPath = JsPath \ "parentCompany" \ s"deemedParent[$i]" \ "crn"
}
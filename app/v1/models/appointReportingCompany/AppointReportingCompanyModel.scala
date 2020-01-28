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

package v1.models.appointReportingCompany

import play.api.libs.json.{JsPath, Json}
import v1.models._
import v1.validation.appointReportingCompany.AppointReportingCompanyValidator

case class AppointReportingCompanyModel(agentDetails: AgentDetailsModel,
                                        reportingCompany: ReportingCompanyModel,
                                        authorisingCompanies: Seq[AuthorisingCompanyModel],
                                        isReportingCompanyAppointingItself: Boolean,
                                        identityOfAppointingCompany: Option[IdentityOfCompanySubmittingModel],
                                        ultimateParentCompany: Option[UltimateParentModel],
                                        accountingPeriod: AccountingPeriodModel,
                                        declaration: Boolean) extends AppointReportingCompanyValidator {

  override val appointReportingCompanyModel: AppointReportingCompanyModel = this

  val ukCrns: Seq[(JsPath, CRNModel)] = Seq(
    Some(AppointReportingCompanyModel.reportingCompanyCrnPath -> reportingCompany.crn),
    ultimateParentCompany.flatMap(_.crn.map(crn => AppointReportingCompanyModel.ultimateParentCrnPath -> crn)),
    identityOfAppointingCompany.flatMap(_.crn.map(crn => AppointReportingCompanyModel.identityOfAppointingCompanyCrnPath -> crn))
  ).flatten
}

object AppointReportingCompanyModel{

  implicit val format = Json.format[AppointReportingCompanyModel]

  val reportingCompanyCrnPath: JsPath = JsPath \ "reportingCompany" \ "crn"
  val ultimateParentCrnPath: JsPath = JsPath \ "ultimateParentCompany" \ "crn"
  val identityOfAppointingCompanyCrnPath: JsPath = JsPath \ "identityOfAppointingCompany" \ "crn"
}
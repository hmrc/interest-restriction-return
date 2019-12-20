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

package assets

import java.time.LocalDate

import models._
import models.revokeReportingCompany.RevokeReportingCompanyModel

object RevokeReportingCompanyConstants extends BaseConstants {

  private val date = LocalDate.now()

  val revokeReportingCompanyModel = RevokeReportingCompanyModel(
    agentDetails = AgentDetailsModel(
      true,Some("agent")
    ),
    reportingCompany = ReportingCompanyModel(
      companyName,
      ctutr,
      Some(crn),
      true
    ),
    isReportingCompanyRevokingItself = true,
    companyMakingRevocation = Some(IdentityOfCompanySubmittingModel(
      companyName,
      Some(ctutr),
      Some(crn),
      None,
      None
    )),
    ultimateParent = Some(UltimateParentModel(
      CompanyNameModel("name"),
      Some(ctutr),
      Some(crn),
      Some("name"),
      None,
      None
    )),
    accountingPeriod = AccountingPeriodModel(
      date.minusMonths(6),
      date.minusMonths(3)
    ),
    authorisingCompanies = Seq(
      AuthorisingCompanyModel(
        CompanyNameModel("name"),
        ctutr,
        Some(true)
      )
    ),
    declaration = true
  )
}

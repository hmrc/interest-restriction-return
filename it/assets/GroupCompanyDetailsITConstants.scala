package assets

import assets.AccountingPeriodITConstants.accountingPeriodJson

import play.api.libs.json.Json

object GroupCompanyDetailsITConstants {

  val totalCompanies = 1

  val groupCompanyDetailsJson = Json.obj(
    "totalCompanies" -> totalCompanies,
    "accountingPeriod" -> accountingPeriodJson
  )
}

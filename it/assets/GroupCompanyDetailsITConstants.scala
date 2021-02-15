package assets

import assets.AccountingPeriodITConstants.accountingPeriodJson
import play.api.libs.json.Json

object GroupCompanyDetailsITConstants {

  val groupCompanyDetailsJson = Json.obj(
    "totalCompanies" -> 0,
    "accountingPeriod" -> accountingPeriodJson
  )
}

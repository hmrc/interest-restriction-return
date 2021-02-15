package assets

import assets.AccountingPeriodITConstants.accountingPeriodJson
import play.api.libs.json.Json

object GroupCompanyDetailsITConstants {

  val groupCompanyDetailsJson = Json.obj(
    "totalCompanies" -> 1,
    "accountingPeriod" -> accountingPeriodJson
  )
}

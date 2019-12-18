package assets

import java.time.LocalDate

import play.api.libs.json.Json

object AccountingPeriodITConstants {

  val startDate: LocalDate = LocalDate.now().minusMonths(18)
  val endDate: LocalDate = LocalDate.now().minusDays(1)

  val accountingPeriodJson = Json.obj(
    "startDate" -> startDate,
    "endDate" -> endDate
  )
}

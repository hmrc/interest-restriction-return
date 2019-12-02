package assets

import java.time.LocalDate

import play.api.libs.json.Json

object AccountingPeriodITConstants {

  val startDate: LocalDate = LocalDate.of(2000,1,1)
  val endDate: LocalDate = LocalDate.of(2010,1,1)

  val accountingPeriodJson = Json.obj(
    "startDate" -> startDate,
    "endDate" -> endDate
  )
}

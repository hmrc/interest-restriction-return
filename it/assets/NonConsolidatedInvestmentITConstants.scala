package assets

import play.api.libs.json.Json

object NonConsolidatedInvestmentITConstants {

  val consolidatedInvestments = Seq("investment1", "investment2", "investment3")


  val nonConsolidatedInvestmentJson= Json.obj(
    "isElected" -> true,
    "nonConsolidatedInvestments" -> consolidatedInvestments
  )
}

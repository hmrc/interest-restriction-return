package assets

import play.api.libs.json.Json
import assets.GroupRatioITConstants._
import assets.NonConsolidatedInvestmentElectionITConstants._
import assets.ConsolidatedPartnershipITConstants._

object GroupLevelElectionsITConstants {

  val groupLevelElectionsJson= Json.obj(
    "groupRatio" -> groupRatioJson,
    "interestAllowanceAlternativeCalculation" -> true,
    "interestAllowanceNonConsolidatedInvestment" -> nonConsolidatedInvestmentJson,
    "interestAllowanceConsolidatedPartnership" -> consolidatedPartnershipsJson
  )

}

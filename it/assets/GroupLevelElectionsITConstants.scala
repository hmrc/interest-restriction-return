package assets

import play.api.libs.json.Json
import assets.GroupRatioBlendedITConstants.groupRatioBlendedJson
import assets.NonConsolidatedInvestmentITConstants.nonConsolidatedInvestmentJson
import assets.ConsolidatedPartnershipITConstants.consolidatedPartnershipsJson

object GroupLevelElectionsITConstants {

  val groupLevelElectionsJson = Json.obj(
    "isElected" -> true,
    "groupRatioBlended" -> groupRatioBlendedJson,
    "groupEBITDAChargeableGains" -> true,
    "interestAllowanceAlternativeCalculation" -> true,
    "interestAllowanceNonConsolidatedInvestment" -> nonConsolidatedInvestmentJson,
    "interestAllowanceConsolidatedPartnership" -> consolidatedPartnershipsJson
  )
}

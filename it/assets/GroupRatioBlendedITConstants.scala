package assets

import play.api.libs.json.Json
import assets.InvestorGroupITConstants._

object GroupRatioBlendedITConstants {

  val investorGroups = Seq("investment1", "investment2", "investment3")

  val groupRatioBlendedJson= Json.obj(
    "isElected" -> true,
    "investorGroups" -> Seq(investorGroupsJson)
  )
}

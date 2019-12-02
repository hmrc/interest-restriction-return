package assets

import play.api.libs.json.Json

object GroupRatioBlendedITConstants {

  val investorGroups = Seq("investment1", "investment2", "investment3")

  val groupRatioBlendedJson = Json.obj(
    "isElected" -> true,
    "investorGroups" -> investorGroups
  )
}

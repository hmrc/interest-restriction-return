package assets

import models.ConsolidatedPartnershipModel
import play.api.libs.json.Json

object ConsolidatedPartnershipITConstants {

  val consolidatedPartnerships = Seq("investment1", "investment2", "investment3")

  val consolidatedPartnershipsJson = Json.obj(
    "isElected" -> true,
    "consolidatedPartnerships" -> consolidatedPartnerships
  )
}

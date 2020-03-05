package assets

import assets.UltimateParentITConstants._
import assets.DeemedParentITConstants._
import play.api.libs.json.Json

object ParentCompanyITConstants {

  val parentCompanyJson = Json.obj(
    "ultimateParent" -> ultimateParentJson,
    "deemedParent" -> Seq(deemedParentJson, deemedParentJson)
  )
}

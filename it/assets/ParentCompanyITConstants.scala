package assets


import assets.UltimateParentITConstants.ukParentJson
import assets.DeemedParentITConstants.deemedParentJson
import play.api.libs.json.Json

object ParentCompanyITConstants {


  val parentCompanyJsonMax = Json.obj(
    "ultimateParent" -> ukParentJson,
    "deemedParent" -> Seq(deemedParentJson)
  )
}

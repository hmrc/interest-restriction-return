package assets

import play.api.libs.json.Json

object DeemedParentITConstants extends BaseITConstants {

  val nonUkCountryCode = "US"

  val deemedParentJson= Json.obj(
    "companyName" -> companyName,
    "ctutr" -> ctutr)
}

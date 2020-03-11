package assets

import play.api.libs.json.Json

object UltimateParentITConstants extends BaseITConstants {

  val registeredCompanyName = "some company"
  val otherUkTaxReference = "other reference"

  val ultimateParentJson= Json.obj(
    "registeredCompanyName" -> registeredCompanyName,
    "ctutr" -> ctutr
  )
}

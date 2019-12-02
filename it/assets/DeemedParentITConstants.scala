package assets

import play.api.libs.json.Json

object DeemedParentITConstants extends BaseITConstants {

  val companyName = "some company ltd"
  val knownAs = "some company"
  val nonUkCountryCode = "US"

  val deemedParentJson = Json.obj(
    "companyName" -> companyName,
    "ctutr" -> ctutr,
    "crn" -> crn,
    "knownAs" -> knownAs,
    "sautr" -> sautr,
    "countryOfIncorporation" -> nonUkCountryCode
  )
}

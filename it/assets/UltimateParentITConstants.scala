package assets

import play.api.libs.json.Json

object UltimateParentITConstants extends BaseITConstants {

  val registeredCompanyName = "some company"
  val knownAs = "something"
  val otherUkTaxReference = "other reference"
  val countryOfIncorporation = "US"
  val nonUkCrn = "aA1234567890"

  val ultimateParentJson= Json.obj(
    "registeredCompanyName" -> registeredCompanyName,
    "ctutr" -> ctutr,
    "crn" -> crn,
    "knownAs" -> knownAs,
    "countryOfIncorporation" -> countryOfIncorporation,
    "nonUkCrn" -> nonUkCrn
  )
}

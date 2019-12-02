package assets

import play.api.libs.json.Json

object UltimateParentITConstants extends BaseITConstants {

  val registeredCompanyName = "some company"
  val knownAs = "something"
  val otherUkTaxReference = "other reference"


  val ukParentJson = Json.obj(
    "registeredCompanyName" -> registeredCompanyName,
    "ctutr" -> ctutr,
    "crn" -> crn,
    "knownAs" -> knownAs,
    "sautr" -> otherUkTaxReference
  )

  val nonUkParentJson = Json.obj(
    "registeredCompanyName" -> registeredCompanyName,
    "knownAs" -> knownAs,
    "countryOfIncorporation" -> "US",
    "crn" -> crn
  )
}

/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package assets

import assets.DeemedParentConstants._
import assets.UltimateParentConstants._
import play.api.libs.json.Json
import v1.models.ParentCompanyModel

object ParentCompanyConstants {

  val parentCompanyModelMax = ParentCompanyModel(
    ultimateParent = Some(ultimateParentModelMax),
    deemedParent = Some(Seq(deemedParentModelMax, deemedParentModelMax))
  )

  val parentCompanyModelMin = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Some(Seq(deemedParentModelMin, deemedParentModelMin))
  )

  val parentCompanyModelUltMax = ParentCompanyModel(
    ultimateParent = Some(ultimateParentModelUkCompany),
    deemedParent = None
  )

  val parentCompanyModelUltUkCompany = ParentCompanyModel(
    ultimateParent = Some(ultimateParentModelUkCompany),
    deemedParent = None
  )

  val parentCompanyModelUltNonUkCompany = ParentCompanyModel(
    ultimateParent = Some(ultimateParentModelNonUkCompany),
    deemedParent = None
  )

  val parentCompanyModelUltUkPartnership = ParentCompanyModel(
    ultimateParent = Some(ultimateParentModelUkPartnership),
    deemedParent = None
  )

  val parentCompanyModelDeemedMin = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Some(Seq(deemedParentModelUkCompany, deemedParentModelUkCompany))
  )

  val parentCompanyModelNone = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = None
  )

  val parentCompanyModelDeemedMax = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Some(
      Seq(
        deemedParentModelUkCompany,
        deemedParentModelUkPartnership,
        deemedParentModelMax
      )
    )
  )

  val parentCompanyModelDeemedUkCompany = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Some(Seq(deemedParentModelUkCompany, deemedParentModelUkCompany))
  )

  val parentCompanyModelDeemedNonUkCompany = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Some(Seq(deemedParentModelNonUkCompany))
  )

  val parentCompanyModelDeemedUkPartnership = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Some(Seq(deemedParentModelUkPartnership))
  )

  val parentCompanyJsonMax = Json.obj(
    "ultimateParent" -> ultimateParentJsonMax,
    "deemedParent"   -> Seq(deemedParentJsonMax)
  )

  val parentCompanyJsonMin = Json.obj(
    "deemedParent" -> Seq(deemedParentJsonMin, deemedParentJsonMin)
  )

  val parentCompanyJsonUltMax = Json.obj(
    "ultimateParent" -> ultimateParentJsonMax
  )

  val parentCompanyJsonUltUkCompany = Json.obj(
    "ultimateParent" -> ultimateParentJsonUkCompany
  )

  val parentCompanyJsonUltNonUkCompany = Json.obj(
    "ultimateParent" -> ultimateParentJsonNonUkCompany
  )

  val parentCompanyJsonUltUkPartnership = Json.obj(
    "ultimateParent" -> ultimateParentJsonUkPartnership
  )

  val parentCompanyJsonDeemedMin = Json.obj(
    "deemedParent" -> Seq(deemedParentJsonMin, deemedParentJsonMin)
  )

  val parentCompanyJsonDeemedMax = Json.obj(
    "deemedParent" -> Seq(
      deemedParentJsonMax,
      deemedParentJsonMin,
      deemedParentJsonNonUkCompany
    )
  )

  val parentCompanyJsonDeemedUkCompany = Json.obj(
    "deemedParent" -> Seq(deemedParentJsonUkCompany, deemedParentJsonUkCompany)
  )

  val parentCompanyJsonDeemedNonUkCompany = Json.obj(
    "deemedParent" -> Seq(deemedParentJsonNonUkCompany)
  )

  val parentCompanyJsonDeemedUkPartnership = Json.obj(
    "deemedParent" -> Seq(deemedParentJsonUkPartnership)
  )
}

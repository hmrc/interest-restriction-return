/*
 * Copyright 2024 HM Revenue & Customs
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

package data

import data.DeemedParentConstants._
import data.UltimateParentConstants._
import play.api.libs.json.{JsObject, Json}
import v1.models.ParentCompanyModel

object ParentCompanyConstants {

  val parentCompanyModelMax: ParentCompanyModel = ParentCompanyModel(
    ultimateParent = Some(ultimateParentModelMax),
    deemedParent = Some(Seq(deemedParentModelMax, deemedParentModelMax))
  )

  val parentCompanyModelMin: ParentCompanyModel = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Some(Seq(deemedParentModelMin, deemedParentModelMin))
  )

  val parentCompanyModelUltMax: ParentCompanyModel = ParentCompanyModel(
    ultimateParent = Some(ultimateParentModelUkCompany),
    deemedParent = None
  )

  val parentCompanyModelUltUkCompany: ParentCompanyModel = ParentCompanyModel(
    ultimateParent = Some(ultimateParentModelUkCompany),
    deemedParent = None
  )

  val parentCompanyModelUltUkPartnership: ParentCompanyModel = ParentCompanyModel(
    ultimateParent = Some(ultimateParentModelUkPartnership),
    deemedParent = None
  )

  val parentCompanyModelDeemedMin: ParentCompanyModel = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Some(Seq(deemedParentModelUkCompany, deemedParentModelUkCompany))
  )

  val parentCompanyModelNone: ParentCompanyModel = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = None
  )

  val parentCompanyModelDeemedMax: ParentCompanyModel = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Some(
      Seq(
        deemedParentModelUkCompany,
        deemedParentModelUkPartnership,
        deemedParentModelMax
      )
    )
  )

  val parentCompanyModelDeemedUkCompany: ParentCompanyModel = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Some(Seq(deemedParentModelUkCompany, deemedParentModelUkCompany))
  )

  val parentCompanyModelDeemedNonUkCompany: ParentCompanyModel = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Some(Seq(deemedParentModelNonUkCompany))
  )

  val parentCompanyModelDeemedUkPartnership: ParentCompanyModel = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Some(Seq(deemedParentModelUkPartnership))
  )

  val parentCompanyJsonMax: JsObject = Json.obj(
    "ultimateParent" -> ultimateParentJsonMax,
    "deemedParent"   -> Seq(deemedParentJsonMax)
  )

  val parentCompanyJsonMin: JsObject = Json.obj(
    "deemedParent" -> Seq(deemedParentJsonMin, deemedParentJsonMin)
  )

  val parentCompanyJsonUltMax: JsObject = Json.obj(
    "ultimateParent" -> ultimateParentJsonMax
  )

  val parentCompanyJsonUltUkCompany: JsObject = Json.obj(
    "ultimateParent" -> ultimateParentJsonUkCompany
  )

  val parentCompanyJsonUltNonUkCompany: JsObject = Json.obj(
    "ultimateParent" -> ultimateParentJsonNonUkCompany
  )

  val parentCompanyJsonUltUkPartnership: JsObject = Json.obj(
    "ultimateParent" -> ultimateParentJsonUkPartnership
  )

  val parentCompanyJsonDeemedMin: JsObject = Json.obj(
    "deemedParent" -> Seq(deemedParentJsonMin, deemedParentJsonMin)
  )

  val parentCompanyJsonDeemedMax: JsObject = Json.obj(
    "deemedParent" -> Seq(
      deemedParentJsonMax,
      deemedParentJsonMin,
      deemedParentJsonNonUkCompany
    )
  )

  val parentCompanyJsonDeemedUkCompany: JsObject = Json.obj(
    "deemedParent" -> Seq(deemedParentJsonUkCompany, deemedParentJsonUkCompany)
  )

  val parentCompanyJsonDeemedNonUkCompany: JsObject = Json.obj(
    "deemedParent" -> Seq(deemedParentJsonNonUkCompany)
  )

  val parentCompanyJsonDeemedUkPartnership: JsObject = Json.obj(
    "deemedParent" -> Seq(deemedParentJsonUkPartnership)
  )
}

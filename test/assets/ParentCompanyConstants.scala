/*
 * Copyright 2019 HM Revenue & Customs
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

import assets.UltimateParentConstants._
import assets.DeemedParentConstants._
import models.abbreviatedReturn.{DeemedParentModel, ParentCompanyModel}
import play.api.libs.json.Json

object ParentCompanyConstants {

  val parentCompanyJsonMax = Json.obj(
    "ultimateParent" -> ukParentJsonMax,
    "deemedParent" -> Seq(deemedParentModelMax)
  )

  val parentCompanyModelMax = ParentCompanyModel(
    ultimateParent = Some(ukParentModelMax),
    deemedParent = Seq(deemedParentModelMax)
  )

  val parentCompanyJsonMin = Json.obj(
    "deemedParent" -> Seq.empty[DeemedParentModel]
  )

  val parentCompanyModelMin = ParentCompanyModel(
    ultimateParent = None,
    deemedParent = Seq()
  )
}



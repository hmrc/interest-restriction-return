/*
 * Copyright 2020 HM Revenue & Customs
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

package v1.models

import play.api.libs.json.Json
import v1.validation.ParentCompanyValidator

case class ParentCompanyModel(ultimateParent: Option[UltimateParentModel],
                              deemedParent: Option[Seq[DeemedParentModel]])  extends ParentCompanyValidator {

  override val parentCompanyModel: ParentCompanyModel = this

  val deemedUkCrns: Option[Seq[CRNModel]] = deemedParent.map{_.flatMap{_.crn}}
  val ultimateUkCrns: Option[CRNModel] = ultimateParent.flatMap(_.crn)
}


object ParentCompanyModel {

  implicit val format = Json.format[ParentCompanyModel]
}
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

package models

import play.api.libs.json.{JsPath, JsString, Reads, Writes}
import validation.CRNValidator

case class CRNModel(crn: String) extends CRNValidator{
  override val crnModel = this
}

object CRNModel {

  implicit val reads: Reads[CRNModel] = JsPath.read[String].map(CRNModel.apply)

  implicit val writes: Writes[CRNModel] = Writes {
    model => JsString(model.crn)
  }

}



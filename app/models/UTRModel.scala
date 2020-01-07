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

package models

import play.api.libs.json.{JsPath, JsString, Reads, Writes}
import validation.UTRValidator

case class UTRModel(utr: String) extends UTRValidator{
  override val utrModel = this
}

object UTRModel {

  implicit val reads: Reads[UTRModel] = JsPath.read[String].map(UTRModel.apply)

  implicit val writes: Writes[UTRModel] = Writes {
    model => JsString(model.utr)
  }

}

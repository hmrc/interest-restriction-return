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

import play.api.libs.json.{JsPath, JsString, Reads, Writes}
import v1.validation.CountryCodeValidator

case class CountryCodeModel(code: String) extends CountryCodeValidator {
  override val countryCodeModel = this
}

object CountryCodeModel {

  implicit val reads: Reads[CountryCodeModel] = JsPath.read[String].map(CountryCodeModel.apply)

  implicit val writes: Writes[CountryCodeModel] = Writes {
    model => JsString(model.code)
  }

}

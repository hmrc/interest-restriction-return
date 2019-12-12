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

import play.api.libs.json.{Format, Json, Reads, Writes, _}

sealed trait UltimateParentModel {

  val registeredCompanyName: String
  val knownAs: Option[String]
  val crn: String
}

case class NonUkParentModel(registeredCompanyName: String,
                            knownAs: Option[String],
                            countryOfIncorporation: String,
                            crn: String) extends UltimateParentModel


case class UkParentModel(registeredCompanyName: String,
                         ctutr: Option[String],
                         crn: String,
                         knownAs: Option[String],
                         sautr: Option[String]) extends UltimateParentModel

object UltimateParentModel {

  implicit def writes: Writes[UltimateParentModel] = Writes {
    case x: UkParentModel => Json.toJson(x)(UkParentModel.format)
    case x: NonUkParentModel => Json.toJson(x)(NonUkParentModel.format)
  }

  implicit def reads: Reads[UltimateParentModel] = {
    __.read[UkParentModel](UkParentModel.format).map(x => x: UltimateParentModel) orElse
      __.read[NonUkParentModel](NonUkParentModel.format).map(x => x: UltimateParentModel)
  }
}

object UkParentModel {

  implicit def format: Format[UkParentModel] = Json.format[UkParentModel]
}

object NonUkParentModel {

  implicit def format: Format[NonUkParentModel] = Json.format[NonUkParentModel]
}

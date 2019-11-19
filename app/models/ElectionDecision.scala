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

import play.api.libs.json._

sealed trait ElectionDecision

case object Elect extends ElectionDecision {

  override val toString = "elect"
}

case object Revoke extends ElectionDecision {

  override val toString = "revoke"
}

object ElectionDecision {

  implicit val writes: Writes[ElectionDecision] = Writes {
    case Elect => JsString(Elect.toString)
    case Revoke => JsString(Revoke.toString)
  }

  implicit val reads: Reads[ElectionDecision] = Reads {
    case JsString(Elect.toString) => JsSuccess(Elect)
    case JsString(Revoke.toString) => JsSuccess(Revoke)
    case _ => JsError("Unknown ElectionDecision")
  }
}
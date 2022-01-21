/*
 * Copyright 2022 HM Revenue & Customs
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

import play.api.libs.json._

sealed trait SubmissionType

case object Original extends SubmissionType {

  override val toString = "original"
}

case object Revised extends SubmissionType {

  override val toString = "revised"
}

object SubmissionType {

  implicit val writes: Writes[SubmissionType] = Writes {
    case Original => JsString(Original.toString)
    case Revised => JsString(Revised.toString)
  }

  implicit val reads: Reads[SubmissionType] = Reads {
    case JsString(Original.toString) => JsSuccess(Original)
    case JsString(Revised.toString) => JsSuccess(Revised)
    case _ => JsError(s"Valid submission types are ${Original.toString} and ${Revised.toString}")
  }
}

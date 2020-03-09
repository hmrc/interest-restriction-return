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

package v1.validation

import play.api.libs.json.{JsString, Writes}

sealed trait ErrorCode
case object UNEXPECTED_FIELD extends ErrorCode
case object MISSING_FIELD extends ErrorCode
case object NEGATIVE_AMOUNT extends ErrorCode
case object MISMATCHED_AMOUNT extends ErrorCode
case object INVALID_DATE extends ErrorCode
case object INVALID_LENGTH extends ErrorCode
case object APPOINT_REPORTING_COMPANY extends ErrorCode
case object INVALID_PERCENTAGE extends ErrorCode
case object INVALID_AMOUNT extends ErrorCode
case object INVALID_COUNTRY_CODE extends ErrorCode
case object INVALID_CRN extends ErrorCode
case object INVALID_UTR extends ErrorCode
case object COMPANIES_DECLARATION extends ErrorCode
case object INVALID_JSON extends ErrorCode
case object BAD_REQUEST extends ErrorCode

object ErrorCode {
  implicit val writes: Writes[ErrorCode] = Writes { x => JsString(x.toString) }
}

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

package v1.connectors

import play.api.http.Status.INTERNAL_SERVER_ERROR
import play.api.libs.json.{Json, OFormat}
import v1.models.nrs.NrSubmissionId

case class DesSuccessResponse(acknowledgementReference: String)

object DesSuccessResponse {
  implicit val fmt: OFormat[DesSuccessResponse] = Json.format[DesSuccessResponse]
}

object HttpHelper {
  type SubmissionResponse = Either[ErrorResponse, DesSuccessResponse]
  type NrsResponse        = Either[ErrorResponse, NrSubmissionId]
}

sealed trait ErrorResponse

case class NrsError(errorMessage: String) extends ErrorResponse

case class InvalidSuccessResponse(
  status: Int = INTERNAL_SERVER_ERROR,
  body: String = HttpErrorMessages.UNEXPECTED_ERROR
) extends ErrorResponse

case class UnexpectedFailure(status: Int, body: String) extends ErrorResponse

object HttpErrorMessages {
  val ABBREVIATED_ERROR = "Error returned when trying to submit abbreviated return"
  val APPOINT_ERROR     = "Error returned when trying to appoint a reporting company"
  val FULL_ERROR        = "Error returned when trying to submit a full return"
  val REVOKE_ERROR      = "Error returned when trying to revoke a reporting company"
  val UNEXPECTED_ERROR  = "Invalid Json returned in Success response"
  val NRS_ERROR         = "Error returned when trying to submit Non Repudiation Submission"
}

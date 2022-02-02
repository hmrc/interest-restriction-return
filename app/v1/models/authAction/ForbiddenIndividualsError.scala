package v1.models.authAction

import play.api.http.Status.FORBIDDEN
import play.api.libs.json.{Json, OFormat}

case class ForbiddenIndividualsError(status: Int = FORBIDDEN, message: String =
"You are unable to access this service as an individual. This service is only available to individual companies or groups of companies.")

object ForbiddenIndividualsError {
  implicit val format: OFormat[ForbiddenIndividualsError] = Json.format[ForbiddenIndividualsError]
}

/*
 * Copyright 2023 HM Revenue & Customs
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

package v1.models.nrs

import play.api.libs.json._
import uk.gov.hmrc.auth.core.retrieve._
import uk.gov.hmrc.auth.core.{AffinityGroup, ConfidenceLevel, CredentialRole}

import java.time.LocalDate
import java.util.UUID

case class NrsRetrievalData(
  internalId: Option[String],
  externalId: Option[String],
  agentCode: Option[String],
  optionalCredentials: Option[Credentials],
  confidenceLevel: ConfidenceLevel,
  nino: Option[String],
  saUtr: Option[String],
  optionalName: Option[Name],
  dateOfBirth: Option[LocalDate],
  email: Option[String],
  agentInformation: AgentInformation,
  groupIdentifier: Option[String],
  credentialRole: Option[CredentialRole],
  mdtpInformation: Option[MdtpInformation],
  optionalItmpName: Option[ItmpName],
  itmpDateOfBirth: Option[LocalDate],
  optionalItmpAddress: Option[ItmpAddress],
  affinityGroup: Option[AffinityGroup],
  credentialStrength: Option[String],
  loginTimes: LoginTimes
)

object NrsRetrievalData {
  implicit val credentialsFormat: OFormat[Credentials]           = Json.format[Credentials]
  implicit val nameFormat: OFormat[Name]                         = Json.format[Name]
  implicit val agentInformationFormat: OFormat[AgentInformation] = Json.format[AgentInformation]
  implicit val mdtpInformationFormat: OFormat[MdtpInformation]   = Json.format[MdtpInformation]
  implicit val itmpNameFormat: OFormat[ItmpName]                 = Json.format[ItmpName]
  implicit val itmpAddressFormat: OFormat[ItmpAddress]           = Json.format[ItmpAddress]
  implicit val loginTimesFormat: OFormat[LoginTimes]             = Json.format[LoginTimes]
  implicit val nrsRetrievalData: OFormat[NrsRetrievalData]       = Json.format[NrsRetrievalData]
}

case class NrSubmissionId(nrSubmissionId: UUID) extends AnyVal {
  override def toString: String = nrSubmissionId.toString
}

object NrSubmissionId {
  implicit val format: OFormat[NrSubmissionId] = Json.format[NrSubmissionId]
}

case class NrsMetadata(
  businessId: String,
  notableEvent: String,
  payloadContentType: String,
  payloadSha256Checksum: String,
  userSubmissionTimestamp: String,
  identityData: NrsRetrievalData,
  userAuthToken: String,
  headerData: JsValue,
  searchKeys: JsValue
)

object NrsMetadata {
  implicit val format: OFormat[NrsMetadata] = Json.format[NrsMetadata]
}

case class NrsPayload(payload: String, metadata: NrsMetadata)

object NrsPayload {
  implicit val format: OFormat[NrsPayload] = Json.format[NrsPayload]
}

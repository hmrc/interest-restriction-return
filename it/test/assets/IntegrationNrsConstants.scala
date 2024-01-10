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

package assets

import play.api.libs.json._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve._
import v1.models.nrs.{NrSubmissionId, NrsMetadata, NrsPayload, NrsRetrievalData}

import java.lang.String.format
import java.math.BigInteger
import java.security.MessageDigest.getInstance
import java.time.{Instant, LocalDate, LocalDateTime}
import java.util.UUID

object IntegrationNrsConstants {

  val jsonPayload: String =
    """
  {
    "something": "true"
  }
  """

  val responsePayload: JsValue = Json.toJson(NrSubmissionId(UUID.randomUUID()))

  val nrsInternalIdValue: String                                = "internalId"
  val nrsExternalIdValue: String                                = "externalId"
  val nrsAgentCodeValue: String                                 = "agentCode"
  val nrsCredentials: Option[Credentials]                       = Some(Credentials(providerId = "providerId", providerType = "providerType"))
  val nrsConfidenceLevel: ConfidenceLevel.L500.type             = ConfidenceLevel.L500
  val nrsNinoValue: String                                      = "ninov"
  val nrsSaUtrValue: String                                     = "saUtr"
  val nrsNameValue: Option[Name]                                = Some(Name(Some("name"), Some("lastname")))
  val TWENTY_FIVE: Int                                          = 25
  val nrsDateOfBirth: Option[LocalDate]                         = Some(LocalDate.now().minusYears(TWENTY_FIVE))
  val nrsEmailValue: Option[String]                             = Some("nrsEmailValue")
  val nrsAgentInformationValue: AgentInformation                =
    AgentInformation(Some("agentId"), Some("agentCode"), Some("agentFriendlyName"))
  val nrsGroupIdentifierValue: Option[String]                   = Some("groupIdentifierValue")
  val nrsCredentialRole: Option[User.type]                      = Some(User)
  val nrsMdtpInformation: MdtpInformation                       = MdtpInformation("deviceId", "sessionId")
  val nrsItmpName: Option[ItmpName]                             = Some(ItmpName(Some("givenName"), Some("middleName"), Some("familyName")))
  val nrsItmpAddress: Option[ItmpAddress]                       = Some(
    ItmpAddress(
      Some("line1"),
      Some("line2"),
      Some("line3"),
      Some("line4"),
      Some("line5"),
      Some("postCode"),
      Some("countryName"),
      Some("countryCode")
    )
  )
  val nrsAffinityGroup: Option[AffinityGroup.Organisation.type] = Some(AffinityGroup.Organisation)
  val nrsCredentialStrength: Option[String]                     = Some("STRONG")

  val CURRENT_TIME_IN_MILLIS: Long  = 1530442800000L
  val PREVIOUS_TIME_IN_MILLIS: Long = 1530464400000L
  val NRS_TIMESTAMP_IN_MILLIS: Long = 1530475200000L
  val currentLoginTime: Instant     = Instant.ofEpochSecond(CURRENT_TIME_IN_MILLIS)
  val previousLoginTime: Instant    = Instant.ofEpochSecond(PREVIOUS_TIME_IN_MILLIS)

  val nrsLoginTimes: LoginTimes = LoginTimes(currentLoginTime, Some(previousLoginTime))

  val nrsRetrievalData: NrsRetrievalData = NrsRetrievalData(
    internalId = Some(nrsInternalIdValue),
    externalId = Some(nrsExternalIdValue),
    agentCode = Some(nrsAgentCodeValue),
    optionalCredentials = nrsCredentials,
    confidenceLevel = nrsConfidenceLevel,
    nino = Some(nrsNinoValue),
    saUtr = Some(nrsSaUtrValue),
    optionalName = nrsNameValue,
    dateOfBirth = nrsDateOfBirth,
    email = nrsEmailValue,
    agentInformation = nrsAgentInformationValue,
    groupIdentifier = nrsGroupIdentifierValue,
    credentialRole = nrsCredentialRole,
    mdtpInformation = Some(nrsMdtpInformation),
    optionalItmpName = nrsItmpName,
    itmpDateOfBirth = nrsDateOfBirth,
    optionalItmpAddress = nrsItmpAddress,
    affinityGroup = nrsAffinityGroup,
    credentialStrength = nrsCredentialStrength,
    loginTimes = nrsLoginTimes
  )

  val metadata: NrsMetadata = NrsMetadata(
    businessId = "irr",
    notableEvent = "irr-submission",
    payloadContentType = "application/json",
    payloadSha256Checksum = sha256Hash(jsonPayload),
    userSubmissionTimestamp = LocalDateTime.now().toString,
    identityData = nrsRetrievalData,
    userAuthToken = "Bearer 123",
    headerData = Json.obj(),
    searchKeys = JsObject(Map[String, JsValue]("searchKey" -> JsString("searchValue")))
  )

  val payload: NrsPayload = NrsPayload(
    payload = jsonPayload,
    metadata = metadata
  )

  def sha256Hash(text: String): String =
    format("%064x", new BigInteger(1, getInstance("SHA-256").digest(text.getBytes("UTF-8"))))

  type NrsRetrievalDataType =
    Option[String] ~ Option[String] ~ Option[String] ~ Option[Credentials] ~ ConfidenceLevel ~ Option[String] ~
      Option[String] ~ Option[Name] ~ Option[LocalDate] ~ Option[String] ~ AgentInformation ~ Option[String] ~
      Option[CredentialRole] ~ Option[MdtpInformation] ~ Option[ItmpName] ~ Option[LocalDate] ~ Option[ItmpAddress] ~
      Option[AffinityGroup] ~ Option[String] ~ LoginTimes

  val fakeResponse: NrsRetrievalDataType = new ~(
    new ~(
      new ~(
        new ~(
          new ~(
            new ~(
              new ~(
                new ~(
                  new ~(
                    new ~(
                      new ~(
                        new ~(
                          new ~(
                            new ~(
                              new ~(
                                new ~(
                                  new ~(
                                    new ~(
                                      new ~(Some(nrsInternalIdValue), Some(nrsExternalIdValue)),
                                      Some(nrsAgentCodeValue)
                                    ),
                                    nrsCredentials
                                  ),
                                  nrsConfidenceLevel
                                ),
                                Some(nrsNinoValue)
                              ),
                              Some(nrsSaUtrValue)
                            ),
                            nrsNameValue
                          ),
                          nrsDateOfBirth
                        ),
                        nrsEmailValue
                      ),
                      nrsAgentInformationValue
                    ),
                    nrsGroupIdentifierValue
                  ),
                  nrsCredentialRole
                ),
                Some(nrsMdtpInformation)
              ),
              nrsItmpName
            ),
            nrsDateOfBirth
          ),
          nrsItmpAddress
        ),
        nrsAffinityGroup
      ),
      nrsCredentialStrength
    ),
    nrsLoginTimes
  )

}

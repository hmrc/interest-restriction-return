/*
 * Copyright 2025 HM Revenue & Customs
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

package config

import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import java.util.Base64
import javax.inject.{Inject, Singleton}
import scala.util.{Failure, Success, Try}

@Singleton
class AppConfig @Inject() (servicesConfig: ServicesConfig, configuration: Configuration) {

  lazy val desUrl: String                   = servicesConfig.baseUrl("des")
  lazy val desAuthorisationToken: String    =
    s"Bearer ${servicesConfig.getString("microservice.services.des.authorisation-token")}"
  lazy val desEnvironment: (String, String) =
    "Environment" -> servicesConfig.getString("microservice.services.des.environment")

  lazy val nrsEnabled: Boolean =
    configuration.getOptional[Boolean]("microservice.services.nrs.enabled").getOrElse(false)

  lazy val nrsUrl: Option[String]                =
    if (nrsEnabled) {
      configuration
        .getOptional[Configuration]("microservice.services.nrs")
        .map(_ => servicesConfig.baseUrl("nrs"))
    } else {
      None
    }
  lazy val nrsAuthorisationToken: Option[String] = configuration.getOptional[String]("microservice.services.nrs.apikey")

  lazy val apiGatewayContext: String = servicesConfig.getString("api.gateway.context")

  def apiStatus(version: String): String = servicesConfig.getString(s"api.$version.status")

  def featureSwitch: Option[Configuration] = configuration.getOptional[Configuration]("feature-switch")

  lazy val endpointsEnabled: Boolean = servicesConfig.getBoolean("api-definitions.endpoints.enabled")

  // HIP
  lazy val isHipEnabled: Boolean =
    configuration.getOptional[Boolean]("microservice.services.hip.enabled").getOrElse(false)

  private lazy val hipUrl: Try[String] = Try(servicesConfig.baseUrl("hip"))
  def baseHipUrl: String               = if (isHipEnabled) {
    val finalUrl = hipUrl match {
      case Failure(exception) =>
        throw new RuntimeException(
          s"Failed to retrieve HIP base URL from configuration: ${exception.getMessage}"
        )
      case Success(value)     => value
    }
    s"$finalUrl/cir"
  } else {
    throw new RuntimeException("HIP is not enabled, cannot retrieve base URL.")
  }

  private val clientId: String                   = servicesConfig.getString("microservice.services.hip.clientId")
  private val secret: String                     = servicesConfig.getString("microservice.services.hip.secret")
  def hipAuthorizationToken: String              = Base64.getEncoder.encodeToString(s"$clientId:$secret".getBytes("UTF-8"))
  lazy val hipAppointReportingCompanyUrl: String = s"$baseHipUrl/appoint"
  lazy val hipRevokeReportingCompanyUrl: String  = s"$baseHipUrl/revoke"
  lazy val hipFullReturnUrl: String              = s"$baseHipUrl/return"
  lazy val hipAbbreviatedReturnUrl: String       = s"$baseHipUrl/return/abbreviated"

}

/*
 * Copyright 2021 HM Revenue & Customs
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

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

trait AppConfig {

  def desUrl: String
  def appName: String
  def desEnvironmentHeader: (String, String)
  def desAuthorisationToken: String
  def apiGatewayContext: String
  def apiStatus(version: String): String
  def featureSwitch: Option[Configuration]
  def endpointsEnabled: Boolean
  def nrsUrl: Option[String]
  def nrsAuthorisationToken: Option[String]

}

@Singleton
class AppConfigImpl @Inject()(val servicesConfig: ServicesConfig, configuration: Configuration) extends AppConfig {

  lazy val desUrl: String = servicesConfig.baseUrl("des")
  lazy val desAuthorisationToken: String = s"Bearer ${servicesConfig.getString("microservice.services.des.authorisation-token")}"
  lazy val desEnvironmentHeader: (String, String) = "Environment" -> servicesConfig.getString("microservice.services.des.environment")
  lazy val appName: String = servicesConfig.getString("appName")

  lazy val nrsUrl: Option[String] = configuration.getOptional[Configuration]("microservice.services.nrs").map(_ => servicesConfig.baseUrl("nrs"))
  lazy val nrsAuthorisationToken: Option[String] = configuration.getOptional[String]("microservice.services.nrs.authorisation-token").map(token => s"Bearer $token")

  val apiGatewayContext: String = servicesConfig.getString("api.gateway.context")

  def apiStatus(version: String): String = servicesConfig.getString(s"api.$version.status")

  def featureSwitch: Option[Configuration] = configuration.getOptional[Configuration](s"feature-switch")

  val endpointsEnabled: Boolean = servicesConfig.getBoolean("api-definitions.endpoints.enabled")
}

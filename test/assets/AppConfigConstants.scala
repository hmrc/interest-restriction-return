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

package assets

import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import config.{AppConfig, AppConfigImpl}

object AppConfigConstants {

  def createAppConfig(configMap: Map[String, Any]): AppConfig = {
    val config: Configuration = Configuration.from(configMap)
    val servicesConfig = new ServicesConfig(config)
    new AppConfigImpl(servicesConfig, config)
  }

  val servicesMap = Map(
    "auth" -> Map(
      "host" -> "localhost",
      "port" -> "8500"
    ),
    "des" -> Map(
      "host" -> "localhost",
      "port" -> "9262",
      "environment" -> "dev",
      "authorisation-token" -> "dev"
    )
  )

  val nrsMap = Map(
    "host" -> "localhost",
    "port" -> "1111",
    "authorisation-token" -> "some.token",
    "enabled" -> "true"
  )

  val nrsDisabledMap = Map(
    "enabled" -> "false"
  )


  val servicesMapWithNrs: Map[String, Map[String, String]] = servicesMap + ("nrs" -> nrsMap)
  val servicesMapWithNrsDisabled: Map[String, Map[String, String]] = servicesMap + ("nrs" -> nrsDisabledMap)

  def config(serviceMap: Map[String, Map[String, String]]) = Map(
    "api" -> Map(
      "1.0" -> Map("status" -> "BETA"),
      "gateway.context" -> "organisations/interest-restriction"
    ),
    "api-definitions.endpoints.enabled" -> "true",
    "microservice.services" -> serviceMap
  )

  val appConfigWithNrs = createAppConfig(config(servicesMapWithNrs))
  val appConfigWithNrsDisabled = createAppConfig(config(servicesMap))
  val appConfigWithoutNrs = createAppConfig(config(servicesMap))
  
}

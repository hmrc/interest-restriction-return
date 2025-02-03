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

package definition

import config.AppConfig
import definition.Versions.*
import javax.inject.{Inject, Singleton}
import play.api.Logging

@Singleton
class ApiDefinitionFactory @Inject() (appConfig: AppConfig) extends Logging {

  lazy val definition: Definition =
    Definition(
      api = APIDefinition(
        name = "Interest Restriction Return (IRR)",
        description = "An API for providing Interest Restriction Return data",
        context = appConfig.apiGatewayContext,
        categories = Seq("CORPORATION_TAX"),
        versions = Seq(
          APIVersion(
            version = VERSION_1,
            status = appConfig.apiStatus(VERSION_1),
            endpointsEnabled = appConfig.endpointsEnabled
          )
        )
      )
    )

}

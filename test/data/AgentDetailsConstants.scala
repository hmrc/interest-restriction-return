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

package data

import play.api.libs.json.{JsObject, Json}
import v1.models.AgentDetailsModel

object AgentDetailsConstants {

  val agentName: String = "some agent"

  val agentDetailsJsonMax: JsObject = Json.obj(
    "agentActingOnBehalfOfCompany" -> true,
    "agentName"                    -> agentName
  )

  val agentDetailsModelMax: AgentDetailsModel = AgentDetailsModel(
    agentActingOnBehalfOfCompany = true,
    agentName = Some(agentName)
  )

  val agentDetailsJsonMin: JsObject = Json.obj(
    "agentActingOnBehalfOfCompany" -> false
  )

  val agentDetailsModelMin: AgentDetailsModel = AgentDetailsModel(
    agentActingOnBehalfOfCompany = false,
    agentName = None
  )

  val agentDetailsJsonWhitespaceName: JsObject = Json.obj(
    "agentActingOnBehalfOfCompany" -> true,
    "agentName"                    -> " some agent "
  )
}

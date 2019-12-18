/*
 * Copyright 2019 HM Revenue & Customs
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

package validation

import models.Validation.ValidationResult
import models.{AgentDetailsModel, Validation}
import play.api.libs.json.{JsPath, JsString, Json}

trait AgentDetailsValidator extends BaseValidation {
  import cats.implicits._

  val agentDetailsModel: AgentDetailsModel

  private def validateAgentName(implicit topPath: JsPath): ValidationResult[Option[String]]  = {
    val lengthCheck = if(agentDetailsModel.agentName.fold(true: Boolean){name => name.length >= 1 && name.length <= 160}){
      agentDetailsModel.agentName.validNec
    } else {
      AgentNameLengthError(agentDetailsModel.agentName.get).invalidNec
    }
    val suppliedCheck = (agentDetailsModel.agentActingOnBehalfOfCompany,agentDetailsModel.agentName) match {
      case (true,None) => AgentNameNotSuppliedError().invalidNec
      case (false,Some(name)) => AgentNameSuppliedError(name).invalidNec
      case _ => agentDetailsModel.agentName.validNec
    }

    combineValidationsForField(lengthCheck, suppliedCheck)
  }

  def validate(implicit path: JsPath): ValidationResult[AgentDetailsModel] = validateAgentName.map(_ => agentDetailsModel)
}

case class AgentNameLengthError(name: String)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "Agent name must be between 1-160 characters if supplied"
  val path = topPath \ "agentName"
  val value = JsString(name)
}

case class AgentNameNotSuppliedError(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "Agent name must be supplied if agent is acting on behalf of company"
  val path = topPath \ "agentName"
  val value = Json.obj()
}

case class AgentNameSuppliedError(name: String)(implicit topPath: JsPath) extends Validation {
  val errorMessage: String = "Agent name must not be supplied if agent is not acting on behalf of company"
  val path = topPath \ "agentName"
  val value = JsString(name)
}

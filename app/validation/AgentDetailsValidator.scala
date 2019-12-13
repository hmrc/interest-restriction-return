package validation

import javax.inject.Inject
import models.{AgentDetailsModel, Validation}
import models.Validation.ValidationResult

case object AgentNameLengthError extends Validation {
  def errorMessages: String = "Agent name must be between 1-160 characters if supplied"
}

case object AgentNameNotSuppliedError extends Validation {
  def errorMessages: String = "Agent name must be supplied if agent is acting on behalf of company"
}

case object AgentNameSuppliedError extends Validation {
  def errorMessages: String = "Agent name must not be supplied if agent is not acting on behalf of company"
}

trait AgentDetailsValidator {
  import cats.implicits._

  val agentDetailsModel: AgentDetailsModel

  private def validateAgentActingOnBehalfOfCompany(agentActingOnBehalfOfCompany: Boolean): ValidationResult[Boolean] = {
    agentActingOnBehalfOfCompany.validNec
  }

  private def validateAgentName(agentDetailsModel: AgentDetailsModel): ValidationResult[Option[String]]  = {
    val lengthCheck = if(agentDetailsModel.agentName.fold(true: Boolean){name => name.length >= 1 && name.length <= 160}){
      agentDetailsModel.agentName.validNec
    } else {
      AgentNameLengthError.invalidNec
    }
    val suppliedCheck = (agentDetailsModel.agentActingOnBehalfOfCompany,agentDetailsModel.agentName) match {
      case (true,None) => AgentNameNotSuppliedError.invalidNec
      case (false,Some(_)) => AgentNameSuppliedError.invalidNec
      case _ => agentDetailsModel.agentName.validNec
    }

    if(lengthCheck.toOption.isDefined && suppliedCheck.toOption.isDefined){
      agentDetailsModel.agentName.validNec
    } else {
      lengthCheck.combine(suppliedCheck)
    }
  }

  def validate = {
    (validateAgentActingOnBehalfOfCompany(agentDetailsModel.agentActingOnBehalfOfCompany),
      validateAgentName(agentDetailsModel)).mapN(AgentDetailsModel)
  }
}
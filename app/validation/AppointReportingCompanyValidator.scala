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

import cats.data.{NonEmptyChain, Validated}
import models.Validation.ValidationResult
import models.appointReportingCompany.AppointReportingCompanyModel
import models.{IdentityOfCompanySubmittingModel, UltimateParentModel, Validation}

trait AppointReportingCompanyValidator {

  import cats.implicits._

  val appointReportingCompanyModel: AppointReportingCompanyModel

  private def validateIdentityOfAppointingCompany: ValidationResult[Option[IdentityOfCompanySubmittingModel]] = {
    (appointReportingCompanyModel.isReportingCompanyAppointingItself, appointReportingCompanyModel.identityOfAppointingCompany) match {
      case (true, Some(_)) => IdentityOfAppointingCompanyIsSupplied.invalidNec
      case (false, None) => IdentityOfAppointingCompanyIsNotSupplied.invalidNec
      case _ => appointReportingCompanyModel.identityOfAppointingCompany.validNec
    }
  }

  private def validateUltimateParentCompany: ValidationResult[Option[UltimateParentModel]] = {
    (appointReportingCompanyModel.reportingCompany.sameAsUltimateParent, appointReportingCompanyModel.ultimateParentCompany) match {
      case (true, Some(_)) => UltimateParentCompanyIsSupplied.invalidNec
      case (false, None) => UltimateParentCompanyIsNotSupplied.invalidNec
      case _ => appointReportingCompanyModel.ultimateParentCompany.validNec
    }
  }

  def validate: Validated[NonEmptyChain[Validation], AppointReportingCompanyModel] =
    (validateIdentityOfAppointingCompany, validateUltimateParentCompany).mapN((_,_) => appointReportingCompanyModel)
}

case object IdentityOfAppointingCompanyIsNotSupplied extends Validation {
  def errorMessages: String = "Identity of Appointing Company must be supplied if it is not the same as the reporting company or agent"
}

case object IdentityOfAppointingCompanyIsSupplied extends Validation {
  def errorMessages: String = "Identity of Appointing Company must not be supplied if it is the same as the reporting company or agent"
}

case object UltimateParentCompanyIsSupplied extends Validation {
  def errorMessages: String = "Ultimate Parent Company must not be supplied if it is the same as the reporting company"
}

case object UltimateParentCompanyIsNotSupplied extends Validation {
  def errorMessages: String = "Ultimate Parent Company must be supplied if it is not the same as the reporting company"
}











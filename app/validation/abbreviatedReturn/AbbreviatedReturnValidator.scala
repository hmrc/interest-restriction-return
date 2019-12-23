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

package validation.abbreviatedReturn

import models.Validation.ValidationResult
import models.abbreviatedReturn.AbbreviatedReturnModel
import models.{Original, ParentCompanyModel, Revised, Validation}
import play.api.libs.json.{JsPath, Json}
import validation.BaseValidation

trait AbbreviatedReturnValidator extends BaseValidation {

  import cats.implicits._

  val abbreviatedReturnModel: AbbreviatedReturnModel

  private def validateRevisedReturnDetails: ValidationResult[Option[String]] = {
    (abbreviatedReturnModel.submissionType, abbreviatedReturnModel.revisedReturnDetails) match {
      case (Original, Some(details)) => RevisedReturnDetailsSupplied(details).invalidNec
      case (Revised, None) => RevisedReturnDetailsNotSupplied.invalidNec
      case _ => abbreviatedReturnModel.revisedReturnDetails.validNec
    }
  }

  private def validateParentCompany: ValidationResult[Option[String]] = {
    (abbreviatedReturnModel.reportingCompany.sameAsUltimateParent, abbreviatedReturnModel.parentCompany) match {
      case (true, Some(details)) => ParentCompanyDetailsSupplied(details).invalidNec
      case (false, None) => ParentCompanyDetailsNotSupplied.invalidNec
      case _ => abbreviatedReturnModel.revisedReturnDetails.validNec
    }
  }

  def validate: ValidationResult[AbbreviatedReturnModel] = {

    val validatedUkCompanies = abbreviatedReturnModel.ukCompanies.zipWithIndex.map {
      case (a, i) => a.validate(JsPath \ s"ukCompanies[$i]")
    }

    (
      abbreviatedReturnModel.agentDetails.validate(JsPath \ "agentDetails"),
      abbreviatedReturnModel.reportingCompany.validate(JsPath \ "reportingCompany"),
      optionValidations(abbreviatedReturnModel.parentCompany.map(_.validate(JsPath \ "parentCompany"))),
      abbreviatedReturnModel.groupCompanyDetails.validate(JsPath \ "groupCompanyDetails"),
      optionValidations(abbreviatedReturnModel.groupLevelElections.map(_.validate(JsPath \ "groupLevelElections"))),
      combineValidations(validatedUkCompanies:_*),
      validateParentCompany,
      validateRevisedReturnDetails
    ).mapN((_,_,_,_,_,_,_,_) => abbreviatedReturnModel)
  }
}

case object RevisedReturnDetailsNotSupplied extends Validation {
  val errorMessage: String = "A description of the amendments made to the return must be supplied if this is a revised return"
  val path = JsPath \ "revisedReturnDetails"
  val value = Json.obj()
}

case class RevisedReturnDetailsSupplied(details: String) extends Validation {
  val errorMessage: String = "A description of the amendments made to the return cannot be supplied if this is an original return"
  val path = JsPath \ "revisedReturnDetails"
  val value = Json.toJson(details)
}

case object ParentCompanyDetailsNotSupplied extends Validation {
  val errorMessage: String = "Parent Company is required if the Reporting Company is not the same as the Ultimate Parent"
  val path = JsPath \ "parentCompany"
  val value = Json.obj()
}

case class ParentCompanyDetailsSupplied(parentCompany: ParentCompanyModel) extends Validation {
  val errorMessage: String = "Parent Company should not be supplied as the parent is the same as the Reporting Company"
  val path = JsPath \ "parentCompany"
  val value = Json.toJson(parentCompany)
}


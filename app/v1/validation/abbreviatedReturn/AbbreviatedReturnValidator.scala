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

package v1.validation.abbreviatedReturn

import play.api.libs.json.JsPath
import v1.models.Validation.ValidationResult
import v1.models.abbreviatedReturn.AbbreviatedReturnModel
import v1.models.{Original, Revised}
import v1.validation.BaseValidation
import v1.validation.errors._

trait AbbreviatedReturnValidator extends BaseValidation {

  import cats.implicits._

  val abbreviatedReturnModel: AbbreviatedReturnModel

  private def validateRevisedReturnDetails: ValidationResult[_] =
    (abbreviatedReturnModel.submissionType, abbreviatedReturnModel.revisedReturnDetails) match {
      case (Original, Some(details)) => RevisedReturnDetailsSupplied(details).invalidNec
      case (Revised, None)           => RevisedReturnDetailsNotSupplied.invalidNec
      case (Revised, Some(details))  => details.validate(JsPath)
      case _                         => abbreviatedReturnModel.revisedReturnDetails.validNec
    }

  private def validateParentCompany: ValidationResult[Boolean] =
    (abbreviatedReturnModel.reportingCompany.sameAsUltimateParent, abbreviatedReturnModel.parentCompany) match {
      case (true, Some(details)) => ParentCompanyDetailsSupplied(details).invalidNec
      case (false, None)         => ParentCompanyDetailsNotSupplied.invalidNec
      case _                     => abbreviatedReturnModel.appointedReportingCompany.validNec
    }

  private def validateAppointedReporter: ValidationResult[Boolean] =
    if (abbreviatedReturnModel.appointedReportingCompany) {
      abbreviatedReturnModel.appointedReportingCompany.validNec
    } else {
      ReportingCompanyNotAppointed.invalidNec
    }

  private def validateDeclaration: ValidationResult[Boolean] =
    abbreviatedReturnModel.declaration match {
      case true  => abbreviatedReturnModel.declaration.validNec
      case false => ReturnDeclarationError(abbreviatedReturnModel.declaration).invalidNec
    }

  def validate: ValidationResult[AbbreviatedReturnModel] = {

    val validatedUkCompanies =
      if (abbreviatedReturnModel.ukCompanies.isEmpty) {
        UkCompaniesEmpty.invalidNec
      } else {
        combineValidations(abbreviatedReturnModel.ukCompanies.zipWithIndex.map { case (a, i) =>
          a.validate(JsPath \ s"ukCompanies[$i]")
        }: _*)
      }

    combineValidations(
      abbreviatedReturnModel.agentDetails.validate(JsPath \ "agentDetails"),
      abbreviatedReturnModel.reportingCompany.validate(JsPath \ "reportingCompany"),
      optionValidations(abbreviatedReturnModel.parentCompany.map(_.validate(JsPath \ "parentCompany"))),
      abbreviatedReturnModel.groupCompanyDetails.validate(JsPath \ "groupCompanyDetails"),
      optionValidations(abbreviatedReturnModel.groupLevelElections.map(_.validate(JsPath \ "groupLevelElections"))),
      validatedUkCompanies,
      validateParentCompany,
      validateRevisedReturnDetails,
      validateAppointedReporter,
      validateDeclaration
    ).map(_ => abbreviatedReturnModel)
  }
}

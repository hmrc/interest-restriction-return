/*
 * Copyright 2020 HM Revenue & Customs
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

package v1.validation

import config.Constants
import play.api.libs.json.{JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.{GroupCompanyDetailsModel, Validation}

trait GroupCompanyDetailsValidator extends BaseValidation {

  import cats.implicits._

  val groupCompanyDetails: GroupCompanyDetailsModel

  private def validateTotalCompanies(implicit path: JsPath): ValidationResult[Int] = {
    val companies = groupCompanyDetails.totalCompanies
    if(companies >= 1 && companies <= Constants.intMax) {
      companies.validNec
    } else {
      GroupCompanyDetailsTotalCompaniesError(companies).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[GroupCompanyDetailsModel] =
    (validateTotalCompanies,
      groupCompanyDetails.accountingPeriod.validate(path \ "accountingPeriod")
      ).mapN((_,_) => groupCompanyDetails)
}

case class GroupCompanyDetailsTotalCompaniesError(totalCompanies: Int)(implicit topPath: JsPath) extends Validation {
  val code = NEGATIVE_AMOUNT
  val path = topPath \ "totalCompanies"
  val message: String =
    s"totalCompanies is ${totalCompanies} but it must be greater than 0 and less than ${Constants.intMax}"
  }
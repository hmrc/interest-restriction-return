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
import models.{IdentityOfCompanySubmittingModel, Validation}
import play.api.libs.json.{JsPath, Json}

trait IdentityOfCompanySubmittingValidator {

  import cats.implicits._

  val identityOfCompanySubmitting: IdentityOfCompanySubmittingModel

  private def validateIdentityOfCompanySubmitting(implicit path: JsPath): ValidationResult[IdentityOfCompanySubmittingModel] = {
    val isUk = identityOfCompanySubmitting.ctutr.isDefined || identityOfCompanySubmitting.crn.isDefined
    val isNonUk = identityOfCompanySubmitting.countryOfIncorporation.isDefined || identityOfCompanySubmitting.nonUkCrn.isDefined

    if(isUk && isNonUk) {
      CannotBeUkAndNonUk(identityOfCompanySubmitting).invalidNec
    } else {
      identityOfCompanySubmitting.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[IdentityOfCompanySubmittingModel] =
    validateIdentityOfCompanySubmitting.map(_ => identityOfCompanySubmitting)
}

case class CannotBeUkAndNonUk(companySubmitting: IdentityOfCompanySubmittingModel)(implicit val path: JsPath) extends Validation {
  val errorMessage: String = "Identity of company submitting cannot contain data for UK and NonUK fields"
  val value = Json.toJson(companySubmitting)
}








/*
 * Copyright 2022 HM Revenue & Customs
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

import play.api.libs.json.{JsPath, Json}
import v1.models.Validation.ValidationResult
import v1.models.{IdentityOfCompanySubmittingModel, Validation}

trait IdentityOfCompanySubmittingValidator extends BaseValidation {

  import cats.implicits._

  val identityOfCompanySubmitting: IdentityOfCompanySubmittingModel

  private def validateIdentityOfCompanySubmitting(implicit path: JsPath): ValidationResult[IdentityOfCompanySubmittingModel] = {
    val isUk = identityOfCompanySubmitting.ctutr.isDefined
    val isNonUk = identityOfCompanySubmitting.countryOfIncorporation.isDefined

    if(isUk && isNonUk) {
      CannotBeUkAndNonUk(identityOfCompanySubmitting).invalidNec
    } else {
      identityOfCompanySubmitting.validNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[IdentityOfCompanySubmittingModel] =
    (validateIdentityOfCompanySubmitting,
      identityOfCompanySubmitting.companyName.validate(path \ "companyName"),
      optionValidations(identityOfCompanySubmitting.ctutr.map(_.validate(path \ "ctutr"))),
      optionValidations(identityOfCompanySubmitting.countryOfIncorporation.map(_.validate(path \ "countryOfIncorporation"))),
      optionValidations(identityOfCompanySubmitting.legalEntityIdentifier.map(_.validate(path \ "legalEntityIdentifier")))
      ).mapN((_,_,_,_,_) => identityOfCompanySubmitting)
}

case class CannotBeUkAndNonUk(companySubmitting: IdentityOfCompanySubmittingModel)(implicit val path: JsPath) extends Validation {
  val code = "IDENTITY_CTUTR_COUNTRY"
  val errorMessage: String = "Company submitting cannot send both a CTUTR and a country of incorporation"
  val value = Some(Json.toJson(companySubmitting))
}








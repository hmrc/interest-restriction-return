/*
 * Copyright 2021 HM Revenue & Customs
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

import play.api.libs.json.JsPath
import v1.models.AuthorisingCompanyModel
import v1.models.Validation.ValidationResult

trait AuthorisingCompanyValidator extends BaseValidation {

  import cats.implicits._

  val authorisingCompanyModel: AuthorisingCompanyModel

  def validate(implicit path: JsPath): ValidationResult[AuthorisingCompanyModel] =
    (authorisingCompanyModel.companyName.validate(path \ "companyName"),
      authorisingCompanyModel.utr.validate(path \ "utr")
      ).mapN((_,_) => authorisingCompanyModel)
}
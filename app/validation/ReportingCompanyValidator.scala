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

import models.ReportingCompanyModel
import models.Validation.ValidationResult
import play.api.libs.json.JsPath

trait ReportingCompanyValidator extends BaseValidation {

  import cats.implicits._

  val reportingCompanyModel: ReportingCompanyModel

  def validate(implicit path: JsPath): ValidationResult[ReportingCompanyModel] =
    (reportingCompanyModel.ctutr.validate(path \ "ctutr"),
      optionValidations(reportingCompanyModel.crn.map(_.validate(path \ "crn"))))
      .mapN((_,_) => reportingCompanyModel)
}
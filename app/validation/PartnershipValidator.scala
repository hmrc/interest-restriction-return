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

package validation

import models.Validation.ValidationResult
import models.{PartnershipModel, Validation}
import play.api.libs.json.{JsPath, Json}

trait PartnershipValidator extends BaseValidation {

  import cats.implicits._

  val partnershipModel: PartnershipModel

  private def validatePartnershipName(implicit path: JsPath): ValidationResult[String] = {
    if (partnershipModel.partnershipName.length >= 1 && partnershipModel.partnershipName.length <= 32767) {
      partnershipModel.partnershipName.validNec
    } else {
      PartnershipNameError(partnershipModel.partnershipName).invalidNec
    }
  }

  def validate(implicit path: JsPath): ValidationResult[PartnershipModel] = {
    validatePartnershipName.map(_ => partnershipModel)
  }
}

case class PartnershipNameError(partnershipName: String)(implicit val topPath: JsPath) extends Validation {
  val errorMessage: String = "Partnership Names must not be more than 32767 characters"
  val path = topPath \ "investorGroups"
  val value = Json.toJson(partnershipName)
}






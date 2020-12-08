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

package v1.desValidation

import assets.fullReturn.FullReturnConstants._
import assets.fullReturn.UkCompanyConstants._
import play.api.libs.json.{JsPath, Json}
import utils.BaseSpec
import v1.models.CompanyNameModel
import play.api.Logging

class FullReturnSchemaValidationSpec extends BaseSpec with ValidationMatchers with Logging {

  implicit val path = JsPath \ "some" \ "path"

  "ukCompanies" should {
    "fail validation when a company name is invalid" in {
      val invalidName = CompanyNameModel("New!£$%^&*()_ComPan\n with spacs Ā to ʯ, Ḁ to ỿ :' ₠ to ₿ Å and K lenth is 160 characters no numbers allowed New!£$%^&*()_ComPany with spaces Ā to ʯ, Ḁ to ỿ")
      val model = fullReturnUltimateParentModel.copy(ukCompanies = Seq(ukCompanyModelReactivationMax.copy(companyName = invalidName), ukCompanyModelReactivationMax))
      model.validate should coverSchemaValidation("submit_full_irr.json", "v1", Json.toJson(model))
    }

  }

}

/*
 * Copyright 2025 HM Revenue & Customs
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

package v1.models.abbreviatedReturn

import data.abbreviatedReturn.AbbreviatedReturnConstants.*
import data.AgentDetailsConstants.*
import data.GroupCompanyDetailsConstants.*
import data.ReportingCompanyConstants.*
import play.api.libs.json.{JsError, JsObject, Json, Writes}
import v1.models.{Original, RevisedReturnDetailsModel, SubmissionType}
import data.abbreviatedReturn.UkCompanyConstants.*
import utils.{BaseSpec, JsonFormatters}

class AbbreviatedReturnModelSpec extends BaseSpec with JsonFormatters {

  implicit val abbreviatedFormatter: Writes[AbbreviatedReturnModel] = abbreviatedReturnWrites

  "AbbreviatedReturnModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = withoutAppointedReportingCompany(abbreviatedReturnUltimateParentJson)
        val actualValue   = Json.toJson(abbreviatedReturnUltimateParentModel)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = withoutAppointedReportingCompany(abbreviatedReturnJsonMin)
        val actualValue   = Json.toJson(abbreviatedReturnModelMin)

        actualValue shouldBe expectedValue
      }

      "All data is included in the Json.toJson" in {
        val expectedValue = withoutAppointedReportingCompany(abbreviatedReturnJsonMin)
        val actualValue   = Json.obj(
          "declaration"          -> true,
          "agentDetails"         -> agentDetailsJsonMin,
          "reportingCompany"     -> reportingCompanyJson,
          "publicInfrastructure" -> true,
          "groupCompanyDetails"  -> groupCompanyDetailsJson,
          "submissionType"       -> Json.toJson[SubmissionType](Original),
          "ukCompanies"          -> Seq(ukCompanyJson),
          "numberOfUkCompanies"  -> 1
        )
        actualValue shouldBe expectedValue
      }
    }

    "not pass the appointedReportingCompany boolean" when {
      "writing json" in {
        val parsedJson: JsObject                             = Json.toJson(abbreviatedReturnUltimateParentModel).as[JsObject]
        val appointedReportingCompanyOption: Option[Boolean] = (parsedJson \ "appointedReportingCompany").asOpt[Boolean]
        appointedReportingCompanyOption shouldBe None
      }
    }

    "correctly read from Json" when {

      "max values given" in {

        val expectedValue = abbreviatedReturnUltimateParentModel
        val actualValue   = abbreviatedReturnUltimateParentJson.as[AbbreviatedReturnModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = abbreviatedReturnModelMin
        val actualValue   = abbreviatedReturnJsonMin.as[AbbreviatedReturnModel]

        actualValue shouldBe expectedValue
      }
    }

    "fail to read from Json" when {

      "empty json" in {
        Json.obj().validate[AbbreviatedReturnModel] shouldBe a[JsError]
      }
      "there is a type mismatch" in {
        Json.arr(abbreviatedReturnUltimateParentJson).validate[AbbreviatedReturnModel] shouldBe a[JsError]
      }
    }
    "deriving the publicInfrastructure" when {

      "there is a single company with qic set to true" in {
        val company           = ukCompanyModel.copy(qicElection = true)
        val abbreviatedReturn = abbreviatedReturnModelMin.copy(ukCompanies = Seq(company))

        abbreviatedReturn.publicInfrastructure shouldBe true
      }

      "there is a single company with qic set to false" in {
        val company           = ukCompanyModel.copy(qicElection = false)
        val abbreviatedReturn = abbreviatedReturnModelMin.copy(ukCompanies = Seq(company))

        abbreviatedReturn.publicInfrastructure shouldBe false
      }

      "there are multiple companies with qic set to true" in {
        val company           = ukCompanyModel.copy(qicElection = true)
        val abbreviatedReturn = abbreviatedReturnModelMin.copy(ukCompanies = Seq(company, company, company))

        abbreviatedReturn.publicInfrastructure shouldBe true
      }

      "there are multiple companies with qic set to false" in {
        val company           = ukCompanyModel.copy(qicElection = false)
        val abbreviatedReturn = abbreviatedReturnModelMin.copy(ukCompanies = Seq(company, company, company))

        abbreviatedReturn.publicInfrastructure shouldBe false
      }

      "there are companies with a mixture of qic to set to true and false" in {
        val trueCompany       = ukCompanyModel.copy(qicElection = true)
        val falseCompany      = ukCompanyModel.copy(qicElection = false)
        val abbreviatedReturn =
          abbreviatedReturnModelMin.copy(ukCompanies = Seq(trueCompany, falseCompany, falseCompany))

        abbreviatedReturn.publicInfrastructure shouldBe true
      }
    }
  }

  def withoutAppointedReportingCompany(json: JsObject): JsObject = json - "appointedReportingCompany"

}

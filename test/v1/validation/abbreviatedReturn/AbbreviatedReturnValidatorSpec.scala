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

package v1.validation.abbreviatedReturn

import data.AgentDetailsConstants.*
import data.ParentCompanyConstants.*
import data.ReportingCompanyConstants.*
import data.abbreviatedReturn.AbbreviatedReturnConstants.*
import data.abbreviatedReturn.UkCompanyConstants.*
import play.api.libs.json.JsPath
import utils.BaseSpec
import v1.models.abbreviatedReturn.AbbreviatedReturnModel
import v1.models.{Original, Revised, RevisedReturnDetailsModel}
import v1.validation.*
import v1.validation.errors.*

class AbbreviatedReturnValidatorSpec extends BaseSpec {

  implicit val path: JsPath = JsPath \ "some" \ "path"

  "AbbreviatedReturnValidator" when {
    ".validate" should {
      "return the valid model" when {
        "a valid abbreviated return with submissionType 'Revised' and some details for a revision are supplied" in {
          rightSide(abbreviatedReturnUltimateParentModel.validate) shouldBe abbreviatedReturnUltimateParentModel
        }

        "a valid abbreviated return with submissionType 'Original' and no details for a revision are supplied" in {
          rightSide(
            abbreviatedReturnUltimateParentModel
              .copy(
                submissionType = Original,
                revisedReturnDetails = None
              )
              .validate
          ) shouldBe abbreviatedReturnUltimateParentModel.copy(submissionType = Original, revisedReturnDetails = None)
        }
      }

      "return appropriate error message" when {
        "submissionType 'Original' and some details for a revision are supplied" in {
          leftSideError(
            abbreviatedReturnUltimateParentModel
              .copy(
                submissionType = Original,
                revisedReturnDetails = Some(RevisedReturnDetailsModel("Revision"))
              )
              .validate
          ).errorMessage shouldBe RevisedReturnDetailsSupplied(RevisedReturnDetailsModel("Revision")).errorMessage
        }

        "submissionType 'Revised' and no details for a revision are supplied" in {
          leftSideError(
            abbreviatedReturnUltimateParentModel
              .copy(
                submissionType = Revised,
                revisedReturnDetails = None
              )
              .validate
          ).errorMessage shouldBe RevisedReturnDetailsNotSupplied.errorMessage
        }

        "Reporting Company is the same as UPC but Parent Details are supplied" in {
          leftSideError(
            abbreviatedReturnUltimateParentModel
              .copy(
                reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = true),
                parentCompany = Some(parentCompanyModelUltUkCompany)
              )
              .validate
          ).errorMessage shouldBe ParentCompanyDetailsSupplied(parentCompanyModelUltUkCompany).errorMessage
        }

        "Reporting Company is the same as deemed but Parent Details are supplied" in {
          leftSideError(
            abbreviatedReturnDeemedParentModel
              .copy(
                reportingCompany = reportingCompanyModel.copy(sameAsUltimateParent = true),
                parentCompany = Some(parentCompanyModelDeemedUkCompany)
              )
              .validate
          ).errorMessage shouldBe ParentCompanyDetailsSupplied(parentCompanyModelUltUkCompany).errorMessage
        }

        "Reporting Company is not the same as UPC and UPC is not supplied" in {
          leftSideError(
            abbreviatedReturnUltimateParentModel
              .copy(
                reportingCompany = reportingCompanyModel,
                parentCompany = None
              )
              .validate
          ).errorMessage shouldBe ParentCompanyDetailsNotSupplied.errorMessage
        }

        "Agent details are invalid" in {
          leftSideError(
            abbreviatedReturnUltimateParentModel
              .copy(agentDetails = agentDetailsModelMax.copy(agentName = None))
              .validate
          ).errorMessage shouldBe AgentNameNotSuppliedError().errorMessage
        }

        "Reporting Company details are invalid" in {
          leftSideError(
            abbreviatedReturnUltimateParentModel
              .copy(reportingCompany = reportingCompanyModel.copy(companyName = companyNameTooLong))
              .validate
          ).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
        }

        "Parent Company is invalid" in {
          leftSideError(
            abbreviatedReturnUltimateParentModel.copy(parentCompany = Some(parentCompanyModelMax)).validate
          ).errorMessage shouldBe
            ParentCompanyCanNotBeUltimateAndDeemed(parentCompanyModelMax).errorMessage
        }

        "it is not the appointed reporting company" in {
          leftSideError(
            abbreviatedReturnUltimateParentModel.copy(appointedReportingCompany = false).validate
          ).errorMessage shouldBe ReportingCompanyNotAppointed.errorMessage
        }

        "Uk Company details are empty" in {
          leftSideError(
            abbreviatedReturnUltimateParentModel.copy(ukCompanies = Seq()).validate
          ).errorMessage shouldBe UkCompaniesEmpty.errorMessage
        }

        "Uk Company details are invalid" in {
          leftSideError(
            abbreviatedReturnUltimateParentModel
              .copy(
                ukCompanies = Seq(ukCompanyModel.copy(companyName = companyNameTooLong))
              )
              .validate
          ).errorMessage shouldBe CompanyNameLengthError(companyNameTooLong.name).errorMessage
        }

        "submissionType 'Revised' and the revised return details are less than 1 character long" in {
          leftSideError(
            abbreviatedReturnUltimateParentModel
              .copy(
                submissionType = Revised,
                revisedReturnDetails = Some(RevisedReturnDetailsModel(""))
              )
              .validate
          ).errorMessage shouldBe RevisedReturnDetailsLengthError("").errorMessage
        }

        "submissionType 'Revised' and the revised return details are more than 5000 character longs" in {
          leftSideError(
            abbreviatedReturnUltimateParentModel
              .copy(
                submissionType = Revised,
                revisedReturnDetails = Some(RevisedReturnDetailsModel("a" * 5001))
              )
              .validate
          ).errorMessage shouldBe RevisedReturnDetailsLengthError("a" * 5001).errorMessage
        }

        "submissionType 'Revised' and the revised return details contains invalid characters" in {
          val returnDetails: String =
            "ʰʲʺ£$%˦˫qwNew!£$%^&*()_ComPan\n with spacs Ā to ʯ, Ḁ to ỿ :' ₠ to ₿ Å and K lenth is 160" +
              " no numbers allowed New!£$%^&*()_ComPany with spaces Ā to ʯ, Ḁ to ỿ"
          leftSideError(
            abbreviatedReturnUltimateParentModel
              .copy(
                submissionType = Revised,
                revisedReturnDetails = Some(RevisedReturnDetailsModel(returnDetails))
              )
              .validate
          ).errorMessage shouldBe RevisedReturnDetailsCharacterError(returnDetails).errorMessage
        }

        "declaration is false" in {
          val model: AbbreviatedReturnModel = abbreviatedReturnUltimateParentModel.copy(declaration = false)
          leftSideError(model.validate).errorMessage shouldBe ReturnDeclarationError(false).errorMessage
        }
      }
    }
  }
}

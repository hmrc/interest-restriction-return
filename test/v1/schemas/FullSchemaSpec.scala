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

package v1.schemas

import play.api.libs.json.{JsValue, Json}
import v1.schemas.helpers.fullReturn.{FullReturnModel, UkCompanyFull}

class FullSchemaSpec extends BaseSchemaSpec {

  val validFullReturn = FullReturnModel(ukCompanies = Some(Seq(UkCompanyFull(allocatedReactivations = None))))

  def validate(json: JsValue): Boolean = validateJson("fullReturnSchema.json", "1.0", json)

  "FullReturn Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON received" in {

        val json = Json.toJson(validFullReturn)

        validate(json) shouldBe true
      }

      "ANGIE is None" in {

          val json = Json.toJson(validFullReturn.copy(angie = None))

          validate(json) shouldBe true
        }


      "Revised Return Details is None" in {

        val json = Json.toJson(validFullReturn.copy(revisedReturnDetails = None))

        validate(json) shouldBe true
      }

      "adjustedGroupInterest is None" in {

        val json = Json.toJson(validFullReturn.copy(adjustedGroupInterest = None))

        validate(json) shouldBe true
      }

      "parentCompany is empty" in {

        val json = Json.toJson(validFullReturn.copy(parentCompany = None))

        validate(json) shouldBe true
      }
    }

    "Return invalid" when {

      "groupLevelElections is empty" in {

        val json = Json.toJson(FullReturnModel(groupLevelElections = None))

        validate(json) shouldBe false
      }

      "agentDetails is empty" in {

        val json = Json.toJson(FullReturnModel(agentDetails = None))

        validate(json) shouldBe false
      }

      "reportingCompany is empty" in {

        val json = Json.toJson(FullReturnModel(reportingCompany = None))

        validate(json) shouldBe false
      }

      "publicInfrastructure is empty" in {

        val json = Json.toJson(FullReturnModel(publicInfrastructure = None))

        validate(json) shouldBe false
      }

      "groupCompanyDetails is empty" in {

        val json = Json.toJson(FullReturnModel(groupCompanyDetails = None))

        validate(json) shouldBe false
      }

      "SubmissionType" when {

        "is None" in {

          val json = Json.toJson(FullReturnModel(submissionType = None))

          validate(json) shouldBe false
        }

        "is supplied but empty" in {

          val json = Json.toJson(FullReturnModel(submissionType = Some("")))

          validate(json) shouldBe false
        }
      }

      "Revised Return Details" when {

        "is supplied but empty" in {

          val json = Json.toJson(FullReturnModel(revisedReturnDetails = Some("")))

          validate(json) shouldBe false
        }

        s"exceeds the maximum description length of ${maxDescriptionLength}" in {

          val json = Json.toJson(FullReturnModel(
            revisedReturnDetails = Some("A" * (maxDescriptionLength + 1))))

          validate(json) shouldBe false
        }
      }

      "UK Companies" when {

        "is None" in {

          val json = Json.toJson(FullReturnModel(ukCompanies = None))

          validate(json) shouldBe false
        }

        "is supplied but empty" in {

          val json = Json.toJson(FullReturnModel(ukCompanies = Some(Seq.empty)))

          validate(json) shouldBe false
        }
      }

      "ANGIE" when {

        "is negative" in {

          val json = Json.toJson(FullReturnModel(angie = Some(-0.11)))

          validate(json) shouldBe false
        }
      }

      "Return Contains Estimate" in{

        val json = Json.toJson(FullReturnModel(returnContainsEstimates = None))

        validate(json) shouldBe false

      }

      "Group Subject To Interest Restrictions" when {

        "groupSubjectToInterestRestrictions is None" in {

          val json = Json.toJson(FullReturnModel(groupSubjectToInterestRestrictions = None))

          validate(json) shouldBe false
        }
      }

      "Group Subject To Interest Reactivations" when {

        "groupSubjectToInterestReactivation is None" in {

          val json = Json.toJson(FullReturnModel(groupSubjectToInterestReactivation = None))

          validate(json) shouldBe false
        }
      }

      "groupLevelAmount is None" in {

        val json = Json.toJson(FullReturnModel(groupLevelAmount = None))

        validate(json) shouldBe false
      }

      "totalReactivation" when {

        "is None" in {

          val json = Json.toJson(FullReturnModel(
            totalReactivation = None))

          validate(json) shouldBe false
        }

        "is less than 0" in {

          val json = Json.toJson(FullReturnModel(
            totalReactivation = Some(-0.01)))

          validate(json) shouldBe false
        }
      }

      "totalRestrictions" when {

        "is None" in {

          val json = Json.toJson(FullReturnModel(
            totalRestrictions = None))

          validate(json) shouldBe false
        }

        "is less than 0" in {

          val json = Json.toJson(FullReturnModel(
            totalRestrictions = Some(-0.01)))

          validate(json) shouldBe false
        }
      }

    }
  }
}
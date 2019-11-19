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

package schemas

import play.api.libs.json.{JsValue, Json}

//noinspection ScalaStyle
class AbbreviatedReturnSchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean =
    validateJson("abbreviatedReturnSchema.json", json)

  "AbbreviatedReturn Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload with UK Parent company" in {

        val json = Json.toJson(AbbreviatedReturnModel())

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with NonUK Parent company" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          parentCompany = Some(ParentCompany(Some(NonUkCompany())))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with 3 Deemed Parent companies" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          parentCompany = Some(ParentCompany(ultimateParent = None, deemedParent = Some(Seq(
            DeemedParent(), DeemedParent(), DeemedParent()
          ))))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload submissionType is revised" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          submissionType = Some("revised")
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload revisedReturnDetails is None" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          revisedReturnDetails = None
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload groupEBITDAChargeableGains is None" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            groupEBITDAChargeableGains = None
          ))))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload interestAllowanceAlternativeCalculation is None" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            interestAllowanceAlternativeCalculation = None
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload groupRatioElection election is revoke" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            groupRatioElection = Some(revokeString)
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload groupRatioBlended election is revoke" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            groupRatioBlended = Some(GroupRatioBlended(
              election = Some(revokeString)
            ))
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload nonConsolidatedInvestments election is revoke" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
              election = Some(revokeString)
            ))
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload interestAllowanceConsolidatedPartnership election is false" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            interestAllowanceConsolidatedPartnership = Some(InterestAllowanceConsolidatedPartnership(
              election = Some(false)
            ))
          ))
        ))

        validate(json) shouldBe true
      }
    }

    "Return invalid" when {

      "agentDetails" when {

        "is empty" in {

          val json = Json.toJson(AbbreviatedReturnModel(
            agentDetails = None
          ))

          validate(json) shouldBe false
        }
      }

      "parent company is none" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          parentCompany = None
        ))

        validate(json) shouldBe false
      }

      "reporting company ultimate parent is empty" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          isReportingCompanyUltimateParent = None
        ))

        validate(json) shouldBe false
      }

      "groupCompanyDetails" when {

        "is None" in {

          val json = Json.toJson(AbbreviatedReturnModel(
            groupCompanyDetails = None
          ))

          validate(json) shouldBe false
        }
      }

      "submissionType" when {

        "is not a valid type" in {

          val json = Json.toJson(AbbreviatedReturnModel(
            submissionType = Some("invalid")
          ))

          validate(json) shouldBe false
        }

        "is empty" in {

          val json = Json.toJson(AbbreviatedReturnModel(
            submissionType = Some("")
          ))

          validate(json) shouldBe false
        }

        "is None" in {

          val json = Json.toJson(AbbreviatedReturnModel(
            submissionType = None
          ))

          validate(json) shouldBe false
        }
      }

      "revisedReturnDetails" when {

        "is empty" in {

          val json = Json.toJson(AbbreviatedReturnModel(
            revisedReturnDetails = Some("")
          ))

          validate(json) shouldBe false
        }
      }

      "ukCompanies" when {

        "is empty Sequence" in {

          val json = Json.toJson(AbbreviatedReturnModel(
            ukCompanies = Some(Seq.empty)
          ))

          validate(json) shouldBe false
        }

        "is None" in {

          val json = Json.toJson(AbbreviatedReturnModel(
            ukCompanies = None
          ))

          validate(json) shouldBe false
        }
      }
    }
  }
}

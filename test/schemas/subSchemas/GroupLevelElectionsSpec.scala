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

package schemas.subSchemas

import play.api.libs.json.{JsValue, Json}
import schemas.BaseSchemaSpec
import schemas.helpers._

class GroupLevelElectionsSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean =
    validateJson("subSchemas/groupLevelElections.json", json)

  "groupLevelElections" when {

    "return valid" when {

      "supplied with valid JSON" in {

        val json = Json.toJson(GroupLevelElections())

        validate(json) shouldBe true
      }
    }

    "return invalid" when {

      "groupRatio" when {

        "is None" in {

          val json = Json.toJson(GroupLevelElections(
            groupRatioElection = None
          ))

          validate(json) shouldBe false
        }
      }

      "groupRatioBlended" when {

        "election" when {

          "is None" in {

            val json = Json.toJson(GroupLevelElections(
              groupRatioBlended = Some(GroupRatioBlended(
                election = None
              ))
            ))

            validate(json) shouldBe false
          }
        }

        "investorGroups" when {

          "groupName" when {

            "contains a seq which includes an empty string" in {

              val json = Json.toJson(GroupLevelElections(
                groupRatioBlended = Some(GroupRatioBlended(
                  investorGroups = Some(Seq(
                    InvestorGroup(
                      groupName = Some("Group A")
                    ),
                    InvestorGroup(
                      groupName = Some("")
                    )
                  ))
                ))
              ))

              validate(json) shouldBe false
            }

            "is None" in {

              val json = Json.toJson(GroupLevelElections(
                groupRatioBlended = Some(GroupRatioBlended(
                  investorGroups = Some(Seq(
                    InvestorGroup(
                      groupName = None
                    )
                  ))
                ))
              ))

              validate(json) shouldBe false

            }
          }

          "contains an empty seq" in {

            val json = Json.toJson(GroupLevelElections(
              groupRatioBlended = Some(GroupRatioBlended(
                investorGroups = Some(Seq())
              ))
            ))

            validate(json) shouldBe false

          }
        }
      }

      "interestAllowanceNonConsolidatedInvestment" when {

        "election" when {

          "is None" in {

            val json = Json.toJson(GroupLevelElections(
              interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
                election = None
              ))
            ))

            validate(json) shouldBe false
          }
        }

        "interestAllowanceNonConsolidatedInvestment" when {

          "investmentName" when {

            "contains a seq which includes an empty string" in {

              val json = Json.toJson(GroupLevelElections(
                interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
                  nonConsolidatedInvestments = Some(Seq(
                    Investment(
                      investmentName = Some("")
                    )
                  ))
                ))
              ))


              validate(json) shouldBe false

            }

            "is None" in {

              val json = Json.toJson(GroupLevelElections(
                interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
                  nonConsolidatedInvestments = Some(Seq(
                    Investment(
                      investmentName = None
                    )
                  ))
                ))
              ))

              validate(json) shouldBe false

            }
          }

          "contains an empty seq" in {

            val json = Json.toJson(GroupLevelElections(
              interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
                nonConsolidatedInvestments = Some(Seq())
              ))
            ))

            validate(json) shouldBe false

          }
        }

        "InterestAllowanceConsolidatedPartnership" when {

          "election is None" in {

            val json = Json.toJson(GroupLevelElections(
              interestAllowanceConsolidatedPartnership = Some(InterestAllowanceConsolidatedPartnership(
                election = None
              ))
            ))

            validate(json) shouldBe false
          }

          "partnership" when {

            "contains a seq which includes an empty string" in {

              val json = Json.toJson(GroupLevelElections(
                interestAllowanceConsolidatedPartnership = Some(InterestAllowanceConsolidatedPartnership(
                  consolidatedPartnerships = Some(Seq(
                    Partnership(
                      partnershipName = Some("")
                    )
                  ))
                ))
              ))

              validate(json) shouldBe false

            }

            "is None" in {

              val json = Json.toJson(GroupLevelElections(
                interestAllowanceConsolidatedPartnership = Some(InterestAllowanceConsolidatedPartnership(
                  consolidatedPartnerships = Some(Seq(
                    Partnership(
                      partnershipName = None
                    )
                  ))
                ))
              ))

              validate(json) shouldBe false

            }

            "contains an empty seq" in {

              val json = Json.toJson(GroupLevelElections(
                interestAllowanceConsolidatedPartnership = Some(InterestAllowanceConsolidatedPartnership(
                  consolidatedPartnerships = Some(Seq())
                ))
              ))

              validate(json) shouldBe false

            }
          }
        }
      }
    }
  }
}

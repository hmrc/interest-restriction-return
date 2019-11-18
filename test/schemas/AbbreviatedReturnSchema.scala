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

import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Logger
import play.api.libs.json.{JsValue, Json, Writes}

import utils.SchemaValidation

//noinspection ScalaStyle
class AbbreviatedReturnSchema extends WordSpec with Matchers with GuiceOneAppPerSuite with SchemaValidation {

  def validate(json: JsValue): Boolean = {
    Logger.debug(s"Json to validate: $json")
    validateJson("abbreviatedReturnSchema.json", json)
  }

  val maxAgentNameLength = 160
  val maxCompanyNameLength = 160
  val utrLength = 10
  val crnLength = 8

  case class AgentDetailsModel(agentActingOnBehalfOfCompany: Boolean = true,
                               agentName: Option[String] = Some("Agent Name"))

  object AgentDetailsModel {
    implicit val writes = Json.writes[AgentDetailsModel]
  }

  sealed trait UltimateParent

  case class UkCompany(registeredCompanyName: Option[String] = Some("cde ltd"),
                       knownAs: Option[String] = Some("efg"),
                       utr: Option[String] = Some("1234567890"),
                       crn: Option[String] = Some("AB123456"),
                       otherUkTaxReference: Option[String] = Some("1234567890")
                      ) extends UltimateParent

  object UkCompany {
    implicit val writes = Json.writes[UkCompany]
  }

  case class NonUkCompany(registeredCompanyName: Option[String] = Some("cde ltd"),
                          knownAs: Option[String] = Some("efg"),
                          countryOfIncorporation: Option[String] = Some("US"),
                          crn: Option[String] = Some("AB123456")
                         ) extends UltimateParent

  object NonUkCompany {
    implicit val writes = Json.writes[NonUkCompany]
  }

  object UltimateParent {
    implicit def writes: Writes[UltimateParent] = Writes {
      case x: UkCompany => Json.toJson(x)(UkCompany.writes)
      case x: NonUkCompany => Json.toJson(x)(NonUkCompany.writes)
    }
  }

  case class DeemedParent(companyName: Option[String] = Some("name"), utr: Option[String] = Some("1111111111"))

  object DeemedParent {
    implicit val writes = Json.writes[DeemedParent]
  }

  case class ParentCompany(ultimateParent: Option[UltimateParent] = Some(UkCompany()), deemedParent: Option[Seq[DeemedParent]] = None)

  object ParentCompany {
    implicit val writes = Json.writes[ParentCompany]
  }

  case class UKCompanies(companyName: Option[String] = Some("name"), utr: Option[String] = Some("1111111111"))

  object UKCompanies {
    implicit val writes = Json.writes[UKCompanies]
  }

  case class AccountingPeriod(startDate: Option[String] = Some("1111-11-11"),
                              endDate: Option[String] = Some("1111-11-11"))

  object AccountingPeriod {
    implicit val writes = Json.writes[AccountingPeriod]
  }

  case class GroupCompanyDetails(totalCompanies: Option[Int] = Some(1),
                                 inclusionOfNonConsentingCompanies: Option[Boolean] = Some(true),
                                 accountingPeriod: Option[AccountingPeriod] = Some(AccountingPeriod()))

  object GroupCompanyDetails {
    implicit val writes = Json.writes[GroupCompanyDetails]
  }

  case class InvestorGroup(groupName: Option[String] = Some("Group"))

  object InvestorGroup {
    implicit val writes = Json.writes[InvestorGroup]
  }

  case class GroupRatioBlended(election: Option[String] = Some("elect"),
                               investorGroups: Option[Seq[InvestorGroup]] = Some(Seq(InvestorGroup())))

  object GroupRatioBlended {
    implicit val writes = Json.writes[GroupRatioBlended]
  }

  case class Investment(investmentName: Option[String] = Some("Name"))

  object Investment {
    implicit val writes = Json.writes[Investment]
  }

  case class InterestAllowanceNonConsolidatedInvestment(election: Option[String] = Some("elect"),
                                                        nonConsolidatedInvestments: Option[Seq[Investment]] = Some(Seq(Investment())))

  object InterestAllowanceNonConsolidatedInvestment {
    implicit val writes = Json.writes[InterestAllowanceNonConsolidatedInvestment]
  }

  case class Partnership(partnershipName: Option[String] = Some("Name"))

  object Partnership {
    implicit val writes = Json.writes[Partnership]
  }

  case class InterestAllowanceConsolidatedPartnership(election: Option[String] = Some("elect"),
                                                      consolidatedPartnerships: Option[Seq[Partnership]] = Some(Seq(Partnership())))

  object InterestAllowanceConsolidatedPartnership {
    implicit val writes = Json.writes[InterestAllowanceConsolidatedPartnership]
  }

  case class GroupLevelElections(groupRatioElection: Option[String] = Some("elect"),
                                 groupRatioBlended: Option[GroupRatioBlended] = Some(GroupRatioBlended()),
                                 groupEBITDAChargeableGains: Option[Boolean] = Some(true),
                                 interestAllowanceAlternativeCalculation: Option[Boolean] = Some(true),
                                 interestAllowanceNonConsolidatedInvestment: Option[InterestAllowanceNonConsolidatedInvestment] = Some(InterestAllowanceNonConsolidatedInvestment()),
                                 interestAllowanceConsolidatedPartnership: Option[InterestAllowanceConsolidatedPartnership] = Some(InterestAllowanceConsolidatedPartnership())
                                )

  object GroupLevelElections {
    implicit val writes = Json.writes[GroupLevelElections]
  }

  case class AbbreviatedReturnModel(agentDetails: Option[AgentDetailsModel] = Some(AgentDetailsModel()),
                                    isReportingCompanyUltimateParent: Option[Boolean] = Some(true),
                                    parentCompany: Option[ParentCompany] = Some(ParentCompany()),
                                    groupCompanyDetails: Option[GroupCompanyDetails] = Some(GroupCompanyDetails()),
                                    submissionType: Option[String] = Some("original"),
                                    revisedReturnDetails: Option[String] = Some("asdfghj"),
                                    groupLevelElections: Option[GroupLevelElections] = Some(GroupLevelElections()),
                                    ukCompanies: Option[Seq[UKCompanies]] = Some(Seq(UKCompanies())))

  object AbbreviatedReturnModel {
    implicit val writes = Json.writes[AbbreviatedReturnModel]
  }

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

      "Validated a successful JSON payload with Deemed Parent company" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          parentCompany = Some(ParentCompany(ultimateParent = None, deemedParent = Some(Seq(DeemedParent()))))
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

      //TODO undecided
      "Validated a successful JSON with a Deemed Parent company with no UTR" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          parentCompany = Some(ParentCompany(ultimateParent = None, Some(Seq(
            DeemedParent(utr = None)
          ))))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with no optional fields" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          agentDetails = Some(AgentDetailsModel(
            agentActingOnBehalfOfCompany = false,
            agentName = None
          )),
          parentCompany = Some(ParentCompany(Some(
            UkCompany(
              knownAs = None,
              otherUkTaxReference = None
            )
          ))),
          revisedReturnDetails = None
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload when otherUkTaxReference is none" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          parentCompany = Some(ParentCompany(
            ultimateParent = Some(UkCompany(
              otherUkTaxReference = None
            ))
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload when otherUkTaxReference is empty" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          parentCompany = Some(ParentCompany(
            ultimateParent = Some(UkCompany(
              otherUkTaxReference = Some("")
            ))
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload when knownAs is none" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          parentCompany = Some(ParentCompany(
            ultimateParent = Some(UkCompany(
              knownAs = None
            ))
          ))
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
          ))))

        validate(json) shouldBe true
      }


      "Validated a successful JSON payload nonConsolidatedInvestments election is revoke" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
            election = Some("revoke")
          ))
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload interestAllowanceConsolidatedPartnership election is None" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            interestAllowanceConsolidatedPartnership = Some(InterestAllowanceConsolidatedPartnership(
              election = None
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

        "agentName" when {

          s"is supplied but blank" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              agentDetails = Some(AgentDetailsModel(
                agentName = Some("")
              ))
            ))

            validate(json) shouldBe false
          }

          s"is supplied but exceeds $maxAgentNameLength" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              agentDetails = Some(AgentDetailsModel(
                agentName = Some("A" * (maxAgentNameLength + 1))
              ))
            ))

            validate(json) shouldBe false
          }
        }
      }

      "reporting company ultimate parent is empty" in {

        val json = Json.toJson(AbbreviatedReturnModel(
          isReportingCompanyUltimateParent = None
        ))

        validate(json) shouldBe false
      }

      "parent Company" when {

        "ultimateParent" when {

          "Uk Company" when {

            "company name" when {

              "is None" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(
                    ultimateParent = Some(UkCompany(
                      registeredCompanyName = None
                    ))
                  ))
                ))

                validate(json) shouldBe false
              }

              s"is empty" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(
                    ultimateParent = Some(UkCompany(
                      registeredCompanyName = Some("")
                    ))
                  ))
                ))

                validate(json) shouldBe false
              }

              s"is longer than $maxCompanyNameLength characters" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(
                    ultimateParent = Some(UkCompany(
                      registeredCompanyName = Some("A" * (maxCompanyNameLength + 1))
                    ))
                  ))
                ))

                validate(json) shouldBe false
              }
            }

            "utr" when {

              s"below $utrLength" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = Some(
                    UkCompany(utr = Some("1" * (utrLength - 1)))
                  ))
                  ))
                )
                validate(json) shouldBe false
              }

              s"above $utrLength" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = Some(
                    UkCompany(utr = Some("1" * (utrLength + 1)))
                  ))
                  ))
                )

                validate(json) shouldBe false
              }

              "is non numeric" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = Some(
                    UkCompany(utr = Some("a" * utrLength))
                  ))
                  ))
                )

                validate(json) shouldBe false
              }

              "is a symbol" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = Some(
                    UkCompany(utr = Some("@"))
                  ))))
                )
                validate(json) shouldBe false
              }
            }

            "crn" when {

              s"below $crnLength" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = Some(
                    UkCompany(crn = Some("1" * (crnLength - 1)))
                  ))
                  ))
                )
                validate(json) shouldBe false
              }

              s"above $crnLength" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = Some(
                    UkCompany(crn = Some("1" * (crnLength + 1)))
                  ))
                  ))
                )

                validate(json) shouldBe false
              }

              "starts with 1 letter" in {
                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = Some(
                    UkCompany(crn = Some("A" + ("1" * (crnLength - 1))))
                  ))
                  ))
                )
                validate(json) shouldBe false
              }

              "starts with 3 letters" in {
                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = Some(
                    UkCompany(crn = Some("AAA" + ("1" * (crnLength - 3))))
                  ))
                  ))
                )
                validate(json) shouldBe false

              }
            }

            "knownAs" when {

              "knownAs is empty" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(
                    ultimateParent = Some(UkCompany(
                      knownAs = Some("")
                    ))
                  ))
                ))

                validate(json) shouldBe false
              }
            }

            s"is longer than $maxCompanyNameLength characters" in {

              val json = Json.toJson(AbbreviatedReturnModel(
                parentCompany = Some(ParentCompany(
                  ultimateParent = Some(UkCompany(
                    knownAs = Some("A" * (maxCompanyNameLength + 1))
                  ))
                ))
              ))

              validate(json) shouldBe false
            }
          }
          "non-uk compnany" when {

            "company name" when {

              "is None" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(
                    ultimateParent = Some(NonUkCompany(
                      registeredCompanyName = None
                    ))
                  ))
                ))

                validate(json) shouldBe false
              }

              s"is empty" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(
                    ultimateParent = Some(NonUkCompany(
                      registeredCompanyName = Some("")
                    ))
                  ))
                ))

                validate(json) shouldBe false
              }

              s"is longer than $maxCompanyNameLength characters" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(
                    ultimateParent = Some(NonUkCompany(
                      registeredCompanyName = Some("A" * (maxCompanyNameLength + 1))
                    ))
                  ))
                ))

                validate(json) shouldBe false
              }
            }

            "countryOfIncorporation" when {

              "is only one letter" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(
                    ultimateParent = Some(NonUkCompany(
                      countryOfIncorporation = Some("A")
                    ))
                  ))
                ))
                validate(json) shouldBe false
              }

              "is three letters" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(
                    ultimateParent = Some(NonUkCompany(
                      countryOfIncorporation = Some("AAA")
                    ))
                  ))
                ))
                validate(json) shouldBe false
              }

              "contains a number" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(
                    ultimateParent = Some(NonUkCompany(
                      countryOfIncorporation = Some("A1")
                    ))
                  ))
                ))
                validate(json) shouldBe false
              }

              "contains a symbol" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(
                    ultimateParent = Some(NonUkCompany(
                      countryOfIncorporation = Some("A@")
                    ))
                  ))
                ))
                validate(json) shouldBe false
              }

              "non-uk crn" when {

                "is None" in {

                  val json = Json.toJson(AbbreviatedReturnModel(
                    parentCompany = Some(ParentCompany(
                      ultimateParent = Some(NonUkCompany(
                        crn = None
                      ))
                    ))
                  ))

                  validate(json) shouldBe false
                }

                s"is empty" in {

                  val json = Json.toJson(AbbreviatedReturnModel(
                    parentCompany = Some(ParentCompany(
                      ultimateParent = Some(NonUkCompany(
                        crn = Some("")
                      ))
                    ))
                  ))

                  validate(json) shouldBe false
                }
              }

              "knownAs" when {

                "knownAs is empty" in {

                  val json = Json.toJson(AbbreviatedReturnModel(
                    parentCompany = Some(ParentCompany(
                      ultimateParent = Some(NonUkCompany(
                        knownAs = Some("")
                      ))
                    ))
                  ))

                  validate(json) shouldBe false
                }
              }

              s"is longer than $maxCompanyNameLength characters" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(
                    ultimateParent = Some(NonUkCompany(
                      knownAs = Some("A" * (maxCompanyNameLength + 1))
                    ))
                  ))
                ))

                validate(json) shouldBe false
              }

            }
          }

          "deemedParent" when {

            "company name" when {

              "is None" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = None, Some(Seq(
                    DeemedParent(
                      companyName = None
                    )
                  )))))
                )

                validate(json) shouldBe false
              }

              s"is empty" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = None, Some(Seq(
                    DeemedParent(
                      companyName = Some("")
                    )
                  ))))
                ))

                validate(json) shouldBe false
              }

              s"is longer than $maxCompanyNameLength characters" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = None, Some(Seq(
                    DeemedParent(
                      companyName = Some("A" * (maxCompanyNameLength + 1))
                    )
                  ))))
                ))

                validate(json) shouldBe false
              }
            }


            "utr" when {

              s"below $utrLength" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = None, Some(Seq(
                    DeemedParent(utr = Some("1" * (utrLength - 1)))
                  ))))
                ))

                validate(json) shouldBe false
              }

              s"above $utrLength" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = None, Some(Seq(
                    DeemedParent(utr = Some("1" * (utrLength + 1)))
                  ))))
                ))

                validate(json) shouldBe false
              }

              "is non numeric" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = None, Some(Seq(
                    DeemedParent(utr = Some("a" * utrLength))
                  ))))
                ))

                validate(json) shouldBe false
              }

              "is a symbol" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  parentCompany = Some(ParentCompany(ultimateParent = None, Some(Seq(
                    DeemedParent(utr = Some("@"))
                  ))))
                ))

                validate(json) shouldBe false
              }
            }
          }

          "deemed parents and ultimate parent is None" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              parentCompany = Some(ParentCompany(None, None))
            ))

            validate(json) shouldBe false
          }

          "deemed parents is an empty list and ultimate parent is None" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              parentCompany = Some(ParentCompany(ultimateParent = None,
                Some(Seq.empty))
              ))
            )

            validate(json) shouldBe false
          }

          "more than 3 deemed parents and ultimate parent is None" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              parentCompany = Some(ParentCompany(ultimateParent = None,
                Some(Seq(DeemedParent(), DeemedParent(), DeemedParent(), DeemedParent())))
              ))
            )

            validate(json) shouldBe false
          }

          "is None" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              parentCompany = None
            ))

            validate(json) shouldBe false
          }
        }
      }

      "groupCompanyDetails" when {

        "totalCompanies" when {

          "is None" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              groupCompanyDetails = Some(GroupCompanyDetails(
                totalCompanies = None
              ))
            ))

            validate(json) shouldBe false
          }

          "is 0" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              groupCompanyDetails = Some(GroupCompanyDetails(
                totalCompanies = Some(0)
              ))
            ))

            validate(json) shouldBe false
          }
        }

        "inclusionOfNonConsentingCompanies" when {

          "is None" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              groupCompanyDetails = Some(GroupCompanyDetails(
                inclusionOfNonConsentingCompanies = None
              ))
            ))

            validate(json) shouldBe false
          }
        }

        "accountingPeriod" when {

          "startDate is None" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              groupCompanyDetails = Some(GroupCompanyDetails(
                accountingPeriod = Some(AccountingPeriod(startDate = None))
              ))
            ))

            validate(json) shouldBe false
          }

          "endDate is None" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              groupCompanyDetails = Some(GroupCompanyDetails(
                accountingPeriod = Some(AccountingPeriod(endDate = None))
              ))
            ))

            validate(json) shouldBe false
          }


          "is None" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              groupCompanyDetails = Some(GroupCompanyDetails(
                accountingPeriod = None
              ))
            ))

            validate(json) shouldBe false
          }
        }

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

        "companyName" when {

          "is empty" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              ukCompanies = Some(Seq(
                UKCompanies(companyName = Some(""))
              ))
            ))

            validate(json) shouldBe false
          }

          s"is over $maxCompanyNameLength" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              ukCompanies = Some(Seq(
                UKCompanies(companyName = Some("A" * (maxCompanyNameLength + 1)))
              ))
            ))

            validate(json) shouldBe false
          }

          "is None" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              ukCompanies = Some(Seq(
                UKCompanies(companyName = None)
              ))
            ))

            validate(json) shouldBe false
          }
        }

        "utr" when {

          s"below $utrLength" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              ukCompanies = Some(Seq(
                UKCompanies(utr = Some("1" * (utrLength - 1)))
              ))
            ))

            validate(json) shouldBe false
          }

          s"above $utrLength" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              ukCompanies = Some(Seq(
                UKCompanies(utr = Some("1" * (utrLength + 1)))
              ))
            ))

            validate(json) shouldBe false
          }

          "is non numeric" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              ukCompanies = Some(Seq(
                UKCompanies(utr = Some("a" * utrLength))
              ))
            ))

            validate(json) shouldBe false
          }

          "is a symbol" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              ukCompanies = Some(Seq(
                UKCompanies(utr = Some("@"))
              ))
            ))

            validate(json) shouldBe false
          }
        }

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

      "groupLevelElections" when {

        "groupRatio" when {

          "is Empty" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              groupLevelElections = Some(GroupLevelElections(
                groupRatioElection = Some("")
              ))
            ))

            validate(json) shouldBe false
          }

          "supplied wihin an invalid enum value" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              groupLevelElections = Some(GroupLevelElections(
                groupRatioElection = Some("invalid")
              ))
            ))

            validate(json) shouldBe false

          }
        }

        "groupRatioBlended" when {

          "election" when {

            "is Empty" in {

              val json = Json.toJson(AbbreviatedReturnModel(
                groupLevelElections = Some(GroupLevelElections(
                  groupRatioBlended = Some(GroupRatioBlended(
                    election = Some("")
                  ))
                ))
              ))

              validate(json) shouldBe false
            }

            "supplied wihin an invalid enum value" in {

              val json = Json.toJson(AbbreviatedReturnModel(
                groupLevelElections = Some(GroupLevelElections(
                  groupRatioBlended = Some(GroupRatioBlended(
                    election = Some("invalid")
                  ))
                ))
              ))

              validate(json) shouldBe false

            }

          }

          "investorGroups" when {

            "groupName" when {

              "contains a seq which includes an empty string" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  groupLevelElections = Some(GroupLevelElections(
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
                ))

                validate(json) shouldBe false

              }

              "is None" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  groupLevelElections = Some(GroupLevelElections(
                    groupRatioBlended = Some(GroupRatioBlended(
                      investorGroups = Some(Seq(
                        InvestorGroup(
                          groupName = None
                        )
                      ))
                    ))
                  ))
                ))

                validate(json) shouldBe false

              }
            }

            "contains an empty seq" in {

              val json = Json.toJson(AbbreviatedReturnModel(
                groupLevelElections = Some(GroupLevelElections(
                  groupRatioBlended = Some(GroupRatioBlended(
                    investorGroups = Some(Seq())
                  ))
                ))
              ))

              validate(json) shouldBe false

            }
          }
        }

        "interestAllowanceNonConsolidatedInvestment" when {

          "election" when {

            "is Empty" in {

              val json = Json.toJson(AbbreviatedReturnModel(
                groupLevelElections = Some(GroupLevelElections(
                  interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
                    election = Some("")
                  ))
                ))
              ))

              validate(json) shouldBe false
            }

            "supplied wihin an invalid enum value" in {

              val json = Json.toJson(AbbreviatedReturnModel(
                groupLevelElections = Some(GroupLevelElections(
                  interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
                    election = Some("invalid")
                  ))
                ))
              ))
              validate(json) shouldBe false
            }
          }

          "interestAllowanceNonConsolidatedInvestment" when {

            "investmentName" when {

              "contains a seq which includes an empty string" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  groupLevelElections = Some(GroupLevelElections(
                    interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
                      nonConsolidatedInvestments = Some(Seq(
                        Investment(
                          investmentName = Some("")
                        )
                      ))
                    ))
                  ))
                ))

                validate(json) shouldBe false

              }

              "is None" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  groupLevelElections = Some(GroupLevelElections(
                    interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
                      nonConsolidatedInvestments = Some(Seq(
                        Investment(
                          investmentName = None
                        )
                      ))
                    ))
                  ))
                ))

                validate(json) shouldBe false

              }
            }

            "contains an empty seq" in {

              val json = Json.toJson(AbbreviatedReturnModel(
                groupLevelElections = Some(GroupLevelElections(
                  interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
                    nonConsolidatedInvestments = Some(Seq())
                  ))
                ))
              ))

              validate(json) shouldBe false

            }
          }

          "InterestAllowanceConsolidatedPartnership" when {

            "partnership" when {

              "contains a seq which includes an empty string" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  groupLevelElections = Some(GroupLevelElections(
                    interestAllowanceConsolidatedPartnership = Some(InterestAllowanceConsolidatedPartnership(
                      consolidatedPartnerships = Some(Seq(
                        Partnership(
                          partnershipName = Some("")
                        )
                      ))
                    ))
                  ))
                ))

                validate(json) shouldBe false

              }

              "is None" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  groupLevelElections = Some(GroupLevelElections(
                    interestAllowanceConsolidatedPartnership = Some(InterestAllowanceConsolidatedPartnership(
                      consolidatedPartnerships = Some(Seq(
                        Partnership(
                          partnershipName = None
                        )
                      ))
                    ))
                  ))
                ))

                validate(json) shouldBe false

              }

              "contains an empty seq" in {

                val json = Json.toJson(AbbreviatedReturnModel(
                  groupLevelElections = Some(GroupLevelElections(
                    interestAllowanceConsolidatedPartnership = Some(InterestAllowanceConsolidatedPartnership(
                      consolidatedPartnerships = Some(Seq())
                    )
                    ))
                  ))
                )
                validate(json) shouldBe false

              }
            }
          }
        }
      }
    }
  }
}

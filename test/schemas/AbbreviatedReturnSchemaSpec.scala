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
          agentDetails = Some(AgentDetails(
            agentActingOnBehalfOfCompany = Some(false),
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

          "is None" in {

            val json = Json.toJson(AbbreviatedReturnModel(
              groupLevelElections = Some(GroupLevelElections(
                groupRatioElection = None
              ))
            ))

            validate(json) shouldBe false
          }
        }

        "groupRatioBlended" when {

          "election" when {

            "is None" in {

              val json = Json.toJson(AbbreviatedReturnModel(
                groupLevelElections = Some(GroupLevelElections(
                  groupRatioBlended = Some(GroupRatioBlended(
                    election = None
                  ))
                ))
              ))

              validate(json) shouldBe false
            }

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

            "is None" in {

              val json = Json.toJson(AbbreviatedReturnModel(
                groupLevelElections = Some(GroupLevelElections(
                  interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
                    election = None
                  ))
                ))
              ))

              validate(json) shouldBe false
            }

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

            "election is None" in {

              val json = Json.toJson(AbbreviatedReturnModel(
                groupLevelElections = Some(GroupLevelElections(
                  interestAllowanceConsolidatedPartnership = Some(InterestAllowanceConsolidatedPartnership(
                    election = None
                  ))
                ))
              ))

              validate(json) shouldBe false
            }

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

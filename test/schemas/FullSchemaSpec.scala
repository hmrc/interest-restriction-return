package schemas

import play.api.libs.json.{JsValue, Json}

class FullSchemaSpec extends FullSchemaBaseSpec {

  def validate(json: JsValue): Boolean = validateJson("fullReturnSchema.json", json)

  "FullReturn Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload with UK Parent company" in {

        val json = Json.toJson(FullReturnModel())

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with NonUK Parent company" in {

        val json = Json.toJson(FullReturnModel(
          parentCompany = Some(ParentCompany(Some(NonUkUltimateParent())))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with 3 Deemed Parent companies" in {

        val json = Json.toJson(FullReturnModel(
          parentCompany = Some(ParentCompany(ultimateParent = None, deemedParent = Some(Seq(
            DeemedParent(), DeemedParent(), DeemedParent()
          ))))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload submissionType is revised" in {

        val json = Json.toJson(FullReturnModel(
          submissionType = Some("revised")
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload revisedReturnDetails is None" in {

        val json = Json.toJson(FullReturnModel(
          revisedReturnDetails = None
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload groupEBITDAChargeableGains is None" in {

        val json = Json.toJson(FullReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            groupEBITDAChargeableGains = None
          ))))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload interestAllowanceAlternativeCalculation is None" in {

        val json = Json.toJson(FullReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            interestAllowanceAlternativeCalculation = None
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload groupRatioElection election is revoke" in {

        val json = Json.toJson(FullReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            groupRatioElection = Some(revokeString)
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload groupRatioBlended election is revoke" in {

        val json = Json.toJson(FullReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            groupRatioBlended = Some(GroupRatioBlended(
              election = Some(revokeString)
            ))
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload nonConsolidatedInvestments election is revoke" in {

        val json = Json.toJson(FullReturnModel(
          groupLevelElections = Some(GroupLevelElections(
            interestAllowanceNonConsolidatedInvestment = Some(InterestAllowanceNonConsolidatedInvestment(
              election = Some(revokeString)
            ))
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload interestAllowanceConsolidatedPartnership election is false" in {

        val json = Json.toJson(FullReturnModel(
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

          val json = Json.toJson(FullReturnModel(
            agentDetails = None
          ))

          validate(json) shouldBe false
        }
      }

      "reporting company is none" in {

        val json = Json.toJson(FullReturnModel(
          reportingCompany = None
        ))

        validate(json) shouldBe false
      }

      "public infrastructure is none" in {

        val json = Json.toJson(FullReturnModel(
          publicInfrastructure = None
        ))

        validate(json) shouldBe false
      }

      "parent company is none" in {

        val json = Json.toJson(FullReturnModel(
          parentCompany = None
        ))

        validate(json) shouldBe false
      }

      "groupCompanyDetails" when {

        "is None" in {

          val json = Json.toJson(FullReturnModel(
            groupCompanyDetails = None
          ))

          validate(json) shouldBe false
        }
      }

      "submissionType" when {

        "is not a valid type" in {

          val json = Json.toJson(FullReturnModel(
            submissionType = Some("invalid")
          ))

          validate(json) shouldBe false
        }

        "is empty" in {

          val json = Json.toJson(FullReturnModel(
            submissionType = Some("")
          ))

          validate(json) shouldBe false
        }

        "is None" in {

          val json = Json.toJson(FullReturnModel(
            submissionType = None
          ))

          validate(json) shouldBe false
        }
      }

      "revisedReturnDetails" when {

        "is empty" in {

          val json = Json.toJson(FullReturnModel(
            revisedReturnDetails = Some("")
          ))

          validate(json) shouldBe false
        }
      }

      "ukCompanies" when {

        "is empty Sequence" in {

          val json = Json.toJson(FullReturnModel(
            ukCompanies = Some(Seq.empty)
          ))

          validate(json) shouldBe false
        }

        "is None" in {

          val json = Json.toJson(FullReturnModel(
            ukCompanies = None
          ))

          validate(json) shouldBe false
        }
      }
    }
  }






}

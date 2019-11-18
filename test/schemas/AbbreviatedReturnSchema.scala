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

  case class AbbreviatedReturnModel(agentDetails: Option[AgentDetailsModel] = Some(AgentDetailsModel()),
                                    isReportingCompanyUltimateParent: Option[Boolean] = Some(true),
                                    parentCompany: Option[ParentCompany] = Some(ParentCompany()),
                                    groupCompanyDetails: Option[GroupCompanyDetails] = Some(GroupCompanyDetails()),
                                    submissionType: Option[String] = Some("original"),
                                    revisedReturnDetails: Option[String] = Some("asdfghj"),
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
                    companyName = Some("A" * (maxCompanyNameLength+1))
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
  }
}

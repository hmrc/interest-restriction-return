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

package uk.gov.hmrc.interestrestrictionreturn.controllers

import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsValue, Json}
import utils.SchemaValidation

class ReportingCompanyJsonSchema extends WordSpec with Matchers with GuiceOneAppPerSuite {

  val schemaValidation = new SchemaValidation

  def validate(json: JsValue): Boolean = {
    schemaValidation.validateJson("reportingCompanyJsonSchema.json", json)
  }

  val maxCompanyNameLength = 160
  val utrLength = 10
  val crnLength = 8

  case class AgentDetails(agentActingOnBehalfOfCompany: Option[Boolean] = Some(true),
                          agentName: Option[String] = Some("Agent K"))

  object AgentDetails {
    implicit val writes = Json.writes[AgentDetails]
  }

  case class ReportingCompany(companyName: Option[String] = Some("MIB Ltd"),
                              utr: Option[String] = Some("1234567890"),
                              crn: Option[String] = Some("12345678"),
                              sameAsUltimateParent: Option[Boolean] = Some(false),
                              reportingCompanyDeemed: Option[Boolean] = Some(true))

  object ReportingCompany {
    implicit val writes = Json.writes[ReportingCompany]
  }

  case class AuthorisingCompanyModel(companyName: Option[String] = Some("cde ltd"),
                                     utr: Option[String] = Some("1234567890"))

  object AuthorisingCompanyModel {
    implicit val writes = Json.writes[AuthorisingCompanyModel]
  }

  case class AppointReportingCompanyModel(agentDetails: Option[AgentDetails] = Some(AgentDetails()),
                                          reportingCompany: Option[ReportingCompany] = Some(ReportingCompany()),
                                          authorisingCompanies: Option[Seq[AuthorisingCompanyModel]] = Some(Seq(AuthorisingCompanyModel())),
                                          declaration: Boolean = true)

  object AppointReportingCompanyModel {
    implicit val writes = Json.writes[AppointReportingCompanyModel]
  }

  "ReportingCompany Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload" in {

        val json = Json.toJson(AppointReportingCompanyModel())
        validate(json) shouldBe true
      }

      " crn is two characters and six numbers" in {

        val json = Json.toJson(AppointReportingCompanyModel(
          reportingCompany = Some(ReportingCompany(crn = Some("aA111111")))
        ))

        validate(json) shouldBe true
      }
    }

    "Return Invalid" when {

      "agentName" when {

        "agentName is empty" in {

          val json = Json.toJson(AppointReportingCompanyModel(agentDetails = Some(AgentDetails(agentName = Some("")))))

          validate(json) shouldBe false
        }
        //TODO add when validation library is updated to v7
        //      "agentName is not applied" in {
        //
        //        val json = Json.toJson(AppointReportingCompanyModel(agentDetails =  = Some(AgentDetails(agentName = None))))
        //
        //        validate(json) shouldBe false
        //      }
        //
        //      "agentName is applied and agentActOnBehalf is false" in {
        //
        //        val json = Json.toJson(AppointReportingCompanyModel(
        //          agentActingOnBehalfOfCompany = false
        //        ))
        //        validate(json) shouldBe false
        //      }

        "agentName exceeds 160 characters" in {

          val json = Json.toJson(AppointReportingCompanyModel(agentDetails = Some(AgentDetails(agentName = Some("A" * (maxCompanyNameLength + 1))))))

          validate(json) shouldBe false
        }
      }

      "reportingCompanyName" when {

        "reportingCompanyName is empty" in {

          val json = Json.toJson(AppointReportingCompanyModel(reportingCompany = Some(ReportingCompany(companyName = Some("")))))

          validate(json) shouldBe false
        }

        "reportingCompanyName is not applied" in {

          val json = Json.toJson(AppointReportingCompanyModel(reportingCompany = Some(ReportingCompany(companyName = None))))

          validate(json) shouldBe false
        }

        "reportingCompanyName exceeds 160 characters" in {

          val json = Json.toJson(AppointReportingCompanyModel(reportingCompany = Some(ReportingCompany(companyName = Some("A" * (maxCompanyNameLength + 1))))))

          validate(json) shouldBe false
        }
      }

      "utr" when {

        s"below $utrLength" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            reportingCompany = Some(ReportingCompany(utr = Some("1" * (utrLength - 1))))
          ))

          validate(json) shouldBe false
        }

        s"above $utrLength" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            reportingCompany = Some(ReportingCompany(utr = Some("1" * (utrLength + 1))))
          ))

          validate(json) shouldBe false
        }

        "is non numeric" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            reportingCompany = Some(ReportingCompany(utr = Some("a" * (utrLength))))
          ))

          validate(json) shouldBe false
        }

        "is a symbol" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            reportingCompany = Some(ReportingCompany(utr = Some("@")))
          ))

          validate(json) shouldBe false
        }

        "is not applied" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            reportingCompany = Some(ReportingCompany(utr = None))
          ))

          validate(json) shouldBe false
        }
      }

      "authorisingCompanies" when {

        "empty sequence" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            authorisingCompanies = Some(Seq())
          ))

          validate(json) shouldBe false
        }

        "not supplied" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            authorisingCompanies = None
          ))

          validate(json) shouldBe false
        }

        "companyName" when {

          "exceeds 160" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                companyName = Some("A" * (maxCompanyNameLength + 1))
              )))
            ))

            validate(json) shouldBe false
          }

          "is empty" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                companyName = Some("")
              )))
            ))

            validate(json) shouldBe false
          }

          "is not applied" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                companyName = None
              )))
            ))

            validate(json) shouldBe false
          }
        }

        "utr" when {

          s"below $utrLength" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                utr = Some("1" * (utrLength - 1))
              )))
            ))

            validate(json) shouldBe false
          }

          s"above $utrLength" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                utr = Some("1" * (utrLength + 1))
              )))
            ))

            validate(json) shouldBe false
          }

          "is non numeric" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                utr = Some("a" * (utrLength))
              )))
            ))

            validate(json) shouldBe false
          }

          "is a symbol" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                utr = Some("@")
              )))
            ))

            validate(json) shouldBe false
          }

          "is not applied" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                utr = None
              )))
            ))

            validate(json) shouldBe false
          }
        }
      }

      "crn" when {

        s"below $crnLength" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            reportingCompany = Some(ReportingCompany(crn = Some("1" * (crnLength - 1))))
          ))

          validate(json) shouldBe false
        }

        s"above $crnLength" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            reportingCompany = Some(ReportingCompany(crn = Some("1" * (crnLength + 1))))
          ))

          validate(json) shouldBe false
        }

        "is non numeric" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            reportingCompany = Some(ReportingCompany(crn = Some("a" * crnLength)))
          ))

          validate(json) shouldBe false
        }

        "is a symbol" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            reportingCompany = Some(ReportingCompany(crn = Some("@")))
          ))

          validate(json) shouldBe false
        }
      }

      "sameAsUltimateParent" when {

        "is not applied" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            reportingCompany = Some(ReportingCompany(sameAsUltimateParent = None))
          ))

          validate(json) shouldBe false
        }
      }

      "reportingCompanyDeemed" when {

        "is not applied" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            reportingCompany = Some(ReportingCompany(reportingCompanyDeemed = None))
          ))

          validate(json) shouldBe false
        }
      }
    }
  }
}

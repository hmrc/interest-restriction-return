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


  case class AuthorisingCompanyModel(companyName: Option[String] = Some("cde ltd"),
                                     utr: Option[String] = Some("1234567890"))

  object AuthorisingCompanyModel {
    implicit val writes = Json.writes[AuthorisingCompanyModel]
  }

  case class ReportingCompanyModel(agentActingOnBehalfOfCompany: Boolean = true,
                                   agentName: Option[String] = Some("abc"),
                                   reportingCompanyName: Option[String] = Some("abc"),
                                   utr: Option[String] = Some("1234567890"),
                                   crn: String = "12345678",
                                   authorisingCompanies: Option[Seq[AuthorisingCompanyModel]] = Some(Seq(AuthorisingCompanyModel())),
                                   sameAsUltimateParent: Option[Boolean] = Some(false),
                                   reportingCompanyDeemed: Option[Boolean] = Some(true),
                                   confirmTrue: Boolean = true
                                  )

  object ReportingCompanyModel {
    implicit val writes = Json.writes[ReportingCompanyModel]
  }

  "ReportingCompany Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload" in {

        val json = Json.toJson(ReportingCompanyModel())
        println(json)

        validate(json) shouldBe true
      }

      " crn is two characters and six numbers" in {

        val json = Json.toJson(ReportingCompanyModel(
          crn = "aA111111"
        ))

        validate(json) shouldBe true
      }
    }

    "agentName" when {

      "agentName is empty" in {

        val json = Json.toJson(ReportingCompanyModel(agentName = Some("")))

        validate(json) shouldBe false
      }
//TODO add when validation library is updated to v7
//      "agentName is not applied" in {
//
//        val json = Json.toJson(ReportingCompanyModel(agentName = None))
//
//        validate(json) shouldBe false
//      }
//
//      "agentName is applied and agentActOnBehalf is false" in {
//
//        val json = Json.toJson(ReportingCompanyModel(
//          agentActingOnBehalfOfCompany = false
//        ))
//        validate(json) shouldBe false
//      }

      "agentName exceeds 160 characters" in {

        val json = Json.toJson(ReportingCompanyModel(agentName = Some("A" * (maxCompanyNameLength + 1))))

        validate(json) shouldBe false
      }
    }

    "reportingCompanyName" when {

      "reportingCompanyName is empty" in {

        val json = Json.toJson(ReportingCompanyModel(reportingCompanyName = Some("")))

        validate(json) shouldBe false
      }

      "reportingCompanyName is not applied" in {

        val json = Json.toJson(ReportingCompanyModel(reportingCompanyName = None))

        validate(json) shouldBe false
      }

      "reportingCompanyName exceeds 160 characters" in {

        val json = Json.toJson(ReportingCompanyModel(reportingCompanyName = Some("A" * (maxCompanyNameLength + 1))))

        validate(json) shouldBe false
      }
    }

    "utr" when {

      s"below $utrLength" in {

        val json = Json.toJson(ReportingCompanyModel(
          utr = Some("1" * (utrLength - 1))
        ))

        validate(json) shouldBe false
      }

      s"above $utrLength" in {

        val json = Json.toJson(ReportingCompanyModel(
          utr = Some("1" * (utrLength + 1))
        ))

        validate(json) shouldBe false
      }

      "is non numeric" in {

        val json = Json.toJson(ReportingCompanyModel(
          utr = Some("a" * (utrLength))
        ))

        validate(json) shouldBe false
      }

      "is a symbol" in {

        val json = Json.toJson(ReportingCompanyModel(
          utr = Some("@")
        ))

        validate(json) shouldBe false
      }

      "is not applied" in {

        val json = Json.toJson(ReportingCompanyModel(
          utr = None
        ))

        validate(json) shouldBe false
      }
    }

    "agentName" when {


      "authorisingCompanies" when {

        "empty sequence" in {

          val json = Json.toJson(ReportingCompanyModel(
            authorisingCompanies = Some(Seq())
          ))

          validate(json) shouldBe false
        }

        "not supplied" in {

          val json = Json.toJson(ReportingCompanyModel(
            authorisingCompanies = None
          ))

          validate(json) shouldBe false
        }

        "companyName" when {

          "exceeds 160" in {

            val json = Json.toJson(ReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                companyName = Some("A" * (maxCompanyNameLength + 1))
              )))
            ))

            validate(json) shouldBe false
          }

          "is empty" in {

            val json = Json.toJson(ReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                companyName = Some("")
              )))
            ))

            validate(json) shouldBe false
          }

          "is not applied" in {

            val json = Json.toJson(ReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                companyName = None
              )))
            ))

            validate(json) shouldBe false
          }
        }

        "utr" when {

          s"below $utrLength" in {

            val json = Json.toJson(ReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                utr = Some("1" * (utrLength - 1))
              )))
            ))

            validate(json) shouldBe false
          }

          s"above $utrLength" in {

            val json = Json.toJson(ReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                utr = Some("1" * (utrLength + 1))
              )))
            ))

            validate(json) shouldBe false
          }

          "is non numeric" in {

            val json = Json.toJson(ReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                utr = Some("a" * (utrLength))
              )))
            ))

            validate(json) shouldBe false
          }

          "is a symbol" in {

            val json = Json.toJson(ReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanyModel(
                utr = Some("@")
              )))
            ))

            validate(json) shouldBe false
          }

          "is not applied" in {

            val json = Json.toJson(ReportingCompanyModel(
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

          val json = Json.toJson(ReportingCompanyModel(
            crn = "1" * (crnLength - 1)
          ))

          validate(json) shouldBe false
        }

        s"above $crnLength" in {

          val json = Json.toJson(ReportingCompanyModel(
            crn = "1" * (crnLength + 1)
          ))

          validate(json) shouldBe false
        }

        "is non numeric" in {

          val json = Json.toJson(ReportingCompanyModel(
            crn = "a" * crnLength
          ))

          validate(json) shouldBe false
        }

        "is a symbol" in {

          val json = Json.toJson(ReportingCompanyModel(
            crn = "@"
          ))

          validate(json) shouldBe false
        }
      }

      "sameAsUltimateParent" when {

        "is not applied" in {

          val json = Json.toJson(ReportingCompanyModel(
            sameAsUltimateParent = None
          ))

          validate(json) shouldBe false
        }
      }

      "reportingCompanyDeemed" when {

        "is not applied" in {

          val json = Json.toJson(ReportingCompanyModel(
            reportingCompanyDeemed = None
          ))

          validate(json) shouldBe false
        }
      }
    }
  }
}

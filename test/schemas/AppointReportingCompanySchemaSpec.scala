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

import helpers._
import play.api.libs.json.{JsValue, Json}
import schemas.helpers.appointReportingCompany.AppointReportingCompanyModel

class AppointReportingCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean = validateJson("appointReportingCompanySchema.json", json)

  "AppointReportingCompany Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload" in {

        val json = Json.toJson(AppointReportingCompanyModel())
        validate(json) shouldBe true
      }

      " crn is two characters and six numbers" in {

        val json = Json.toJson(AppointReportingCompanyModel(
          reportingCompany = Some(ReportingCompany(crn = Some("AA111111")))
        ))

        validate(json) shouldBe true
      }

      "Start Date is a valid date" in {

        val json = Json.toJson(AppointReportingCompanyModel(
          accountingPeriod = Some(AccountingPeriod(startDate = Some("2020-09-30"), endDate = Some("2020-09-31")))
        ))

        validate(json) shouldBe true
      }

//      "End Date is a valid date" in {
//
//        val json = Json.toJson(AppointReportingCompanyModel(
//          accountingPeriod = Some(AccountingPeriod(endDate = Some("2020-09-30")))
//        ))
//
//        validate(json) shouldBe true
//      }
    }


    "Return Invalid" when {

      "agentName" when {

        "is not supplied" in {

          val json = Json.toJson(AppointReportingCompanyModel(agentDetails = None))

          validate(json) shouldBe false
        }
      }

      "reportingCompany" when {

        "is not supplied" in {

          val json = Json.toJson(AppointReportingCompanyModel(reportingCompany = None))

          validate(json) shouldBe false
        }
      }

      "authorisingCompanies" when {

        "empty sequence" in {

          val json = Json.toJson(AppointReportingCompanyModel(authorisingCompanies = Some(Seq())))

          validate(json) shouldBe false
        }

        "not supplied" in {

          val json = Json.toJson(AppointReportingCompanyModel(authorisingCompanies = None))

          validate(json) shouldBe false
        }

        "companyName" when {

          "exceeds 160" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanies(
                companyName = Some("A" * (maxCompanyNameLength + 1))
              )))
            ))

            validate(json) shouldBe false
          }

          "is empty" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanies(
                companyName = Some("")
              )))
            ))

            validate(json) shouldBe false
          }

          "is not applied" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanies(
                companyName = None
              )))
            ))

            validate(json) shouldBe false
          }
        }

        "utr" when {

          s"below $utrLength" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanies(
                utr = Some("1" * (utrLength - 1))
              )))
            ))

            validate(json) shouldBe false
          }

          s"above $utrLength" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanies(
                utr = Some("1" * (utrLength + 1))
              )))
            ))

            validate(json) shouldBe false
          }

          "is non numeric" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanies(
                utr = Some("a" * (utrLength))
              )))
            ))

            validate(json) shouldBe false
          }

          "is a symbol" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanies(
                utr = Some("@")
              )))
            ))

            validate(json) shouldBe false
          }

          "is not applied" in {

            val json = Json.toJson(AppointReportingCompanyModel(
              authorisingCompanies = Some(Seq(AuthorisingCompanies(
                utr = None
              )))
            ))

            validate(json) shouldBe false
          }
        }
      }

      "declaration" when {

        "not supplied" in {

          val json = Json.toJson(AppointReportingCompanyModel(declaration = None))

          validate(json) shouldBe false
        }
      }

      "Accounting Period" when {

        "Start Date is None" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            accountingPeriod = Some(AccountingPeriod(startDate = None))
          ))

          validate(json) shouldBe false
        }

        "End Date is None" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            accountingPeriod = Some(AccountingPeriod(endDate = None))
          ))

          validate(json) shouldBe false
        }

        "Start Date is supplied but empty" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            accountingPeriod = Some(AccountingPeriod(startDate = Some("") ,endDate = Some("2020-09-30")))
          ))

          validate(json) shouldBe false
        }

        "End Date is supplied but empty" in {

          val json = Json.toJson(AppointReportingCompanyModel(
            accountingPeriod = Some(AccountingPeriod(startDate = Some("2020-09-30") ,endDate = Some("")))
          ))

          validate(json) shouldBe false
        }
      }
    }
  }
}


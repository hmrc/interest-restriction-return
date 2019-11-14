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

package uk.gov.hmrc.interestrestrictionreturn.schemas

import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.{JsValue, Json, Writes}
import utils.SchemaValidation

class AbbreviatedReturnSchema extends WordSpec with Matchers with GuiceOneAppPerSuite with SchemaValidation {

  def validate(json: JsValue): Boolean = validateJson("abbreviatedReturnSchema.json", json)

  val maxAgentNameLength = 160
  val maxCompanyNameLength = 160
  val utrLength = 10
  val crnLength = 8

  case class AgentDetailsModel(agentActingOnBehalf: Boolean = true,
                               agentName: Option[String] = Some("Agent Name"))

  object AgentDetailsModel {
    implicit val writes = Json.writes[AgentDetailsModel]
  }

  sealed trait UltimateParent
  case class UkCompany(registeredCompanyName: Option[String] = Some("cde ltd"),
                       nameByWhichKnown: Option[String] = Some("efg"),
                       utr: Option[String] = Some("1234567890"),
                       crn: Option[String] = Some("AB123456"),
                       otherUkTaxReference: Option[String] = Some("1234567890")
                      ) extends UltimateParent
  object UkCompany {
    implicit val writes = Json.writes[UkCompany]
  }
  case class NonUkCompany(registeredCompanyName: Option[String] = Some("cde ltd"),
                          nameByWhichKnown: Option[String] = Some("efg"),
                          countryOfIncorporation: Option[String] = Some("gb"),
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

  case class AbbreviatedReturnModel(agentDetails: Option[AgentDetailsModel] = Some(AgentDetailsModel()),
                                    isReportingCompanyUltimateParent: Option[Boolean] = Some(true),
                                    ultimateParent: Option[UltimateParent] = Some(UkCompany()))

  object AbbreviatedReturnModel {
    implicit val writes = Json.writes[AbbreviatedReturnModel]
  }

  "AbbreviatedReturn Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload" in {

        val json = Json.toJson(AbbreviatedReturnModel())
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
    }
  }
}

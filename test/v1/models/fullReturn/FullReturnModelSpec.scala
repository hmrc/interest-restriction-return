/*
 * Copyright 2021 HM Revenue & Customs
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

package models.fullReturn

import assets.BaseConstants
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json
import play.api.libs.json.JsObject
import v1.models.fullReturn.FullReturnModel
import assets.fullReturn.FullReturnConstants._
import assets.fullReturn.UkCompanyConstants._

class FullReturnModelSpec extends WordSpec with Matchers with BaseConstants {

  "FullReturnModel" must {

    "correctly write to json" when {

      "max values given with an net expense" in {

        val expectedValue = withoutAppointedReportingCompany(fullReturnNetTaxExpenseJsonMax)
        val actualValue = Json.toJson(fullReturnNetTaxExpenseModelMax)

        actualValue shouldBe expectedValue
      }

      "max values given with an a net income" in {

        val expectedValue = withoutAppointedReportingCompany(fullReturnNetTaxIncomeJsonMax)
        val actualValue = Json.toJson(fullReturnNetTaxIncomeModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = withoutAppointedReportingCompany(fullReturnJsonMin)
        val actualValue = Json.toJson(fullReturnModelMin)

        actualValue shouldBe expectedValue
      }

    }

    "not pass the appointedReportingCompany boolean" when {
      "writing json" in {
        val parsedJson: JsObject = Json.toJson(fullReturnModelMin).as[JsObject]
        val appointedReportingCompanyOption: Option[Boolean] = (parsedJson \ "appointedReportingCompany").asOpt[Boolean]
        appointedReportingCompanyOption shouldBe None
      }
    }

    "correctly read from Json" when {

      "max values given without a net expense or income" in {

        val expectedValue = fullReturnModelMax
        val actualValue = fullReturnJsonMax.as[FullReturnModel]

        actualValue shouldBe expectedValue
      }

      "max values given with a net expense" in {

        val expectedValue = fullReturnNetTaxExpenseModelMax
        val actualValue = fullReturnNetTaxExpenseJsonMax.as[FullReturnModel]

        actualValue shouldBe expectedValue
      }

      "max values given with a net income" in {

        val expectedValue = fullReturnNetTaxIncomeModelMax
        val actualValue = fullReturnNetTaxIncomeJsonMax.as[FullReturnModel]

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = fullReturnModelMin
        val actualValue = fullReturnJsonMin.as[FullReturnModel]

        actualValue shouldBe expectedValue
      }
    }

    "deriving the aggregateNetTaxInterest" when {

      "income is bigger" in {
        fullReturnNetTaxIncomeModelMax.aggregateNetTaxInterest shouldBe ((3 * netTaxInterestIncome) - netTaxInterestExpense)
      }

      "expense is bigger" in {
        fullReturnNetTaxExpenseModelMax.aggregateNetTaxInterest shouldBe (netTaxInterestIncome - (3 * netTaxInterestExpense))
      }

      "income and expense are equal" in {

        val fullReturnModel = fullReturnModelMax.copy(ukCompanies = Seq(ukCompanyModelMax, ukCompanyModelMin.copy(netTaxInterestIncome = 20.00)))

        fullReturnModel.aggregateNetTaxInterest shouldBe 0
      }
    }
      
  }

  def withoutAppointedReportingCompany(json: JsObject): JsObject = json - "appointedReportingCompany"

}
/*
 * Copyright 2023 HM Revenue & Customs
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

package v1.models

import org.scalatest.TryValues._
import play.api.libs.json.{JsPath, JsResultException, JsString, JsonValidationError}

import scala.util.Try
import utils.BaseSpec

class ElectionsSpec extends BaseSpec {

  "Elections" should {
    "be valid" when {
      "groupRatioBlended is entered" in {
        val json   = JsString("groupRatioBlended")
        val result = Try(json.as[Elections])

        result.isSuccess shouldEqual true
      }

      "groupEBITDA is entered" in {
        val json   = JsString("groupEBITDA")
        val result = Try(json.as[Elections])

        result.isSuccess shouldEqual true
      }

      "interestAllowanceAlternativeCalculation is entered" in {
        val json   = JsString("interestAllowanceAlternativeCalculation")
        val result = Try(json.as[Elections])

        result.isSuccess shouldEqual true
      }

      "interestAllowanceNonConsolidatedInvestment is entered" in {
        val json   = JsString("interestAllowanceNonConsolidatedInvestment")
        val result = Try(json.as[Elections])

        result.isSuccess shouldEqual true
      }

      "interestAllowanceConsolidatedPartnership is entered" in {
        val json   = JsString("interestAllowanceConsolidatedPartnership")
        val result = Try(json.as[Elections])

        result.isSuccess shouldEqual true
      }
    }

    "be invalid" when {
      "an incorrect value is entered" in {
        val json   = JsString("Something incorrect")
        val result = Try(json.as[Elections])

        result.failure.exception shouldEqual JsResultException(
          Seq((JsPath, Seq(JsonValidationError(s"Unknown value for Elections"))))
        )
      }
    }
  }
}

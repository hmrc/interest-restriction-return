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

package v1.models

import java.time.LocalDate

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Json, _}
import v1.validation.AccountingPeriodValidator

import scala.util.Try

case class AccountingPeriodModel(startDate: LocalDate, endDate: LocalDate) extends AccountingPeriodValidator {
  override val accountingPeriodModel: AccountingPeriodModel = this
}

object AccountingPeriodModel {

  private def readDate(field: String): Reads[LocalDate] = (__ \ field).read[String]
    .filter(JsonValidationError("Date must be in ISO Date format YYYY-MM-DD"))(_.matches(raw"(\d{4})-(\d{2})-(\d{2})"))
    .filter(JsonValidationError("Date must be a valid date"))(str => Try(LocalDate.parse(str)).isSuccess)
    .map(LocalDate.parse)

  implicit val reads = (readDate("startDate") and readDate("endDate"))(AccountingPeriodModel.apply _)

  implicit val writes = Json.writes[AccountingPeriodModel]
}

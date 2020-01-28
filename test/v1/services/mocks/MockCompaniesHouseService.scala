/*
 * Copyright 2020 HM Revenue & Customs
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

package v1.services.mocks

import org.scalamock.scalatest.MockFactory
import play.api.libs.json.JsPath
import uk.gov.hmrc.http.HeaderCarrier
import v1.connectors.UnexpectedFailure
import v1.models.CRNModel
import v1.models.errors.ValidationErrorResponseModel
import v1.models.requests.IdentifierRequest
import v1.services.CompaniesHouseService

import scala.concurrent.{ExecutionContext, Future}

trait MockCompaniesHouseService extends MockFactory {

  lazy val mockCompaniesHouseService: CompaniesHouseService = mock[CompaniesHouseService]

  def mockCompaniesHouse(crns: Seq[(JsPath, CRNModel)])(response: Either[UnexpectedFailure, Seq[ValidationErrorResponseModel]]): Unit = {
    (mockCompaniesHouseService.invalidCRNs(_: Seq[(JsPath, CRNModel)], _:  Seq[ValidationErrorResponseModel])
                                          (_: HeaderCarrier, _: ExecutionContext, _: IdentifierRequest[_]))
      .expects(crns, *, *, *, *)
      .returns(Future.successful(response))
  }
}
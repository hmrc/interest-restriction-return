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

package services

import connectors.{CompaniesHouseConnector, UnexpectedFailure}
import javax.inject.Inject
import models.requests.IdentifierRequest
import models.{CRNModel, ValidationErrorResponseModel}
import play.api.libs.json.{JsPath, Json}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class CompaniesHouseService @Inject()(companiesHouseConnector: CompaniesHouseConnector) {

  def invalidCRNs(crns: Seq[(JsPath, CRNModel)], errors: Seq[ValidationErrorResponseModel] = Seq())
                 (implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[_])
  : Future[Either[UnexpectedFailure, Seq[ValidationErrorResponseModel]]] = {

    if(crns.isEmpty) {
      Future.successful(Right(errors))
    } else {
      companiesHouseConnector.validateCRN(crns.head._2).flatMap {
        case Left(err: UnexpectedFailure) =>
          Future.successful(Left(err))
        case Left(err) =>
          invalidCRNs(
            crns.tail,
            errors :+ ValidationErrorResponseModel(crns.head._1.toString, Json.toJson(crns.head._2), Seq(err.body))
          )
        case _ =>
          invalidCRNs(crns.tail, errors)
      }
    }
  }
}
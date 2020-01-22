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

package v1.controllers

import cats.data.Validated.{Invalid, Valid}
import play.api.Logger
import play.api.libs.json._
import play.api.mvc.{Request, Result}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.controller.BackendBaseController
import v1.models.Validation.ValidationResult
import v1.models.requests.IdentifierRequest
import v1.models.CRNModel
import v1.models.errors.ValidationErrorResponseModel
import v1.services.{CompaniesHouseService, Submission}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

trait BaseController extends BackendBaseController {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  override def withJsonBody[T](f: (T) => Future[Result])(implicit request: Request[JsValue], m: Manifest[T], reads: Reads[T]): Future[Result] =
    Try(request.body.validate[T]) match {
      case Success(JsSuccess(payload, _)) => f(payload)
      case Success(JsError(errs)) =>
        Logger.debug(request.body.toString())
        Future.successful(BadRequest(Json.toJson(ValidationErrorResponseModel(errs))))
      case Failure(e) => Future.successful(BadRequest(s"Could not parse body due to ${e.getMessage}"))
    }

  def handleValidation[T](validationModel: ValidationResult[T], crns: Seq[(JsPath, CRNModel)], service: Submission[T],
                          companiesHouseService: CompaniesHouseService, controllerName: String)
                         (implicit hc: HeaderCarrier,identifierRequest: IdentifierRequest[JsValue]): Future[Result] = {
    validationModel match {
      case Invalid(e) =>
        Logger.debug(s"[$controllerName][submit] Business Rule Errors: ${Json.toJson(ValidationErrorResponseModel(e))}")
        Future.successful(BadRequest(Json.toJson(ValidationErrorResponseModel(e))))
      case Valid(model) =>
        companiesHouseService.invalidCRNs(crns).flatMap {
          case Left(err) => Future.successful(Status(err.status)(err.body))
          case Right(invalidCrns) if invalidCrns.nonEmpty => Future.successful(BadRequest(Json.toJson(invalidCrns)))
          case _ =>
            service.submit(model).map {
              case Left(err) => Status(err.status)(err.body)
              case Right(response) => Ok(Json.toJson(response))
            }
        }
    }
  }
}

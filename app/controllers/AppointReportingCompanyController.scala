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

package controllers

import cats.data.Validated.{Invalid, Valid}
import controllers.actions.AuthAction
import javax.inject.{Inject, Singleton}
import models.ValidationErrorResponseModel
import models.appointReportingCompany.AppointReportingCompanyModel
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import services.AppointReportingCompanyService

import scala.concurrent.Future

@Singleton()
class AppointReportingCompanyController @Inject()(authAction: AuthAction,
                                                  appointReportingCompanyService: AppointReportingCompanyService,
                                                  override val controllerComponents: ControllerComponents) extends BaseController {

  def appoint(): Action[JsValue] = authAction.async(parse.json) { implicit request =>
    withJsonBody[AppointReportingCompanyModel] { appointReportingCompanyModel =>
      appointReportingCompanyModel.validate match {
        case Invalid(e) =>
          Logger.debug(s"[AppointReportingCompanyController][appoint] Business Rule Errors: ${Json.toJson(ValidationErrorResponseModel(e))}")
          Future.successful(BadRequest(Json.toJson(ValidationErrorResponseModel(e))))
        case Valid(model) =>
          appointReportingCompanyService.appoint(model).map {
            case Left(err) => Status(err.status)(err.body)
            case Right(response) => Ok(Json.toJson(response))
          }
      }
    }
  }
}

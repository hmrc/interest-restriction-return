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

import controllers.actions.AuthAction
import javax.inject.{Inject, Singleton}
import models.revokeReportingCompany.RevokeReportingCompanyModel
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import services.RevokeReportingCompanyService

@Singleton()
class RevokeReportingCompanyController @Inject()(authAction: AuthAction,
                                                 revokeReportingCompanyService: RevokeReportingCompanyService,
                                                 override val controllerComponents: ControllerComponents) extends BaseController {

  def revoke(): Action[JsValue] = authAction.async(parse.json) { implicit request =>
    withJsonBody[RevokeReportingCompanyModel] { revokeReportingCompanyModel =>
      revokeReportingCompanyService.revoke(revokeReportingCompanyModel).map {
        case Left(err) => Status(err.status)(err.body)
        case Right(response) => Ok(Json.toJson(response))
      }
    }
  }
}

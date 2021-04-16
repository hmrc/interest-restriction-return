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

package v1.controllers

import audit.AuditWrapper

import javax.inject.{Inject, Singleton}
import play.api.libs.json.JsValue
import play.api.mvc.{Action, ControllerComponents}
import v1.controllers.actions.AuthAction
import v1.models.fullReturn.FullReturnModel
import v1.services.FullReturnService

@Singleton()
class FullReturnController @Inject()(authAction: AuthAction,
                                     fullReturnService: FullReturnService, implicit val auditWrapper: AuditWrapper,
                                     override val controllerComponents: ControllerComponents) extends BaseController {

  def submit(): Action[JsValue] = authAction.async(parse.json) { implicit request =>
    withJsonBody[FullReturnModel] { fullReturnModel =>
      handleValidation(
        validationModel = fullReturnModel.validate,
        service = fullReturnService,
        controllerName = "FullReturnController"
      )
    }
  }
}

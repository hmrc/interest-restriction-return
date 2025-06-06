/*
 * Copyright 2025 HM Revenue & Customs
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

import config.AppConfig
import play.api.libs.json.JsValue
import play.api.mvc.{Action, ControllerComponents}
import v1.controllers.actions.AuthAction
import v1.models.fullReturn.FullReturnModel
import v1.services.{FullReturnService, NrsService}

import javax.inject.{Inject, Singleton}

@Singleton()
class FullReturnController @Inject() (
  authAction: AuthAction,
  fullReturnService: FullReturnService,
  nrsService: NrsService,
  appConfig: AppConfig,
  override val controllerComponents: ControllerComponents
) extends BaseController {

  def submit(): Action[JsValue] = authAction.async(parse.json) { implicit request =>
    withJsonBody[FullReturnModel] { fullReturnModel =>
      handleValidationAndSubmit(
        validationModel = fullReturnModel.validate,
        service = fullReturnService,
        maybeNrsService = Some(nrsService),
        appConfig = appConfig
      )
    }
  }

  def validate(): Action[JsValue] = authAction.async(parse.json) { implicit request =>
    withJsonBody[FullReturnModel] { fullReturnModel =>
      handleValidationForValidationMode(validationModel = fullReturnModel.validate)
    }
  }
}

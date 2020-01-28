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

import javax.inject.{Inject, Singleton}
import v1.connectors.{CompaniesHouseConnector, UnexpectedFailure}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import v1.controllers.actions.AuthAction
import v1.models.CRNModel


@Singleton()
class CompaniesHouseController @Inject()(authAction: AuthAction,
                                         companiesHouseConnector: CompaniesHouseConnector,
                                         override val controllerComponents: ControllerComponents) extends BaseController {

  def validateCRN(crn: String): Action[AnyContent] = authAction.async { implicit request =>
    companiesHouseConnector.validateCRN(CRNModel(crn)).map {
      case Left(_: UnexpectedFailure) => InternalServerError
      case Left(_) => BadRequest
      case _ => NoContent
    }
  }
}
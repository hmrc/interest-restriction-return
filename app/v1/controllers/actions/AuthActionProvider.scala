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

package v1.controllers.actions

import com.google.inject.Inject
import play.api.mvc.BodyParsers
import uk.gov.hmrc.auth.core.AuthConnector

import scala.concurrent.ExecutionContext

trait AuthActionProvider {
  def apply(isInternal: Boolean = false) : AuthActionBase
}

class AuthActionProviderImpl @Inject()(authConnector: AuthConnector, parser: BodyParsers.Default)
                                      (implicit val ec: ExecutionContext) extends AuthActionProvider {
  def apply(isInternal: Boolean = false) : AuthActionBase = if (isInternal) NoAuthAction(authConnector,parser) else AuthAction(authConnector,parser)
}
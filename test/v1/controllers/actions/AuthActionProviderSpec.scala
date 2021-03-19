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
import play.api.mvc.{BodyParsers}
import uk.gov.hmrc.auth.core._
import utils.BaseSpec


class AuthActionProviderSpec extends BaseSpec {
  "The Auth Action Provider" must {
    "Issue a NoAuthAction when call is internal" in {
      val authActionProvider = new AuthActionProviderImpl(injector.instanceOf[AuthConnector],injector.instanceOf[BodyParsers.Default])

      val result = authActionProvider.apply(true)

      result.isInstanceOf[NoAuthAction] shouldBe true
    }

    "Issue an AuthAction when call is not internal" in {
      val authActionProvider = new AuthActionProviderImpl(injector.instanceOf[AuthConnector],injector.instanceOf[BodyParsers.Default])

      val result = authActionProvider.apply()

      result.isInstanceOf[AuthAction] shouldBe true
    }
  }
}

/*
 * Copyright 2022 HM Revenue & Customs
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

import javax.inject.Inject
import play.api.libs.json.{Format, Json}
import play.api.mvc._
import play.api.test.Helpers
import utils.BaseSpec
import play.api.mvc.Results.Ok
import scala.concurrent.Future

class BaseControllerSpec extends BaseSpec {

  case class DummyModel(x: String, y: String)
  object DummyModel {
    implicit val fmt: Format[DummyModel] = Json.format[DummyModel]
  }

  class DummyController @Inject() (override val controllerComponents: ControllerComponents) extends BaseController
  object TestBaseController extends DummyController(Helpers.stubControllerComponents())

  val validJsonRequest   = fakeRequest.withBody(Json.obj("x" -> "foo", "y" -> "bar"))
  val invalidJsonRequest = fakeRequest.withBody(Json.obj("x" -> "foo"))

  "BaseController.withJsonBody" should {

    "if provided with a valid JSON body" should {

      "return an OK" in {

        val actual: Future[Result] = TestBaseController
          .withJsonBody[DummyModel](_ => Future.successful(Ok("Success")))(validJsonRequest, implicitly, implicitly)

        await(bodyOf(actual)) shouldBe "Success"
      }
    }

    "if provided with an invalid JSON body" should {

      "return `path.missing` if a mandatory property is missing" in {

        val actual: Future[Result] = TestBaseController
          .withJsonBody[DummyModel](_ => Future.successful(Ok("Success")))(invalidJsonRequest, implicitly, implicitly)

        await(jsonBodyOf(actual)) shouldBe Json.obj(
          "path"    -> "/y",
          "code"    -> "JSON_VALIDATION_ERROR",
          "message" -> "error.path.missing"
        )
      }
    }
  }
}

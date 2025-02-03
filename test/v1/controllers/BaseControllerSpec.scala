/*
 * Copyright 2024 HM Revenue & Customs
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

import data.appointReportingCompany.AppointReportingCompanyConstants.*
import play.api.http.Status.*
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Results.Ok
import play.api.mvc.*
import play.api.test.{FakeRequest, Helpers}
import utils.BaseSpec
import v1.controllers.BaseController
import v1.models.appointReportingCompany.AppointReportingCompanyModel

import javax.inject.Inject
import scala.concurrent.Future

class BaseControllerSpec extends BaseSpec {

  class DummyController @Inject() (override val controllerComponents: ControllerComponents) extends BaseController
  object TestBaseController extends DummyController(Helpers.stubControllerComponents())

  class WithJsonBodyTest(json: JsObject) {
    val request: FakeRequest[JsObject] = fakeRequest.withBody(json)

    val result: Future[Result] = TestBaseController
      .withJsonBody[AppointReportingCompanyModel](_ => Future.successful(Ok("Success")))(
        request,
        implicitly[Manifest[AppointReportingCompanyModel]],
        AppointReportingCompanyModel.format
      )
  }

  class HandleValidationTest(model: AppointReportingCompanyModel) {

    val result: Future[Result] = TestBaseController
      .handleValidation[AppointReportingCompanyModel](model.validate)(_ => Future.successful(Ok("Success")))
  }

  "BaseController" when {
    ".withJsonBody" should {
      "return OK with the correct result" when {
        "the body is a valid JSON and matches the model" in new WithJsonBodyTest(appointReportingCompanyJsonMax) {
          status(result)        shouldBe OK
          await(bodyOf(result)) shouldBe "Success"
        }
      }

      "return BAD_REQUEST with the appropriate error response" when {
        "the body is a valid JSON but does not match the model" in new WithJsonBodyTest(
          invalidAppointReportingCompanyJson
        ) {
          status(result)            shouldBe BAD_REQUEST
          await(jsonBodyOf(result)) shouldBe Json.obj(
            "path"    -> "/authorisingCompanies",
            "code"    -> "JSON_VALIDATION_ERROR",
            "message" -> "error.path.missing"
          )
        }
      }

      "return BAD_REQUEST with an exception message" when {
        "the body cannot be parsed" in new WithJsonBodyTest(None.orNull) {
          status(result) shouldBe BAD_REQUEST

          val exceptionMessage: String = intercept[Exception] {
            await(jsonBodyOf(result))
          }.getMessage

          exceptionMessage should include("Could not parse body due to")
          exceptionMessage should include("null")
        }
      }
    }

    ".handleValidation" should {
      "return OK with the correct result" when {
        "the validation of the model is successful" in new HandleValidationTest(appointReportingCompanyModelMax) {
          status(result)        shouldBe OK
          await(bodyOf(result)) shouldBe "Success"
        }
      }

      "return BAD_REQUEST with the appropriate error response" when {
        "the validation of the model fails" in new HandleValidationTest(
          appointReportingCompanyModelMax.copy(authorisingCompanies = Seq.empty)
        ) {
          status(result)            shouldBe BAD_REQUEST
          await(jsonBodyOf(result)) shouldBe Json.obj(
            "path"    -> "/authorisingCompanies",
            "code"    -> "AUTHORISING_COMPANIES_EMPTY",
            "message" -> "Enter at least 1 authorising company"
          )
        }
      }
    }
  }
}

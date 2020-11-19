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

package config

import play.api.test.Helpers._
import com.typesafe.config.ConfigFactory
import controllers.Assets
import definition.{Definition, ApiDefinitionFactory}
import definition.Definition._
import mocks.MockAppConfig
import play.api.Configuration
import utils.BaseSpec
import play.api.http.{DefaultHttpErrorHandler, HttpErrorConfig, Status}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{ControllerComponents, Result}
import play.libs.Json

import scala.concurrent.Future

class DocumentationControllerSpec extends BaseSpec  {

  val selfAssessmentApiDefinition: ApiDefinitionFactory = new ApiDefinitionFactory(TestHelper.mockConfig)
  val controller: DocumentationController = TestHelper.controller(selfAssessmentApiDefinition)

  "definition" must {
    "return the definition file" in {
      val expected: Definition = selfAssessmentApiDefinition.definition
      val expectedJson  = Json.toJson(expected)
      val result: Future[Result] = controller.definition()(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe expectedJson
    }
  }

  "raml" must {
    "return the raml documentation" in {
      val result: Future[Result] = controller.raml("1.0", "application.raml")(fakeRequest)
      val raml =
        scala.io.Source.fromInputStream(getClass().getResourceAsStream("/public/api/conf/1.0/application.raml")).mkString
      status(result) shouldBe Status.OK
      contentAsString(result) shouldBe raml
    }
  }

  "conf" must {
    "return conf documentation" in {
      val result: Future[Result] = controller.conf("1.0", "abbreviatedReturn.raml")(fakeRequest)
      val raml =
        scala.io.Source.fromInputStream(getClass().getResourceAsStream("/public/api/conf/1.0/abbreviatedReturn.raml")).mkString
      status(result) shouldBe Status.OK
      contentAsString(result) shouldBe raml
    }
  }

}

object TestHelper extends MockAppConfig {

  def mockConfig: AppConfig = {
    MockedAppConfig.apiGatewayContext.returns("gateway")
    MockedAppConfig.featureSwitch.returns(Some(Configuration(ConfigFactory.parseString(
      """
        |version-1.enabled = true
        |version-2.enabled = true
      """.stripMargin))))
    (mockAppConfig.apiStatus _).expects("1.0").returns("ALPHA")
    (mockAppConfig.endpointsEnabled _).expects().returns(true)
    mockAppConfig
  }

  lazy val errorHandler = new DefaultHttpErrorHandler(HttpErrorConfig(showDevErrors = false, None), None, None)

  lazy val fakeApplication =
    new GuiceApplicationBuilder()
      .configure(
        Configuration(
          ConfigFactory.parseString("""
                                      | metrics.enabled       = false
                                    """.stripMargin)
        )
      )
      .build()

  def controller(selfAssessmentApiDefinition: ApiDefinitionFactory): DocumentationController = {
    lazy val mockCc: ControllerComponents = fakeApplication.injector.instanceOf[ControllerComponents]
    lazy val mockAssets: Assets = fakeApplication.injector.instanceOf[Assets]
    new DocumentationController(selfAssessmentApiDefinition, mockCc, mockAssets, errorHandler)
  }

}

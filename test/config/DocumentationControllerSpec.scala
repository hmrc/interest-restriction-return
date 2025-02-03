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

package config

import com.typesafe.config.ConfigFactory
import controllers.Assets
import definition.ApiDefinitionFactory
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.when
import play.api.http.{DefaultHttpErrorHandler, HttpErrorConfig}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.Helpers.*
import play.api.{Application, Configuration}
import utils.BaseSpec

import scala.concurrent.Future

class DocumentationControllerSpec extends BaseSpec {

  when(mockAppConfig.apiGatewayContext).thenReturn("gateway")
  when(mockAppConfig.featureSwitch).thenReturn(Some(Configuration(ConfigFactory.parseString("""
      |version-1.enabled = true
      |version-2.enabled = true
      """.stripMargin))))
  when(mockAppConfig.apiStatus(ArgumentMatchers.eq("1.0"))).thenReturn("ALPHA")
  when(mockAppConfig.endpointsEnabled).thenReturn(true)

  private val selfAssessmentApiDefinition: ApiDefinitionFactory = new ApiDefinitionFactory(mockAppConfig)
  private val controller: DocumentationController               = TestHelper.controller(selfAssessmentApiDefinition)

  "DocumentationController" when {
    ".definition" must {
      "return the definition file" in {
        val expectedJson           = Json.toJson(selfAssessmentApiDefinition.definition)
        val result: Future[Result] = controller.definition()(fakeRequest)
        status(result)        shouldBe OK
        contentAsJson(result) shouldBe expectedJson
      }
    }

    ".specification" must {
      "return the yaml documentation" in {
        val result: Future[Result] = controller.specification("1.0", "application.yaml")(fakeRequest)
        val spec                   =
          scala.io.Source
            .fromInputStream(getClass.getResourceAsStream("/public/api/conf/1.0/application.yaml"))
            .mkString
        status(result)          shouldBe OK
        contentAsString(result) shouldBe spec
      }
    }
  }
}

object TestHelper {

  lazy val errorHandler = new DefaultHttpErrorHandler(HttpErrorConfig(showDevErrors = false, None), None, None)

  lazy val fakeApplication: Application =
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
    lazy val mockAssets: Assets           = fakeApplication.injector.instanceOf[Assets]
    new DocumentationController(selfAssessmentApiDefinition, mockCc, mockAssets)
  }
}

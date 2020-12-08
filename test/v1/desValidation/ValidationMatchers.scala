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

package v1.desValidation

import org.scalatest._
import matchers._
import cats.data.Validated
import cats.data.Validated._
import v1.models.Validation.ValidationResult
import play.api.libs.json.JsValue
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}
import play.api.Logging
import play.api.libs.json.JsValue
import java.io.File

trait ValidationMatchers {

  _: Logging =>

  def validationsMatch(ourValidation: Validated[_, _], jsonSchemaValidation: Validated[_, _]): Boolean = (ourValidation, jsonSchemaValidation) match {
    case (Invalid(_), _) => true
    case (Valid(_), Valid(_)) => true
    case _ => false
  }

  def validationToString(validation: Validated[_, _]): String = validation match {
    case Valid(_) => "passed"  
    case Invalid(e) => s"failed with reason: $e"
  }
    
  class ValidationMatchesSchemaValidationMatcher(schemaName: String, version: String, jsonToValidate: JsValue) extends Matcher[ValidationResult[_]] {
    def apply(left: ValidationResult[_]) = {
      val schemaValidation = validateJson(schemaName, version, jsonToValidate)
      MatchResult(
        validationsMatch(left, schemaValidation),
        s"""Our validation ${validationToString(left)}.\nThe schema validation ${validationToString(schemaValidation)}""",
        s"""Our validation ${validationToString(left)}.\nThe schema validation ${validationToString(schemaValidation)}"""
      )
    }
  }

  def coverSchemaValidation(schemaName: String, version: String, jsonToValidate: JsValue) = new ValidationMatchesSchemaValidationMatcher(schemaName, version, jsonToValidate)

  private final lazy val jsonMapper = new ObjectMapper()
  private final lazy val jsonFactory = jsonMapper.getFactory

  private def loadRequestSchema(schemaName: String, version: String): JsonSchema = {
    val file = new File(s"test/$version/desValidation/desSchemas/$schemaName")
    val uri = file.toURI
    JsonSchemaFactory.byDefault().getJsonSchema(uri.toString)
  }

  def validateJson(schemaName: String, schemaVersion: String, json: JsValue): Validated[String, JsValue] = {
    logger.debug(s"Json to validate: $json")
    val jsonParser = jsonFactory.createParser(json.toString)
    val jsonNode: JsonNode = jsonMapper.readTree(jsonParser)
    val result: ProcessingReport = loadRequestSchema(schemaName, schemaVersion).validate(jsonNode)
    result.isSuccess match {
      case true => Valid(json)
      case false => Invalid(result.toString)
    }
  }
}

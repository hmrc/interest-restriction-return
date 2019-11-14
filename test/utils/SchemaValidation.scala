/*
 * Copyright 2019 HM Revenue & Customs
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

package utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}
import play.api.libs.json.JsValue

trait SchemaValidation {

  private[utils] final lazy val jsonMapper = new ObjectMapper()
  private[utils] final lazy val jsonFactory = jsonMapper.getFactory

  private[utils] def loadRequestSchema(schemaName: String): JsonSchema = {
    val jsonSchema = scala.io.Source.fromFile(s"docs/schemas/$schemaName")
    val schema: String = jsonSchema.mkString
    val schemaMapper = new ObjectMapper()
    val factory = schemaMapper.getFactory
    val schemaParser: JsonParser = factory.createParser(schema)
    val schemaJson: JsonNode = schemaMapper.readTree(schemaParser)
    jsonSchema.close()
    JsonSchemaFactory.byDefault().getJsonSchema(schemaJson)
  }

  def validateJson(schemaName: String, json: JsValue): Boolean = {
    val jsonParser = jsonFactory.createParser(json.toString)
    val jsonNode: JsonNode = jsonMapper.readTree(jsonParser)
    val result: ProcessingReport = loadRequestSchema(schemaName).validate(jsonNode)
    result.isSuccess
  }
}

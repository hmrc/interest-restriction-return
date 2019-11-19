package schemas.subSchemas

import play.api.libs.json.JsValue
import schemas.BaseSchemaSpec

class ParentCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean =
    validateJson("subSchemas/agentDetails.json", json)

}

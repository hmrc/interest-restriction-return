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

package utils

import config.{AppConfig, FeatureSwitch}
import play.api.libs.json._
import v1.models.{GroupLevelElectionsModel, GroupRatioModel}
import v1.models.abbreviatedReturn.AbbreviatedReturnModel
import v1.models.fullReturn.FullReturnModel

import javax.inject.Inject

trait JsonFormatters {
  val cr008Enabled: Boolean

  def removeJsPathIfFeatureNotEnabled[T](path: JsPath)(implicit writes: Writes[T]): Writes[T] =
    Writes[T] { models =>
      val json: JsObject = Json.toJson(models).as[JsObject]
      if (cr008Enabled) 
        json 
      else 
        json.transform(path.json.prune) match {
          case JsSuccess(newJson, _) => newJson.as[JsValue]
          case _ => json
        }
    }

  implicit val groupLevelElectionWrites: Writes[GroupLevelElectionsModel] = removeJsPathIfFeatureNotEnabled(__ \ "activeInterestAllowanceAlternativeCalculation")(GroupLevelElectionsModel.writes)

  implicit val groupRatioWrites: Writes[GroupRatioModel] = removeJsPathIfFeatureNotEnabled(__ \ "activeGroupEBITDAChargeableGains")(GroupRatioModel.writes)

  implicit val fullReturnWrites: Writes[FullReturnModel] = Writes { models =>
    JsObject(Json.obj(
      "agentDetails" -> models.agentDetails,
      "reportingCompany" -> models.reportingCompany,
      "parentCompany" -> models.parentCompany,
      "publicInfrastructure" -> models.publicInfrastructure,
      "groupCompanyDetails" -> models.groupCompanyDetails,
      "submissionType" -> models.submissionType,
      "revisedReturnDetails" -> models.revisedReturnDetails,
      "groupLevelElections" -> models.groupLevelElections,
      "ukCompanies" -> models.ukCompanies,
      "angie" -> models.angie,
      "returnContainsEstimates" -> models.returnContainsEstimates,
      "groupEstimateReason" -> models.groupEstimateReason,
      "groupSubjectToInterestRestrictions" -> models.groupSubjectToInterestRestrictions,
      "groupSubjectToInterestReactivation" -> models.groupSubjectToInterestReactivation,
      "totalReactivation" -> models.totalReactivation,
      "totalRestrictions" -> models.totalRestrictions,
      "groupLevelAmount" -> models.groupLevelAmount,
      "adjustedGroupInterest" -> models.adjustedGroupInterest
    ).fields.filterNot(_._2 == JsNull))
  }

  implicit val abbreviatedReturnWrites: Writes[AbbreviatedReturnModel] = Writes { models =>
    JsObject(Json.obj(
      "agentDetails" -> models.agentDetails,
      "reportingCompany" -> models.reportingCompany,
      "parentCompany" -> models.parentCompany,
      "publicInfrastructure" -> models.publicInfrastructure,
      "groupCompanyDetails" -> models.groupCompanyDetails,
      "submissionType" -> models.submissionType,
      "revisedReturnDetails" -> models.revisedReturnDetails,
      "groupLevelElections" -> models.groupLevelElections,
      "ukCompanies" -> models.ukCompanies
    ).fields.filterNot(_._2 == JsNull))
  }
}

class FeatureSwitchJsonFormatter @Inject()(val config: AppConfig) extends JsonFormatters {
  override val cr008Enabled: Boolean = FeatureSwitch(config.featureSwitch).changeRequestCR008Enabled
}

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

package utils

import play.api.libs.json._
import v1.models.abbreviatedReturn.AbbreviatedReturnModel
import v1.models.fullReturn.FullReturnModel

trait JsonFormatters {

  implicit val fullReturnWrites: Writes[FullReturnModel] = Writes { models =>
    JsObject(
      Json
        .obj(
          "declaration"                        -> models.declaration,
          "agentDetails"                       -> models.agentDetails,
          "reportingCompany"                   -> models.reportingCompany,
          "parentCompany"                      -> models.parentCompany,
          "publicInfrastructure"               -> models.publicInfrastructure,
          "groupCompanyDetails"                -> models.groupCompanyDetails,
          "submissionType"                     -> models.submissionType,
          "revisedReturnDetails"               -> models.revisedReturnDetails,
          "groupLevelElections"                -> models.groupLevelElections,
          "ukCompanies"                        -> models.ukCompanies,
          "numberOfUkCompanies"                -> models.numberOfUkCompanies,
          "angie"                              -> models.angie,
          "returnContainsEstimates"            -> models.returnContainsEstimates,
          "groupEstimateReason"                -> models.groupEstimateReason,
          "groupSubjectToInterestRestrictions" -> models.groupSubjectToInterestRestrictions,
          "groupSubjectToInterestReactivation" -> models.groupSubjectToInterestReactivation,
          "totalReactivation"                  -> models.totalReactivation,
          "totalRestrictions"                  -> models.totalRestrictions,
          "groupLevelAmount"                   -> models.groupLevelAmount,
          "adjustedGroupInterest"              -> models.adjustedGroupInterest,
          "aggregateNetTaxInterest"            -> models.aggregateNetTaxInterest,
          "aggregateAllocatedRestrictions"     -> models.aggregateAllocatedRestrictions,
          "aggregateTaxEBITDA"                 -> models.aggregateTaxEBITDA,
          "aggregateAllocatedReactivations"    -> models.aggregateAllocatedReactivations
        )
        .fields
        .filterNot(_._2 == JsNull)
    )
  }

  implicit val abbreviatedReturnWrites: Writes[AbbreviatedReturnModel] = Writes { models =>
    JsObject(
      Json
        .obj(
          "declaration"          -> models.declaration,
          "agentDetails"         -> models.agentDetails,
          "reportingCompany"     -> models.reportingCompany,
          "parentCompany"        -> models.parentCompany,
          "publicInfrastructure" -> models.publicInfrastructure,
          "groupCompanyDetails"  -> models.groupCompanyDetails,
          "submissionType"       -> models.submissionType,
          "revisedReturnDetails" -> models.revisedReturnDetails,
          "groupLevelElections"  -> models.groupLevelElections,
          "ukCompanies"          -> models.ukCompanies,
          "numberOfUkCompanies"  -> models.numberOfUkCompanies
        )
        .fields
        .filterNot(_._2 == JsNull)
    )
  }
}

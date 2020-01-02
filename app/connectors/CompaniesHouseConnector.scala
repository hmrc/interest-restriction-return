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

package connectors

import javax.inject.Inject

import config.AppConfig
import connectors.HttpHelper.CRNHttpResponse
import connectors.httpParsers.CompaniesHouseHttpParser.CompaniesHouseReads
import models.CRNModel
import models.requests.IdentifierRequest
import play.api.Logger
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class CompaniesHouseConnector @Inject()(httpClient: HttpClient,
                                        implicit val appConfig: AppConfig) {

  private[connectors] lazy val getCompanyInformationUrl: CRNModel => String = crn =>
    s"${appConfig.companiesHouseProxy}/companies-house-api-proxy/company/${crn.crn}"

  def validateCRN(crn: CRNModel)
                 (implicit hc: HeaderCarrier, ec: ExecutionContext, request: IdentifierRequest[_]): Future[CRNHttpResponse] = {

    Logger.debug(s"[CompaniesHouseConnector][getCompanyDetails] URL: ${getCompanyInformationUrl(crn)}")
    httpClient.GET[CRNHttpResponse](getCompanyInformationUrl(crn))(CompaniesHouseReads, hc, ec)
  }

}

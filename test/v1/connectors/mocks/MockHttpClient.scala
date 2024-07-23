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

package v1.connectors.mocks

import config.AppConfig
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.mockito.{ArgumentMatchers, Mockito}
import uk.gov.hmrc.http.client.{HttpClientV2, RequestBuilder}
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}

trait MockHttpClient {

  val mockHttpClient: HttpClientV2 = Mockito.mock(classOf[HttpClientV2])

  val mockRequestBuilder: RequestBuilder = Mockito.mock(classOf[RequestBuilder])
  val mockAppConfig: AppConfig           = Mockito.mock(classOf[AppConfig])

  def mockDesURL(url: String): Unit =
    when(mockAppConfig.desUrl).thenReturn(url)

  def mockPostCall(fullURL: String): Unit = {
    when(mockRequestBuilder.setHeader(any())).thenReturn(mockRequestBuilder)
    when(mockRequestBuilder.withBody(any())(any(), any(), any())).thenReturn(mockRequestBuilder)
    when(mockHttpClient.post(ArgumentMatchers.eq(url"$fullURL"))(any[HeaderCarrier]())).thenReturn(mockRequestBuilder)
  }

}

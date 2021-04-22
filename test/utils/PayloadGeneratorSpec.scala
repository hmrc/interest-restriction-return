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

import assets.appointReportingCompany.AppointReportingCompanyConstants._
import assets.revokeReportingCompany.RevokeReportingCompanyConstants._
import assets.AuthorisingCompanyConstants._
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json
import v1.models.CompanyNameModel

import java.io.File
import java.util.UUID

/*
  This test class is commented out as it's only needed to run locally
  To copy the file to clipboard the easiest way is running:
  xclip -sel c < FILELOCATION
  FILELOCATION is output by the test as a println
 */

class PayloadGeneratorSpec extends WordSpec with Matchers {

  /*"payload generator" should {

    "generate appoint payloads" when {
      "almost 10mb" in {
        val amount = 95233
        val authorisingCompanies = Seq.fill(amount)(
          authorisingCompanyModel.copy(
            companyName = CompanyNameModel(s"Company name ${UUID.randomUUID()}")
          ))

        val appointJson = appointReportingCompanyModelMax.copy(
          authorisingCompanies = authorisingCompanies
        )

        val file = new File("reportingCompanyPayload-9mb.json")
        if(file.exists()) {
          file.delete()
        }
        file.createNewFile()

        reflect.io.File(file.getAbsolutePath).writeAll(Json.toJson(appointJson).toString())

        println(s"Written to ${file.getAbsolutePath} with size ${file.length()}")
      }

      "over 10mb" in {
        val amount = 95234
        val authorisingCompanies = Seq.fill(amount)(
          authorisingCompanyModel.copy(
            companyName = CompanyNameModel(s"Company name ${UUID.randomUUID()}")
          ))

        val appointJson = appointReportingCompanyModelMax.copy(authorisingCompanies = authorisingCompanies)

        val file = new File("reportingCompanyPayload-10mb.json")
        if(file.exists()) {
          file.delete()
        }
        file.createNewFile()

        reflect.io.File(file.getAbsolutePath).writeAll(Json.toJson(appointJson).toString())

        println(s"Written to ${file.getAbsolutePath} with size ${file.length()}")
      }
    }

    "generate revoke payloads" when {
      "almost 10mb" in {
        val amount = 95234
        val authorisingCompanies = Seq.fill(amount)(
          authorisingCompanyModel.copy(
            companyName = CompanyNameModel(s"Company name ${UUID.randomUUID()}")
          ))

        val appointJson = revokeReportingCompanyModelMax.copy(authorisingCompanies = authorisingCompanies)

        val file = new File("revokeCompanyPayload-9mb.json")
        if(file.exists()) {
          file.delete()
        }
        file.createNewFile()

        reflect.io.File(file.getAbsolutePath).writeAll(Json.toJson(appointJson).toString())
        println(s"Written to ${file.getAbsolutePath} with size ${file.length()}")
      }

      "over 10mb" in {
        val amount = 95235
        val authorisingCompanies = Seq.fill(amount)(
          authorisingCompanyModel.copy(
            companyName = CompanyNameModel(s"Company name ${UUID.randomUUID()}")
          ))

        val appointJson = revokeReportingCompanyModelMax.copy(authorisingCompanies = authorisingCompanies)

        val file = new File("revokeCompanyPayload-10mb.json")
        if(file.exists()) {
          file.delete()
        }
        file.createNewFile()

        reflect.io.File(file.getAbsolutePath).writeAll(Json.toJson(appointJson).toString())

        println(s"Written to ${file.getAbsolutePath} with size ${file.length()}")
      }
    }
  }*/
}
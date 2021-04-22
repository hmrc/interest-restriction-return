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
import play.api.libs.json.Json
import v1.models.CompanyNameModel

import java.io.File
import java.util.UUID

/*
  To run use the following in sbt shell:
  runMain app.utils.PayloadGenerator <appoint|revoke> <amount> <filePath>
 */

object PayloadGenerator extends App {

  def generateAppointPayload(amount: Int, filePath: String): Unit = {
    val authorisingCompanies = Seq.fill(amount)(
      authorisingCompanyModel.copy(
        companyName = CompanyNameModel(s"Company name ${UUID.randomUUID()}")
      ))

    val appointJson = appointReportingCompanyModelMax.copy(authorisingCompanies = authorisingCompanies)
    val jsonString = Json.toJson(appointJson).toString()

    generateFile(jsonString, filePath)
  }

  def generateRevokePayload(amount: Int, filePath: String): Unit= {
    val authorisingCompanies = Seq.fill(amount)(
      authorisingCompanyModel.copy(
        companyName = CompanyNameModel(s"Company name ${UUID.randomUUID()}")
      ))

    val revokeJson = revokeReportingCompanyModelMax.copy(authorisingCompanies = authorisingCompanies)
    val jsonString = Json.toJson(revokeJson).toString()

    generateFile(jsonString, filePath)
  }

  private def generateFile(jsonString: String, filePath: String): Unit = {
    val file = new File(filePath)
    if(file.exists()) {
      file.delete()
    }
    file.createNewFile()

    reflect.io.File(file.getAbsolutePath).writeAll(jsonString)
    println(s"Written to ${file.getAbsolutePath} with size ${file.length()}")
  }

  args.toList match {
    case "appoint" :: amount :: filePath :: _ => generateAppointPayload(amount.toInt, filePath)
    case "revoke" :: amount :: filePath :: _ => generateRevokePayload(amount.toInt, filePath)
    case _ => println("Run with <appoint|revoke> <amount> <filePath>")
  }

}
/*
 * Copyright 2023 HM Revenue & Customs
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

import org.scalamock.scalatest.MockFactory
import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Result
import play.api.test.Helpers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

trait UnitSpec extends AnyWordSpecLike with Matchers with OptionValues with MockFactory {

  import scala.concurrent.duration._
  import scala.concurrent.{Await, Future}

  implicit val defaultTimeout: FiniteDuration = 5 seconds

  implicit def extractAwait[A](future: Future[A]): A = await[A](future)

  def await[A](future: Future[A])(implicit timeout: Duration): A = Await.result(future, timeout)

  // Convenience to avoid having to wrap andThen() parameters in Future.successful
  implicit def liftFuture[A](v: A): Future[A] = Future.successful(v)

  def status(of: Result): Int = of.header.status

  def status(of: Future[Result]): Int = status(Await.result(of, defaultTimeout))

  def jsonBodyOf(result: Result): JsValue =
    Json.parse(bodyOf(result))

  def jsonBodyOf(resultF: Future[Result]): Future[JsValue] =
    resultF.map(jsonBodyOf)

  def bodyOf(result: Result): String =
    Helpers.contentAsString(result)(Helpers.defaultAwaitTimeout)

  def bodyOf(resultF: Future[Result]): Future[String] =
    resultF.map(bodyOf)
}

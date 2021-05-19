import play.core.PlayVersion.current
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-27"    % "3.4.0",
    "org.typelevel"           %% "cats-core"                    % "2.0.0",
    "com.chuusai"             %% "shapeless"                    % "2.3.3",
    "com.typesafe.play"       %% "play-json-joda"               % "2.7.4"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-27"       % "3.0.0"                % Test,
    "uk.gov.hmrc"             %% "hmrctest"                     % "3.10.0-play-26"         % "test, it",
    "org.scalatest"           %% "scalatest"                    % "3.0.8"                 % "test",
    "com.typesafe.play"       %% "play-test"                    % current                 % "test",
    "org.pegdown"             %  "pegdown"                      % "1.6.0"                 % "test, it",
    "org.scalatestplus.play"  %% "scalatestplus-play"           % "4.0.0"                 % "test, it",
    "org.scalamock"           %% "scalamock-scalatest-support"  % "3.6.0"                 % "test",
    "com.github.tomakehurst"  %  "wiremock-standalone"          % "2.22.0"                % "it"
  )

}

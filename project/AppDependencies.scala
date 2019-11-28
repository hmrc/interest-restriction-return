import play.core.PlayVersion.current
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-play-26"            % "1.3.0"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-play-26"            % "1.3.0"                 % Test classifier "tests",
    "uk.gov.hmrc"             %% "hmrctest"                     % "3.9.0-play-26"         % "test, it",
    "org.scalatest"           %% "scalatest"                    % "3.0.8"                 % "test",
    "com.typesafe.play"       %% "play-test"                    % current                 % "test",
    "org.pegdown"             %  "pegdown"                      % "1.6.0"                 % "test, it",
    "org.scalatestplus.play"  %% "scalatestplus-play"           % "3.1.2"                 % "test, it",
    "org.scalamock"           %% "scalamock-scalatest-support"  % "3.6.0"                 % "test",
    "com.github.fge"          %  "json-schema-validator"        % "2.2.6"                 % "test",
    "com.github.tomakehurst"  %  "wiremock-standalone"          % "2.22.0"                % "it"
  )

}

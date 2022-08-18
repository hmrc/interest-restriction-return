import play.core.PlayVersion.current
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"       %% "bootstrap-backend-play-28" % "6.4.0",
    "org.typelevel"     %% "cats-core"                 % "2.8.0",
    "com.chuusai"       %% "shapeless"                 % "2.3.9",
    "com.typesafe.play" %% "play-json-joda"            % "2.9.2"
  )

  def test(scope: String = "test, it"): Seq[ModuleID] = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-28"      % "6.4.0",
    "org.scalatest"          %% "scalatest"                   % "3.2.13",
    "com.typesafe.play"      %% "play-test"                   % current,
    "org.pegdown"             % "pegdown"                     % "1.6.0",
    "org.scalatestplus.play" %% "scalatestplus-play"          % "5.1.0",
    "org.scalamock"          %% "scalamock-scalatest-support" % "3.6.0",
    "com.github.tomakehurst"  % "wiremock-standalone"         % "2.22.0",
    "com.vladsch.flexmark"    % "flexmark-all"                % "0.62.2"
  ).map(_ % scope)

}

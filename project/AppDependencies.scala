import play.core.PlayVersion.current
import sbt._

object AppDependencies {
  private lazy val bootstrapPlayVersion = "7.12.0"

  val compile = Seq(
    "uk.gov.hmrc"       %% "bootstrap-backend-play-28" % bootstrapPlayVersion,
    "org.typelevel"     %% "cats-core"                 % "2.9.0",
    "com.chuusai"       %% "shapeless"                 % "2.3.10",
    "com.typesafe.play" %% "play-json-joda"            % "2.9.3"
  )

  def test(scope: String = "test, it"): Seq[ModuleID] = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-28"      % bootstrapPlayVersion,
    "org.scalatest"          %% "scalatest"                   % "3.2.14",
    "com.typesafe.play"      %% "play-test"                   % current,
    "org.pegdown"             % "pegdown"                     % "1.6.0",
    "org.scalatestplus.play" %% "scalatestplus-play"          % "5.1.0",
    "org.scalamock"          %% "scalamock-scalatest-support" % "3.6.0",
    "com.github.tomakehurst"  % "wiremock-standalone"         % "2.27.2",
    "com.vladsch.flexmark"    % "flexmark-all"                % "0.62.2"
  ).map(_ % scope)

}

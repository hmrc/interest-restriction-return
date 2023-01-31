import sbt._

object AppDependencies {
  private lazy val bootstrapPlayVersion = "7.13.0"

  private lazy val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"       %% "bootstrap-backend-play-28" % bootstrapPlayVersion,
    "org.typelevel"     %% "cats-core"                 % "2.9.0",
    "com.chuusai"       %% "shapeless"                 % "2.3.10",
    "com.typesafe.play" %% "play-json-joda"            % "2.9.4"
  )

  private lazy val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"           %% "bootstrap-test-play-28" % bootstrapPlayVersion,
    "org.scalatest"         %% "scalatest"              % "3.2.15",
    "com.typesafe.play"     %% "play-test"              % "2.8.19",
    "org.scalamock"         %% "scalamock"              % "5.2.0",
    "com.github.tomakehurst" % "wiremock-standalone"    % "2.27.2",
    "com.vladsch.flexmark"   % "flexmark-all"           % "0.62.2"
  ).map(_ % "test, it")

  def apply(): Seq[ModuleID]           = compile ++ test
}

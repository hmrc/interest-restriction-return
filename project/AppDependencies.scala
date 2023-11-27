import sbt.*

object AppDependencies {
  private lazy val bootstrapPlayVersion = "7.23.0"

  private lazy val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"   %% "bootstrap-backend-play-28" % bootstrapPlayVersion,
    "org.typelevel" %% "cats-core"                 % "2.10.0",
    "com.chuusai"   %% "shapeless"                 % "2.3.10"
  )

  private lazy val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"         %% "bootstrap-test-play-28" % bootstrapPlayVersion,
    "org.scalatest"       %% "scalatest"              % "3.2.17",
    "org.scalamock"       %% "scalamock"              % "5.2.0",
    "org.wiremock"         % "wiremock-standalone"    % "3.3.1",
    "com.vladsch.flexmark" % "flexmark-all"           % "0.64.8"
  ).map(_ % "test, it")

  def apply(): Seq[ModuleID]           = compile ++ test
}

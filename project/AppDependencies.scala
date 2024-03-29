import sbt.*

object AppDependencies {
  private lazy val bootstrapPlayVersion = "8.4.0"

  private lazy val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"   %% "bootstrap-backend-play-30" % bootstrapPlayVersion,
    "org.typelevel" %% "cats-core"                 % "2.10.0",
    "com.chuusai"   %% "shapeless"                 % "2.3.10"
  )

  private lazy val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"   %% "bootstrap-test-play-30" % bootstrapPlayVersion,
    "org.scalamock" %% "scalamock"              % "5.2.0"
  ).map(_ % Test)

  def apply(): Seq[ModuleID]           = compile ++ test
}

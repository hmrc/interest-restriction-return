import sbt.*

object AppDependencies {

  private lazy val bootstrapPlayVersion = "9.13.0"

  private lazy val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"   %% "bootstrap-backend-play-30" % bootstrapPlayVersion,
    "org.typelevel" %% "cats-core"                 % "2.13.0"
  )

  private lazy val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30" % bootstrapPlayVersion
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test

}

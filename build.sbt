import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings

val appName = "interest-restriction-return"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    majorVersion := 0,
    scalaVersion := "2.12.17",
    scalacOptions ++= Seq("-feature", "-language:postfixOps", "-Ypartial-unification", "-Wconf:src=routes/.*:s"),
    libraryDependencies ++= AppDependencies(),
    // To resolve a bug with version 2.x.x of the scoverage plugin - https://github.com/sbt/sbt/issues/6997
    libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always,
    PlayKeys.playDefaultPort := 9261
  )
  .settings(scoverageSettings: _*)
  .settings(inConfig(Test)(testSettings): _*)
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(playRoutesSettings)

lazy val scoverageSettings = {
  import scoverage._
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;config.*;.*(AuthService|BuildInfo|Routes).*",
    ScoverageKeys.coverageMinimumStmtTotal := 99,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val testSettings: Seq[Def.Setting[_]] = Seq(
  fork := true,
  javaOptions ++= Seq(
    "-Dconfig.resource=test.application.conf"
  )
)

lazy val playRoutesSettings = {
  import play.sbt.routes.RoutesKeys
  RoutesKeys.routesImport := Seq.empty
}

addCommandAlias("scalafmtAll", "all scalafmtSbt scalafmt Test/scalafmt")
addCommandAlias("scalastyleAll", "all scalastyle Test/scalastyle")

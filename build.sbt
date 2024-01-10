import scoverage.ScoverageKeys.*
import uk.gov.hmrc.DefaultBuildSettings

val appName = "interest-restriction-return"

ThisBuild / majorVersion := 0
ThisBuild / scalaVersion := "2.13.12"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    majorVersion := 0,
    scalaVersion := "2.13.12",
    scalacOptions ++= Seq("-feature", "-language:postfixOps", "-Wconf:src=routes/.*:s"),
    libraryDependencies ++= AppDependencies(),
    PlayKeys.playDefaultPort := 9261
  )
  .settings(scoverageSettings)

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test")
  .settings(DefaultBuildSettings.itSettings())

lazy val scoverageSettings =
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    coverageExcludedPackages := "<empty>;config.*;.*Routes.*",
    coverageMinimumStmtTotal := 100,
    coverageFailOnMinimum := true,
    coverageHighlighting := true
  )

addCommandAlias("scalafmtAll", "all scalafmtSbt scalafmt Test/scalafmt it/Test/scalafmt")
addCommandAlias("scalastyleAll", "all scalastyle Test/scalastyle it/Test/scalastyle")

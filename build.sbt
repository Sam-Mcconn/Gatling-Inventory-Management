enablePlugins(GatlingPlugin)

scalaVersion := "2.13.14"

scalacOptions := Seq(
  "-encoding", "UTF-8", "-release:8", "-deprecation",
  "-feature", "-unchecked", "-language:implicitConversions", "-language:postfixOps")

val gatlingVersion = "3.11.5"
libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "test,it"
libraryDependencies += "io.gatling"            % "gatling-test-framework"    % gatlingVersion % "test,it"

val scalacheckVersion = "1.18.0"
libraryDependencies += "org.scalacheck" %% "scalacheck" % scalacheckVersion % Test

val circeVersion = "0.15.0-M1"
libraryDependencies += "io.circe" %% "circe-core" % circeVersion
libraryDependencies += "io.circe" %% "circe-generic" % circeVersion

val scalaTest = "3.2.19"
libraryDependencies += "org.scalatest" %% "scalatest" % scalaTest % Test

// Enterprise Cloud (https://cloud.gatling.io/) configuration reference: https://gatling.io/docs/gatling/reference/current/extensions/sbt_plugin/#working-with-gatling-enterprise-cloud
// Enterprise Self-Hosted configuration reference: https://gatling.io/docs/gatling/reference/current/extensions/sbt_plugin/#working-with-gatling-enterprise-self-hosted

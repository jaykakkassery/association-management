name := """association-management"""
organization := "net.jptech"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)


val circeVersion = "0.12.3"


scalaVersion := "2.12.8"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies ++= Seq(
  guice,
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "mysql" % "mysql-connector-java" % "8.0.15"
)
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
javaOptions in Universal ++= Seq(
  "-Dpidfile.path=/dev/null"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "net.jptech.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "net.jptech.binders._"

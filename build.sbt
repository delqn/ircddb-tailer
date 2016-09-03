name := "ircddb-tailer"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  ws
)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"


// -- requirements for Tests
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
libraryDependencies += "org.specs2" %% "specs2-core" % "3.0" % "test"
scalacOptions in Test ++= Seq("-Yrangepos")

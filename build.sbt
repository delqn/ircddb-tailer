name := "ircddb-tailer"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

scalaVersion := "2.11.8"

// Repo for test libs
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  jdbc,
  ws,

  // Required for PostgreSQL
  "org.squeryl" %% "squeryl" % "0.9.5-7",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "com.typesafe.play" %% "play-iteratees" % "2.6.0",

  "org.apache.commons" % "commons-dbcp2" % "2.0.1",

  // Required for testing
  "org.scalactic" %% "scalactic" % "3.0.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.specs2" %% "specs2-core" % "3.0" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

name := """defenders"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "junit" % "junit" % "4.11",  
  "com.novocode" % "junit-interface" % "0.11",
  "org.seleniumhq.selenium" % "selenium-java" % "2.35.0",
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.1",
  "com.datastax.cassandra" % "cassandra-driver-mapping" % "2.1.1"
)

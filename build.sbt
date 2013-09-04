name := "Hierarchy"

version := "0.1"

scalaVersion := "2.10.2"

libraryDependencies ++= List(
    "com.typesafe.slick" %% "slick" % "1.0.1",
    "ch.qos.logback" % "logback-classic" % "1.0.13",
    "org.clapper" %% "grizzled-slf4j" % "1.0.1",
    "com.h2database" % "h2" % "1.3.166"
    //"org.postgresql" % "postgresql" % "9.2-1003-jdbc4"
)

libraryDependencies += "org.specs2" %% "specs2" % "2.1.1" % "test"

name := "quoteparser"

organization := "io.github.silvaren"

version := "1.0"

scalaVersion := "2.11.0"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.14.0"
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test->default"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

crossPaths := false
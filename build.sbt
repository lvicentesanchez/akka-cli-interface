name := "akka-cli-interface"

version := "0.1-SNAPNSHOT"

scalaVersion := "2.10.1"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

resolvers ++= Seq(
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
)

libraryDependencies ++= Seq(
  "com.ning" % "async-http-client" % "1.7.13",
  "com.typesafe.akka" %% "akka-actor" % "2.1.+",
  "io.netty" % "netty" % "3.6.5.Final",
  "net.databinder.dispatch" %% "dispatch-core" % "0.10.+"
)

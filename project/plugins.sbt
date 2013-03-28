resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

logLevel := Level.Warn

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.3.0")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.2")

addSbtPlugin("com.orrsella" % "sbt-sublime" % "1.0.4")

addSbtPlugin("org.ensime" % "ensime-sbt-cmd" % "0.1.1")
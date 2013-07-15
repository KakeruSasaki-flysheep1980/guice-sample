import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "guice-sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "net.codingwell" % "scala-guice_2.10" % "3.0.2",
    "net.codingwell" %% "scala-guice" % "3.0.2",
    "com.github" % "cache4guice" % "0.2"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked"),
    resolvers ++= Seq(
      "Local Maven Repository" at Path.userHome.asFile.toURI.toURL+"/.m2/repository"
    )
    // Add your own project settings here      
  )

}

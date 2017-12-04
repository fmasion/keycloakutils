name := "keycloak"


organization in ThisBuild := "com.kreactive"

scalaVersion in Global := "2.12.4"

crossScalaVersions in ThisBuild := Seq("2.11.11", "2.12.4")

scalacOptions in ThisBuild ++= Seq("-deprecation")

lazy val basicModel = project in file("BasicModel")

lazy val authenticator = project in file("authenticator") dependsOn basicModel aggregate basicModel

bintrayReleaseOnPublish in ThisBuild := false

bintrayOrganization in ThisBuild := Some("kreactive")

licenses in ThisBuild := List(
  ("Apache-2.0",
    url("https://www.apache.org/licenses/LICENSE-2.0"))
)

homepage in ThisBuild := Some(url("https://github.com/kreactive"))

publishTo := Some("kreactive bintray" at "https://api.bintray.com/maven/kreactive/maven/keycloak")

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

publishMavenStyle := true

publishArtifact := false




// don't publish root artifact
packagedArtifacts in file(".") := Map.empty

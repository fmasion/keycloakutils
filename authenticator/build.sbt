name := "keycloak-authenticator"

version := "3.2.0-b"

libraryDependencies ++= Seq(
  "org.keycloak" % "keycloak-jboss-adapter-core" % Version.keycloak,
  "org.keycloak" % "keycloak-adapter-core" % Version.keycloak,
  "org.keycloak" % "keycloak-core" % Version.keycloak,
  "org.jboss.logging" % "jboss-logging" % Version.jbossLogging,
  
  // Test dependencies
  "org.scalatest" %% "scalatest" % Version.scalaTest % "test"
)
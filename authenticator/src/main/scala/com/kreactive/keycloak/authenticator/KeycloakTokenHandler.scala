package com.kreactive.keycloak.authenticator

import java.io.ByteArrayInputStream

import org.keycloak.adapters.rotation.AdapterRSATokenVerifier
import org.keycloak.adapters.{KeycloakDeployment, KeycloakDeploymentBuilder}
import org.keycloak.representations.AccessToken

import scala.util.Try

case class KeycloakTokenHandler(keycloakDeployment: KeycloakDeployment) {

  def tokenInfos(tokenStr: String, activeFilter: Boolean = true): Option[TokenInfos] =
    rsaCheckToken(tokenStr, activeFilter).flatMap(TokenInfos(_))

  @deprecated("use rsaCheckToken instead", "1.0.5")
  def extractToken(tokenStr: String, activeFilter: Boolean): Option[AccessToken] =
    rsaCheckToken(tokenStr, activeFilter)

  //default activeFilter as false for backward compatibility
  def rsaCheckToken(tokenStr: String, activeFilter: Boolean = false ): Option[AccessToken] =
    Try(AdapterRSATokenVerifier.verifyToken(tokenStr, keycloakDeployment, activeFilter, true)).toOption

  def isActive(accessToken: AccessToken) = accessToken.isActive && accessToken.getIssuedAt > keycloakDeployment.getNotBefore
}

object KeycloakTokenHandler {
  def apply(keycloakJson: String): Try[KeycloakTokenHandler] = {
    Try(KeycloakDeploymentBuilder.build(new ByteArrayInputStream(keycloakJson.getBytes))).map { keycloakDeployment =>
      new KeycloakTokenHandler(keycloakDeployment)
    }
  }
}
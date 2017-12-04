package com.kreactive.keycloak.authenticator


import java.time.Instant
import java.util

import com.kreactive.model.{ApplicationId, Realm, Role, UserId}
import org.keycloak.representations.AccessToken

import scala.collection.JavaConverters._
import scala.language.implicitConversions

case class OptionalInfos(
                          email: Option[String],
                          firstname: Option[String],
                          lastname: Option[String],
                          locale: Option[String]
                        )

case class TimeWindow(
                       issuedAt: Instant,
                       notBefore: Instant,
                       expiresAt: Instant
) {
  def active = {
    val now = Instant.now
    expiresAt.isAfter(now) && issuedAt.isBefore(now) && notBefore.isBefore(now)
  }
}
case class TokenInfos(
                       realm: Realm,
                       app: ApplicationId,
                       userId: UserId,
                       roles: Map[ApplicationId, List[Role]],
                       window: TimeWindow,
                       optional: OptionalInfos
                     ) {
  def getRealmRoles = roles.get(Constant.REALM)
  def getAppsRoles = roles.filterKeys(_ != Constant.REALM)
  def getAppRoles(app: ApplicationId) = roles.get(app)
  def isMailValid = roles.get(Constant.REALM).exists(_.contains(Constant.MAIL_VALIDE))
  def isMailBounce = roles.get(Constant.REALM).exists(_.contains(Constant.MAIL_BOUNCE))
  def active = window.active
}

object TokenInfos {
  implicit def sec2instant(sec: Int): Instant = Instant.ofEpochSecond(sec)

  def apply(access: AccessToken): Option[TokenInfos] = for {
    realm <- Option(access.getIssuer).flatMap(_.split("/auth/realms/").toList.drop(1).headOption)
    app <- Option(access.getIssuedFor)
    UserId(userId) <- Option(access.getSubject)
    appRoles <- Option(access.getResourceAccess().asScala.map { case (ap, at) => (ApplicationId(ap), at.getRoles.asScala.toList.map(Role(_))) }.toMap)
    javaRealmRoles <- Option(access.getRealmAccess)
      .fold(Option[util.Set[String]](new util.HashSet[String]())) {ra => Option(ra.getRoles)}
    issuedAt <- Option(access.getIssuedAt)
    notBefore <- Option(access.getNotBefore)
    expireAt <- Option(access.getExpiration)
  } yield {
    val realmRoles = Map(Constant.REALM -> javaRealmRoles.asScala.map(Role(_)).toList)
    val optional = OptionalInfos(
      Option(access.getEmail),
      Option(access.getGivenName),
      Option(access.getFamilyName),
      Option(access.getLocale)
    )
    val window = TimeWindow(issuedAt, notBefore, expireAt)

    TokenInfos(
      realm = Realm(realm),
      app = ApplicationId(app),
      userId = userId,
      appRoles ++ realmRoles,
      window,
      optional
    )
  }
}

object Constant {
  val REALM = ApplicationId("realm")
  val MAIL_VALIDE = Role("mail_valide")
  val MAIL_BOUNCE = Role("mail_bounce")
}

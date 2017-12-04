package com.kreactive.model

import java.util.UUID

import com.kreactive.capsule.ValueClass

import scala.util.Try

case class UserId(value: UUID) extends AnyVal {
  override def toString: String = value.toString
}

object UserId extends ValueClass[UUID, UserId] {
  override def construct: (UUID) => UserId = apply
  override def deconstruct: (UserId) => UUID = _.value

  def unapply(str: String): Option[UserId] =
    Try(UUID.fromString(str)).toOption.map(apply)
}

package com.kreactive.model

import com.kreactive.capsule.StringValueClass

final case class ApplicationId(value: String) {
  override def toString = value

  override def equals(obj: Any): Boolean =
    obj.isInstanceOf[ApplicationId] &&
      value.equalsIgnoreCase(obj.asInstanceOf[ApplicationId].value)

  override def hashCode(): Int =
    value.toLowerCase.hashCode
}

object ApplicationId extends StringValueClass[ApplicationId] {
  override def construct: (String) => ApplicationId = apply

  override def deconstruct: (ApplicationId) => String = _.value
}
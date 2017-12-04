package com.kreactive.model

import com.kreactive.capsule.StringValueClass

case class Role(name: String) extends AnyVal {
  override def toString = name
}

object Role extends StringValueClass[Role] {
  override def construct: (String) => Role = apply

  override def deconstruct: (Role) => String = _.name
}

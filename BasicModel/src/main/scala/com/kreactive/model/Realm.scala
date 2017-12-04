package com.kreactive.model

import com.kreactive.capsule.StringValueClass

case class Realm(name: String) extends AnyVal {
  override def toString = name
}

object Realm extends StringValueClass[Realm] {
  override def construct: (String) => Realm = apply

  override def deconstruct: (Realm) => String = _.name
}






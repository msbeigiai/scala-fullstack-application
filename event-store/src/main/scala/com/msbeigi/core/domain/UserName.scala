package com.msbeigi.core.domain

import io.circe.{Codec, Decoder}
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec

case class UserName(userName: String) extends AnyVal

object UserName {
  implicit val codec: Codec[UserName] = deriveUnwrappedCodec[UserName]
}

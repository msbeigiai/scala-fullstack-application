package com.msbeigi.core.domain

import io.circe.{Codec, Decoder}
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec

case class Message(message: String) extends AnyVal

object Message {
  implicit val codec: Codec[Message] = deriveUnwrappedCodec[Message]

}
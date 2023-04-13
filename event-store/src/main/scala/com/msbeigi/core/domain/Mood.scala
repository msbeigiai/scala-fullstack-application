package com.msbeigi.core.domain

import io.circe.{Codec, Decoder}
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec

case class Mood(mood: String) extends AnyVal

object Mood {
  implicit val codec: Codec[Mood] = deriveUnwrappedCodec[Mood]

}

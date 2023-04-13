package com.msbeigi.core.domain

import io.circe.{Codec, Decoder}
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec

case class Regret(regret: String) extends AnyVal

object Regret {
  implicit val codec: Codec[Regret] = deriveUnwrappedCodec[Regret]
}

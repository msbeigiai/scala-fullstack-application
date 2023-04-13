package com.msbeigi.core.domain

import io.circe.{Codec, Decoder}
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec

case class PlanToAchieve(planToAchieve: String) extends AnyVal

object PlanToAchieve {
  implicit val codec: Codec[PlanToAchieve] = deriveUnwrappedCodec[PlanToAchieve]

}

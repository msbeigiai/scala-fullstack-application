package com.msbeigi.core.http.routes

import cats.effect.Async
import cats.implicits.catsSyntaxApply
import com.msbeigi.core.domain.Event
import com.msbeigi.core.kafka.KafkaProducerAlgebra
import org.http4s.{EntityDecoder, HttpRoutes}
import org.http4s.circe.jsonOf
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

case class EventsRoutes[F[_] : Async] (kafkaProducerAlgebra: KafkaProducerAlgebra[F]) extends Http4sDsl[F] {
  private[routes] val prefix = "/api/v1/event"
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root =>
      implicit val entityDecoder: EntityDecoder[F, Event] = jsonOf[F, Event]
      req.attemptAs[Event].foldF(
        _ => BadRequest("An error occurred check the request body"),
        event => kafkaProducerAlgebra.publish(event) *> Created("Created \uD83D\uDC81"))
  }

  val router: HttpRoutes[F] = Router(prefix -> routes)
}

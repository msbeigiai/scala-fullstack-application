package com.msbeigi.core.server


import cats.effect.{Async, ExitCode}
import cats.effect.std.Console
import com.msbeigi.core.config.Config
import com.msbeigi.core.http.routes.EventsRoutes
import com.msbeigi.core.kafka.{KafkaConsumerAlgebra, KafkaProducerAlgebra}
import org.http4s.blaze.server.BlazeServerBuilder
import fs2.Stream

import scala.concurrent.ExecutionContext.global

object Server {

  def stream[F[_] : Async : Console](config: Config): Stream[F, ExitCode] = for {
    _ <- Stream.eval(Console[F].println("Starting the server \uD83D\uDE80"))
    kafkaProducerAlgebra: KafkaProducerAlgebra[F] = KafkaProducerAlgebra.impl[F](config.kafkaConfig)
    kafkaConsumerAlgebra: KafkaConsumerAlgebra[F] = KafkaConsumerAlgebra.impl[F](config.kafkaConfig)
    server <- BlazeServerBuilder[F]
      .bindHttp(config.serverConfig.port.value, config.serverConfig.host.value)
      .withHttpApp(EventsRoutes[F](kafkaProducerAlgebra).router.orNotFound)
      .serve
      .concurrently(kafkaConsumerAlgebra.consume)
  } yield server

}

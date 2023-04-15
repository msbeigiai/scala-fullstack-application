package com.msbeigi.core.server


import cats.effect.{Async, ExitCode}
import cats.effect.std.Console
import com.msbeigi.core.config.Config
import com.msbeigi.core.elasticsearch.EsAlgebra
import com.msbeigi.core.http.routes.EventsRoutes
import com.msbeigi.core.kafka.{KafkaConsumerAlgebra, KafkaProducerAlgebra}
import com.msbeigi.core.service.IndexService
import org.http4s.blaze.server.BlazeServerBuilder
import fs2.Stream
import org.elasticsearch.client.RestHighLevelClient

import scala.concurrent.ExecutionContext.global

object Server {

  def stream[F[_] : Async : Console](
                                      config: Config,
                                      restHighLevelClient: RestHighLevelClient
                                    ): Stream[F, ExitCode] =
    for {
    _ <- Stream.eval(Console[F].println("Starting the server \uD83D\uDE80"))
//    _ <- Stream.eval(esAlgebra.createIndex)
    kafkaProducerAlgebra: KafkaProducerAlgebra[F] = KafkaProducerAlgebra.impl[F](config.kafkaConfig)
    kafkaConsumerAlgebra: KafkaConsumerAlgebra[F] = KafkaConsumerAlgebra.impl[F](config.kafkaConfig)
    esAlgebra = EsAlgebra.impl[F](config.esConfig, restHighLevelClient)
    indexService = IndexService.impl[F](kafkaConsumerAlgebra, esAlgebra)
    server <- BlazeServerBuilder[F]
      .bindHttp(config.serverConfig.port.value, config.serverConfig.host.value)
      .withHttpApp(EventsRoutes[F](kafkaProducerAlgebra).router.orNotFound)
      .serve
      .concurrently(indexService.persist)
  } yield server

}
